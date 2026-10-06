# Frozen Bees: промпты для текстур

## Как пользоваться
- Нейросети плохо рисуют ровно 16x16. Генерируй в большом размере (512x512 или 1024x1024), потом уменьши до 16x16 **методом Nearest Neighbor** (без сглаживания). Это можно сделать в Photoshop, GIMP, Aseprite или на сайте pixelcdn / ezgif (Resize → Nearest).
- Для всех предметов и цветов нужен **прозрачный фон** (PNG). Если генератор рисует фон, вырежь его в редакторе.
- Для блоков нужна **бесшовная** текстура без теней по краям: добавь в промпт "seamless tile, flat lighting".
- Общий стиль всех промптов: `Minecraft pixel art texture, 16x16, limited palette (8-12 colors), flat lighting, hard pixel edges, no anti-aliasing, no text, no watermark`.
- Палитра мода (держись её, чтобы всё смотрелось одним набором): ледяной голубой #A8DCF5, снежно-белый #F2F8FF, глубокий синий #4A86C5, тёмный контур #2B4A73, холодный мёд (светло-голубой) #8FD3FA, тёплый акцент (янтарь внутри мёда) #FFC857.

---

## 1. Предметы (16x16, прозрачный фон)

### item/frozen_honeycomb.png: замороженные соты
```
Minecraft pixel art item icon, 16x16, a single honeycomb chunk made of icy blue wax, hexagonal cells, frosted with white snow crystals on top edge, small icicles hanging from the bottom, pale blue (#A8DCF5) with deeper blue (#4A86C5) cell shading and dark navy outline (#2B4A73), transparent background, centered, hard pixel edges, no anti-aliasing
```
Основа: ванильный предмет honeycomb, перекрашенный в голубой.

### item/cold_honey_bottle.png: бутылочка холодного мёда
```
Minecraft pixel art item icon, 16x16, a glass bottle (like the vanilla honey bottle) filled with glowing pale-blue frozen honey, a few tiny frost crystals on the cork, subtle white highlight on the glass, thick liquid with light cyan (#8FD3FA) and deep blue (#4A86C5) shading, dark navy outline, transparent background, hard pixel edges, no anti-aliasing
```

### item/frozen_arrow.png: морозная стрела
```
Minecraft pixel art item icon, 16x16, a diagonal arrow pointing to the upper right, arrowhead made of ice-blue crystal with a white frost glint, shaft light grey wood, feathers white-blue like snow, tiny snowflake sparkle near the tip, matches vanilla arrow proportions and diagonal angle, dark navy outline, transparent background, hard pixel edges
```
Лучше всего взять ванильную стрелу и перекрасить наконечник и оперение.

### item/frozen_bee_spawn_egg: **не нужна**
Яйцо рисуется автоматически из цветов в коде.

---

## 2. Цветы (16x16, прозрачный фон, крестообразная модель)

### block/snowdrop.png: подснежник
```
Minecraft pixel art plant texture, 16x16, a snowdrop flower: one thin green stem with two slim leaves at the base, a single drooping white bell-shaped blossom at the top with a tiny pale green mark on the petal tips, looks like vanilla lily of the valley in size and style, soft white (#F2F8FF) petals, cool green (#5FA36A) stem, dark outline, transparent background, hard pixel edges, no anti-aliasing
```

### block/frostbloom.png: ледоцвет
```
Minecraft pixel art plant texture, 16x16, a frostbloom flower growing from ice: a crystalline stem and leaves that look like frozen glass shards, one open star-shaped flower with icy light blue petals (#A8DCF5) and a bright white core, tiny sparkles, deeper blue shading (#4A86C5), dark navy outline, similar size to vanilla cornflower, transparent background, hard pixel edges, no anti-aliasing
```

---

## 3. Блоки (16x16, непрозрачные, бесшовные)

### block/frozen_honeycomb_block.png: блок замороженных сот
```
Minecraft pixel art block texture, 16x16, seamless tile, honeycomb block made of icy wax, hexagonal cell pattern in pale blue (#A8DCF5) with deeper blue (#4A86C5) cell walls, frost speckles and tiny ice crystals, same layout as the vanilla honeycomb block but frozen and cold, flat lighting, no outline shadows at the edges, hard pixel edges
```

### block/cold_honey_block.png: блок холодного мёда
```
Minecraft pixel art block texture, 16x16, semi-translucent frozen honey block like the vanilla honey block, light cyan (#8FD3FA) glowing liquid with a slightly darker blue border frame (#4A86C5), small white frost bubbles and one or two amber (#FFC857) specks suspended inside, glossy highlight in one corner, flat lighting, hard pixel edges, no anti-aliasing
```
Текстура может быть полупрозрачной, как у ванильного блока мёда.

### block/cooler.png: охладитель
```
Minecraft pixel art block texture, 16x16, a cooling machine block: dark blue metal-ice frame (#2B4A73) with a glowing pale blue (#A8DCF5) frosty core in the center, small snowflake shape on the face, thin vent lines on the sides, frost crystals on the corners, white highlights, flat lighting, hard pixel edges, no anti-aliasing
```
Один и тот же рисунок на всех шести гранях.

---

