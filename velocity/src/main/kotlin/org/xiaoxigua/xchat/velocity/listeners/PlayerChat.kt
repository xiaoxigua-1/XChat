package org.xiaoxigua.xchat.velocity.listeners

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.slf4j.Logger
import org.xiaoxigua.xchat.common.MiniMessageBuilder
import org.xiaoxigua.xchat.common.XChatEventType
import org.xiaoxigua.xchat.common.config.ConfigManager
import org.xiaoxigua.xchat.common.unitll.Codec
import org.xiaoxigua.xchat.common.unitll.Codecs
import org.xiaoxigua.xchat.velocity.ServerManager
import java.util.*

class PlayerChat(
    private val logger: Logger,
    private val server: ProxyServer,
    private val configManager: ConfigManager
) {
    private val mm = MiniMessageBuilder().createMiniMessage()

    @Subscribe(order = PostOrder.FIRST)
    fun onPlayerChat(event: PlayerChatEvent) {
        event.result = PlayerChatEvent.ChatResult.denied()

        val connect = event.player.currentServer

        if (!connect.isPresent) return

        val serverName = connect.get().serverInfo.name
        val replaceList = listOf(
            Placeholder.component("server", Component.text(serverName)),
            Placeholder.component("player", Component.text(event.player.username)),
            Placeholder.component("message", mm.deserialize(event.message))
        )
        val parsed = mm.deserialize(configManager.config.global.messageFormat, *replaceList.toTypedArray())

        server.sendMessage(parsed)

        val uuid = UUID.randomUUID()
        val data = Codec()

        data.tupleEncode(Pair(Codecs.UUIDCodec, uuid), Pair(Codecs.EventTypeCodec, XChatEventType.GetPlayerItemData))

        event.player.currentServer.get().sendPluginMessage(ServerManager.IDENTIFIER, data.array())
    }
}