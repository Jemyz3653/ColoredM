#!/usr/bin/env python3
"""Generate the door, trapdoor and sand assets without bundling vanilla textures."""

import json
import argparse
from zipfile import ZipFile
from itertools import product
from pathlib import Path


RESOURCES = Path(__file__).resolve().parents[1] / "src/main/resources"
ASSETS = "assets/coloredm"
DATA = "data/coloredm"
COLORS = (
    "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
    "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black",
)
FAMILIES = {"door": "oak_door", "trapdoor": "oak_trapdoor", "sand": "sand"}
ROTATIONS = {"north": 0, "east": 90, "south": 180, "west": 270}
NAMES = {
    "en_us": dict(door="Door", trapdoor="Trapdoor", sand="Sand"),
    "ru_ru": dict(door="дверь", trapdoor="люк", sand="песок"),
    "uk_ua": dict(door="двері", trapdoor="люк", sand="пісок"),
}
UKRAINIAN_DOORS = (
    "Білі", "Помаранчеві", "Пурпурові", "Блакитні", "Жовті", "Лаймові", "Рожеві", "Сірі",
    "Світло-сірі", "Бірюзові", "Фіолетові", "Сині", "Коричневі", "Зелені", "Червоні", "Чорні",
)


def read(path):
    return json.loads((RESOURCES / path).read_text(encoding="utf-8"))


def write(path, value):
    destination = RESOURCES / path
    destination.parent.mkdir(parents=True, exist_ok=True)
    destination.write_text(json.dumps(value, indent=2, ensure_ascii=False) + "\n", encoding="utf-8", newline="\n")


def extend(values, additions):
    values.extend(value for value in additions if value not in values)


def extend_tag(path, additions):
    tag = read(path) if (RESOURCES / path).exists() else {"values": []}
    extend(tag["values"], additions)
    write(path, tag)


def block_model(name, parent, **textures):
    write(f"{ASSETS}/models/block/{name}.json", {"parent": f"minecraft:block/{parent}", "textures": textures})


def variant(model, degrees=0):
    entry = {"model": f"coloredm:block/{model}"}
    if degrees:
        entry["y"] = degrees
    return entry


def door_models(color):
    name = f"{color}_door"
    textures = {half: f"coloredm:block/{color}_oak_door_{half}" for half in ("bottom", "top")}
    for half, hinge, opened in product(("bottom", "top"), ("left", "right"), (False, True)):
        model = f"door_{half}_{hinge}" + ("_open" if opened else "")
        block_model(f"{color}_{model}", model, **textures)

    variants = {}
    for facing, half, hinge, opened in product(ROTATIONS, ("lower", "upper"), ("left", "right"), (False, True)):
        rotation = ROTATIONS[facing] - 90
        if opened:
            rotation += 90 if hinge == "left" else -90
        vertical = "bottom" if half == "lower" else "top"
        model = f"{name}_{vertical}_{hinge}" + ("_open" if opened else "")
        variants[f"facing={facing},half={half},hinge={hinge},open={str(opened).lower()}"] = variant(model, rotation % 360)
    write(f"{ASSETS}/blockstates/{name}.json", {"variants": variants})
    write(f"{ASSETS}/models/item/{name}.json", {
        "parent": "minecraft:item/generated",
        "textures": {"layer0": f"coloredm:item/{color}_oak_door"},
    })
    return f"coloredm:item/{name}"


def trapdoor_models(color):
    name = f"{color}_trapdoor"
    for part in ("bottom", "top", "open"):
        block_model(f"{name}_{part}", f"template_trapdoor_{part}", texture=f"coloredm:block/{color}_oak_trapdoor")
    variants = {}
    for facing, half, opened in product(ROTATIONS, ("bottom", "top"), (False, True)):
        part = "open" if opened else half
        variants[f"facing={facing},half={half},open={str(opened).lower()}"] = variant(
            f"{name}_{part}", ROTATIONS[facing] if opened else 0
        )
    write(f"{ASSETS}/blockstates/{name}.json", {"variants": variants})
    return f"coloredm:block/{name}_bottom"


def sand_models(color):
    name = f"{color}_sand"
    block_model(name, "cube_all", all=f"coloredm:block/{color}_sand")
    write(f"{ASSETS}/blockstates/{name}.json", {"variants": {"": variant(name)}})
    return f"coloredm:block/{name}"


def generate_variant(color, family, item_model):
    name = f"{color}_{family}"
    block_id = f"coloredm:{name}"
    write(f"{ASSETS}/items/{name}.json", {"model": {"type": "minecraft:model", "model": item_model}})
    write(f"{DATA}/recipe/dye/{name}.json", {
        "type": "minecraft:crafting_transmute",
        "category": "building",
        "group": f"coloredm_{family}",
        "input": f"#coloredm:dyeable/{family}",
        "material": f"minecraft:{color}_dye",
        "result": {"id": block_id},
    })
    item = {"type": "minecraft:item", "name": block_id}
    if family == "door" or family.endswith("_door"):
        item["condition"] = {"type": "minecraft:match_block", "blocks": block_id, "state": {"half": "lower"}}
    write(f"{DATA}/loot_table/blocks/{name}.json", {
        "type": "minecraft:block",
        "pools": [{"rolls": 1, "condition": {"type": "minecraft:survives_explosion"}, "entries": [item]}],
        "random_sequence": f"coloredm:blocks/{name}",
    })


