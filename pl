Spell
    ResourceLocation id - ид спелла
    String description - описание спелла
    int castTicks - сколько тиков кастуется
    int durationTicks - сколько тиков выполняется
    int cooldownTicks - сколько тиков перезарядка
    Component name - название спелла

    castTick - какой тип после начала каста
    serverCastingTick(ServerPlayer, castTick)
    clientCastingTick(LocalPlayer, castTIck)
    serverCastingEnd(ServerPlater)
    clientCastingEnd(LocalPlayer)

    casting(ServerPlayer)
    effect(LocalPlayer)

    serverTick(ServerPlayer, activeTick)
    clientTick(LocalPlayer, activeTick)
    serverEnd(ServerPlater)
    clientEnd(LocalPlayer)

    serverCancel(ServerPlayer)
    clientCancel(LocalPlayer)

client KeyPress -> send PacketCastSpell

PacketCastSpell:
    ResourceLocation id - айди спелла который нужно кастануть

1.

1. проверяем перезарядку
2. отправляем в серверный тикер