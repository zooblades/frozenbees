# Текстуры (PNG, 16x16, прозрачность разрешена)
Сейчас лежат плейсхолдеры: заменяй файлы с теми же именами.

assets/frozenbees/textures/block/
- snowdrop.png, frostbloom.png (крестообразные цветы, фон прозрачный)
- frozen_honeycomb_block.png, cold_honey_block.png (второй может быть полупрозрачным)
assets/frozenbees/textures/item/
- frozen_honeycomb.png, cold_honey_bottle.png
assets/frozenbees/icon.png: иконка мода (лучше 128x128)

Этап 2 (пчела/гнездо/улей) добавит:
- entity/frozen_bee/frozen_bee.png, frozen_bee_angry.png, frozen_bee_nectar.png, frozen_bee_angry_nectar.png (64x64, как у ванильной пчелы)
- block/frozen_bee_nest_{top,bottom,side,front,front_honey}.png и то же для frozen_beehive (16x16)

## Этап 2 (добавлено, тоже плейсхолдеры)
textures/entity/frozen_bee/ (64x64, раскладка как у ванильной пчелы textures/entity/bee/bee.png)
- frozen_bee.png, frozen_bee_angry.png, frozen_bee_nectar.png, frozen_bee_angry_nectar.png
textures/block/ (16x16): для frozen_bee_nest и frozen_beehive:
- *_top, *_bottom, *_side, *_front, *_front_honey
Яйцо призыва использует цвета из кода (0xA8DCF5 и 0xFFFFFF), отдельной текстуры не нужно.