def update_atlas(atlas, textures):
    path = f"assets/minecraft/atlases/{atlas}.json"
    definition = read(path)
    source = next(source for source in definition["sources"] if source["type"] == "coloredm:dyed")
    source["textures"].update(textures)
    write(path, definition)


def update_translations():
    for locale, names in NAMES.items():
        path = f"{ASSETS}/lang/{locale}.json"
        strings = read(path)
        for i, color in enumerate(COLORS):
            for family, name in names.items():
                if locale == "uk_ua" and family == "door":
                    adjective = UKRAINIAN_DOORS[i]
                else:
                    template = "ladder" if family == "door" else "lantern" if locale == "uk_ua" else "chest"
                    adjective = strings[f"block.coloredm.{color}_{template}"].rsplit(" ", 1)[0]
                strings[f"block.coloredm.{color}_{family}"] = f"{adjective} {name}"
        write(path, dict(sorted(strings.items())))


def all_doors(vanilla, recipes):
    """Inherit vanilla models and rotations, including orientable trapdoors."""
    block_textures, item_textures = {}, {}
    strings = {locale: read(f"{ASSETS}/lang/{locale}.json") for locale in NAMES}
    materials = {
        'oak':('дуб','дуб'), 'spruce':('ель','ялина'), 'birch':('берёза','береза'),
        'jungle':('тропическое дерево','тропічне дерево'), 'acacia':('акация','акація'),
        'dark_oak':('тёмный дуб','темний дуб'), 'mangrove':('мангр','мангр'),
        'cherry':('вишня','вишня'), 'bamboo':('бамбук','бамбук'),
        'crimson':('багряное дерево','багряне дерево'), 'warped':('искажённое дерево','химерне дерево'),
        'pale_oak':('бледный дуб','блідий дуб'), 'poplar':('тополь','тополя'),
        'iron':('железо','залізо'), 'copper':('медь','мідь'),
    }
    with ZipFile(vanilla) as archive:
        bases = sorted(path.rsplit('/', 1)[1][:-5] for path in archive.namelist()
                       if path.startswith('assets/minecraft/blockstates/')
                       and path.endswith(('_door.json', '_trapdoor.json')))
        names = json.loads(archive.read('assets/minecraft/lang/en_us.json'))
        for base in bases:
            family = base.removeprefix('oak_') if base.startswith('oak_') else base
            original = json.loads(archive.read(f'assets/minecraft/blockstates/{base}.json'))
            for color in COLORS:
                target = f'{color}_{family}'
                state = json.loads(json.dumps(original))
                for entry in state['variants'].values():
                    source_model = entry['model'].split(':', 1)[1]
                    model = json.loads(archive.read('assets/minecraft/models/' + source_model + '.json'))
                    textures = {}
                    for slot, texture in model.get('textures', {}).items():
                        if texture.startswith('minecraft:block/'):
                            block_textures[texture] = 'wood' if base not in ('iron_door','iron_trapdoor') and 'copper' not in base else 'all'
                            textures[slot] = 'coloredm:block/' + color + '_' + texture.rsplit('/', 1)[1]
                    # Parent contains the geometry from the player's active resource pack.
                    own_model = color + '_' + source_model.rsplit('/', 1)[1]
                    write(f'{ASSETS}/models/block/{own_model}.json', {'parent':'minecraft:' + source_model, 'textures':textures})
                    entry['model'] = 'coloredm:block/' + own_model
                write(f'{ASSETS}/blockstates/{target}.json', state)
                if base.endswith('_door'):
                    texture_base = base.removeprefix('waxed_')
                    texture = 'minecraft:item/' + texture_base
                    item_textures[texture] = 'wood' if 'copper' not in base and base != 'iron_door' else 'all'
                    write(f'{ASSETS}/models/item/{target}.json', {'parent':'minecraft:item/generated',
                          'textures':{'layer0':'coloredm:item/' + color + '_' + texture_base}})
                    item_model = 'coloredm:item/' + target
                else:
                    item_model = next(entry['model'] for key, entry in state['variants'].items()
                                      if 'facing=north' in key and 'half=bottom' in key and 'open=false' in key)
                generate_variant(color, family, item_model)
                recipes.append(f'coloredm:dye/{target}')
                for locale, translations in strings.items():
                    door = base.endswith('_door')
                    adjective = translations[f'block.coloredm.{color}_' + ('ladder' if door else 'chest')].rsplit(' ', 1)[0]
                    label = names['block.minecraft.' + base]
                    if locale != 'en_us':
                        material = base.removesuffix('_door').removesuffix('_trapdoor')
                        modifiers = []
                        for prefix, ru, uk in [('waxed_','вощёная','вощений'),('exposed_','слегка окисленная','злегка окиснений'),
                                                ('weathered_','окисленная','окиснений'),('oxidized_','полностью окисленная','повністю окиснений')]:
                            if material.startswith(prefix):
                                material = material.removeprefix(prefix)
                                modifiers.append(ru if locale == 'ru_ru' else uk)
                        description = ', '.join([materials[material][0 if locale == 'ru_ru' else 1], *modifiers])
                        label = ('дверь' if door else 'люк') if locale == 'ru_ru' else ('двері' if door else 'люк')
                        label += ' (' + description + ')'
                        if locale == 'uk_ua' and door:
                            adjective = UKRAINIAN_DOORS[COLORS.index(color)]
                    translations['block.coloredm.' + target] = adjective + ' ' + label
                for category in ('block', 'item'):
                    extend_tag(f'data/c/tags/{category}/dyed/{color}.json', ['coloredm:' + target])
            entries = ['coloredm:' + color + '_' + family for color in COLORS]
            extend_tag(f'{DATA}/tags/item/dyeable/{family}.json', ['minecraft:' + base, *entries])
            wooden = 'copper' not in base and not base.startswith('iron_')
            tag = ('wooden_' if wooden else '') + ('doors' if base.endswith('_door') else 'trapdoors')
            for category in ('block', 'item'):
                extend_tag(f'data/minecraft/tags/{category}/{tag}.json', entries)
            extend_tag('data/minecraft/tags/block/mineable/' + ('axe' if wooden else 'pickaxe') + '.json', entries)
    update_atlas('blocks', block_textures)
    update_atlas('items', item_textures)
    for locale, translations in strings.items():
        write(f'{ASSETS}/lang/{locale}.json', dict(sorted(translations.items())))


