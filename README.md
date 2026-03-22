# Голосовое управление для Unitree Robotics

Структура данных:
```kotlin
package com.hotelka.voicerobot.domain.model
sealed interface RobotCommand // Команды для работы – парсить при распозновании команды голосом
```

```kotlin
package com.hotelka.voicerobot.domain.model
data class HighCmd // Примерный набросок UDP-пакета, который ожидается робот на высоком уровне
```

```kotlin
package com.hotelka.voicerobot.data.mapper
class HighCmdMapper : RobotCommand.CommandMapper<HighCmd> // Маппинг команды: при команде "Сидеть!" – сформировать такой-то UDP-пакет
```

```kotlin
package com.hotelka.voicerobot.data.remote.packer
object HighCmdPacker // Упаковщик UDP-пакета. Превращает HighCmd в данные ByteArray
```

```kotlin
package com.hotelka.voicerobot.domain.model
data class HighState // Состояние робота – ответ после приёма UDP-пакета
```

```kotlin
package com.hotelka.voicerobot.data.dto
data class HighStateMock // Mock – ответ состояния робота от тестого окружения. Исключительно для работы в тестовом окружении. Ожидает JSON-версию HighState
```

```kotlin
package com.hotelka.voicerobot.data.mapper
class HighStateMockMapper // Маппер HighStateMock в HighState
```

Задачки:
1. В RobotCommand. Как интерпретировать параметры передвижения?
2. HighCmd. Логика вычисления crc. Как реализовать?