## 4. Гнездо и улей (по 5 текстур на каждый, 16x16)
Всего 10 файлов. Рисуй по раскладке ванильных `bee_nest_*` и `beehive_*`, это самый простой путь: открой ванильные текстуры, перекрась в ледяную палитру, добавь иней.

### Морозное гнездо (frozen_bee_nest): природный вид
Базовый промпт (меняй последнюю строку для каждой грани):
```
Minecraft pixel art block texture, 16x16, a frozen wild bee nest, rough papery grey-blue material coated with snow and frost, icicles hanging from the lower edge, natural and uneven look like the vanilla bee nest but cold, palette: dusty ice blue (#8FB5D6), snow white (#F2F8FF), deep blue (#4A86C5), dark navy (#2B4A73), flat lighting, hard pixel edges, no anti-aliasing
```
Добавь в конец:
- `frozen_bee_nest_top.png`: `Top face: flat top with a rough snowy crust and tiny ice crystals`
- `frozen_bee_nest_bottom.png`: `Bottom face: rough underside, dark blue-grey, a few short icicles`
- `frozen_bee_nest_side.png`: `Side face: layered papery bands, snow on the upper third, vertical icicles at the bottom`
- `frozen_bee_nest_front.png`: `Front face: same as side but with a round dark entrance hole in the center-lower area`
- `frozen_bee_nest_front_honey.png`: `Front face: same as the front with the entrance hole, but pale blue frozen honey (#8FD3FA) with amber specks dripping out of the hole and frozen into short drips`

### Морозный улей (frozen_beehive): рукотворный
```
Minecraft pixel art block texture, 16x16, a frozen beehive made of wooden planks coated with frost, same layout as the vanilla beehive with horizontal plank stripes, frozen blue-white wood (#BBD7EE and #7FB0D4), snow dusting on the edges, small icicles, dark navy outline lines between planks (#2B4A73), flat lighting, hard pixel edges, no anti-aliasing
```
Добавь в конец:
- `frozen_beehive_top.png`: `Top face: planks seen from above with snow patches and a small tile border`
- `frozen_beehive_bottom.png`: `Bottom face: planks, slightly darker, no snow`
- `frozen_beehive_side.png`: `Side face: horizontal planks, thin frost along the top edge`
- `frozen_beehive_front.png`: `Front face: planks with a dark rectangular entrance slot in the middle and a small landing ledge`
- `frozen_beehive_front_honey.png`: `Front face: same as front but frozen light blue honey (#8FD3FA) with amber specks dripping from the slot and frozen into short drips`

---

## 5. Пчела (64x64, самая сложная часть)
Лучше **не генерировать с нуля**: нейросети не знают раскладку модели. Правильный путь:
1. Возьми ванильную `bee.png` из Minecraft (в jar игры: `assets/minecraft/textures/entity/bee/bee.png`, размер 64x64).
2. Открой в редакторе (Aseprite, Photoshop, GIMP, Blockbench → Paint).
3. Перекрась по палитре ниже. Если хочешь сделать это нейросетью, используй функцию **image-to-image / редактирование изображения** с этим промптом:
```
Recolor this Minecraft bee texture sheet (64x64 UV layout) into a frozen bee: replace the yellow body stripes with pale icy blue (#A8DCF5) and white (#F2F8FF), replace the black stripes with deep navy blue (#2B4A73), wings translucent white-blue with frost veins, add tiny frost crystals on the back and head, antennae with frosty tips, eyes keep dark. Keep the exact same layout, pixel positions, size, and style. Hard pixel edges, no anti-aliasing, no new shapes outside the original UV islands.
```
Нужны 4 файла (папка `textures/entity/frozen_bee/`):
- `frozen_bee.png`: обычная
- `frozen_bee_angry.png`: злая: глаза красные, полосы темнее, добавь красный оттенок в тёмные части (как у ванильной angry-версии)
- `frozen_bee_nectar.png`: с нектаром: на спине и брюшке светящееся пятно холодного мёда (#8FD3FA, лёгкий голубой блеск, как у ванильной с пыльцой)
- `frozen_bee_angry_nectar.png`: комбинация злой и с нектаром

---

## 6. Иконка мода
### assets/frozenbees/icon.png (128x128)
```
Minecraft style mod icon, 128x128, a cute frozen bee with an icy-blue striped body and translucent frosty wings hovering over a snowy honeycomb, soft cool background gradient (light blue to white), small snowflakes around, clean pixel art, readable at small size, no text
```

---

## Сводка файлов
| Тип | Файл | Размер |
|---|---|---|
| item | frozen_honeycomb, cold_honey_bottle, frozen_arrow | 16x16 |
| block | snowdrop, frostbloom, frozen_honeycomb_block, cold_honey_block, cooler | 16x16 |
| block | frozen_bee_nest_{top,bottom,side,front,front_honey} | 16x16 |
| block | frozen_beehive_{top,bottom,side,front,front_honey} | 16x16 |
| entity | frozen_bee, frozen_bee_angry, frozen_bee_nectar, frozen_bee_angry_nectar | 64x64 |
| icon | icon.png | 128x128 |

Путь: `src/main/resources/assets/frozenbees/textures/` + `item/`, `block/`, `entity/frozen_bee/`. Просто замени файлы с теми же именами.