def main(vanilla):
    update_atlas("blocks", {
        "minecraft:block/oak_door_bottom": "wood",
        "minecraft:block/oak_door_top": "wood",
        "minecraft:block/oak_trapdoor": "wood",
        "minecraft:block/sand": "all",
    })
    update_atlas("items", {"minecraft:item/oak_door": "wood"})

    recipes = []
    for color in COLORS:
        models = {"door": door_models(color), "trapdoor": trapdoor_models(color), "sand": sand_models(color)}
        for family, model in models.items():
            generate_variant(color, family, model)
            recipes.append(f"coloredm:dye/{color}_{family}")
        for category in ("block", "item"):
            extend_tag(f"data/c/tags/{category}/dyed/{color}.json", [f"coloredm:{color}_{family}" for family in FAMILIES])

    entries = {family: [f"coloredm:{color}_{family}" for color in COLORS] for family in FAMILIES}
    for family, base_block in FAMILIES.items():
        extend_tag(f"{DATA}/tags/item/dyeable/{family}.json", [f"minecraft:{base_block}", *entries[family]])
    for category in ("block", "item"):
        extend_tag(f"data/minecraft/tags/{category}/wooden_doors.json", entries["door"])
        extend_tag(f"data/minecraft/tags/{category}/wooden_trapdoors.json", entries["trapdoor"])
        extend_tag(f"data/minecraft/tags/{category}/sand.json", entries["sand"])
    extend_tag("data/minecraft/tags/block/mineable/axe.json", entries["door"] + entries["trapdoor"])
    extend_tag("data/minecraft/tags/block/mineable/shovel.json", entries["sand"])
    # Exact ingredients prevent the vanilla glass recipe from matching dyed sand.
    path = RESOURCES / "data/minecraft/tags/item/smelts_to_glass.json"
    if path.exists():
        tag = read("data/minecraft/tags/item/smelts_to_glass.json")
        tag["values"] = [value for value in tag["values"] if value not in entries["sand"]]
        write("data/minecraft/tags/item/smelts_to_glass.json", tag)
    for color in COLORS:
        name = f"{color}_stained_glass_from_sand"
        write(f"{DATA}/recipe/smelting/{name}.json", {
            "type": "minecraft:smelting", "category": "blocks",
            "ingredient": f"coloredm:{color}_sand",
            "result": {"id": f"minecraft:{color}_stained_glass"},
            "experience": 0.1, "cookingtime": 200,
        })
        recipes.append(f"coloredm:smelting/{name}")

    update_translations()
    all_doors(vanilla, recipes)
    path = f"{DATA}/advancement/recipes/dyeing.json"
    advancement = read(path)
    extend(advancement["rewards"]["recipes"], recipes)
    write(path, advancement)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('minecraft_client_jar', type=Path)
    main(parser.parse_args().minecraft_client_jar)
