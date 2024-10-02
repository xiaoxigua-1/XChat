package org.xiaoxigua.xchat.common

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEvent.ShowItem
import net.kyori.adventure.text.minimessage.MiniMessage

class MiniMessageBuilder {
    fun createMiniMessage(): MiniMessage {
        return MiniMessage.builder().build()
    }

    fun createShowItemComponent(name: String, item: ShowItem): Component {
        return Component.text("[$name]").hoverEvent(HoverEvent.showItem(item))
    }
}
