package org.xiaoxigua.xchat.velocity.listeners

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientQueryEntityNBT
import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.slf4j.Logger
import org.xiaoxigua.xchat.common.MiniMessageBuilder
import org.xiaoxigua.xchat.common.config.ConfigManager

class PlayerChat(
    private val logger: Logger,
    private val server: ProxyServer,
    private val configManager: ConfigManager,
    private val queryEntityNBTListener: QueryEntityNBTListener
) {
    private val mm = MiniMessageBuilder().createMiniMessage()

    @Subscribe(order = PostOrder.FIRST)
    fun onPlayerChat(event: PlayerChatEvent) {
        val connect = event.player.currentServer

        if (!connect.isPresent) return
        event.result = ChatResult.denied()

        val serverName = connect.get().serverInfo.name
        val replaceList = listOf(
            Placeholder.component("server", Component.text(serverName)),
            Placeholder.component("player", Component.text(event.player.username)),
            Placeholder.component("message", mm.deserialize(event.message))
        )
        val parsed = mm.deserialize(configManager.config.global.messageFormat, *replaceList.toTypedArray())
        val entityId = PacketEvents.getAPI().playerManager.getUser(event.player).entityId

        PacketEvents.getAPI().playerManager.receivePacketSilently(event.player, WrapperPlayClientQueryEntityNBT(100, entityId))

        queryEntityNBTListener.addListeners(event.player.uniqueId, parsed)
    }
}