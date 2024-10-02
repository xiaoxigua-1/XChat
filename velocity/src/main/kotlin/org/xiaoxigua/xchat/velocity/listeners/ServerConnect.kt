package org.xiaoxigua.xchat.velocity.listeners

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.PluginMessageEvent
import com.velocitypowered.api.event.player.ServerPostConnectEvent
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.ServerConnection
import io.netty.buffer.Unpooled
import org.slf4j.Logger
import org.xiaoxigua.xchat.common.unitll.Codec
import org.xiaoxigua.xchat.common.XChatEventType
import org.xiaoxigua.xchat.common.unitll.Codecs
import org.xiaoxigua.xchat.common.unitll.component.ComponentType
import org.xiaoxigua.xchat.velocity.ServerManager
import java.util.*

class ServerConnect(
    private val logger: Logger,
    private val server: ProxyServer,
    private val serverManager: ServerManager
) {

    @Subscribe
    fun onPlayerConnect(event: ServerPostConnectEvent) {
        val uuid = UUID.randomUUID()
        val data = Codec()

        data.tupleEncode(Pair(Codecs.UUIDCodec, uuid), Pair(Codecs.EventTypeCodec, XChatEventType.CheckServerSupport))

        event.player.currentServer.get().sendPluginMessage(ServerManager.IDENTIFIER, data.array())
    }

    @Subscribe
    fun onPluginMessageFromBackend(event: PluginMessageEvent) {
        if (event.source !is ServerConnection && event.identifier != ServerManager.IDENTIFIER) return

        val backend = event.source as ServerConnection
        val buf = Unpooled.wrappedBuffer(event.data)
        val uuid = Codecs.UUIDCodec.decode(buf)
        val eventType = Codecs.EventTypeCodec.decode(buf)

        logger.info("$uuid: ${eventType.name}")

        when (eventType) {
            XChatEventType.CheckServerSupport -> serverManager.addServer(backend.serverInfo)
            XChatEventType.GetRegistry -> {}
            XChatEventType.GetPlayerItemData -> {
                val count = Codecs.ByteCodec.decode(buf)
                val id = Codecs.VarIntCodec.decode(buf)
                val componentsAddSize = Codecs.VarIntCodec.decode(buf)
                val componentsRemoveSize = Codecs.VarIntCodec.decode(buf)

                logger.info("count: $count id: $id tags: $componentsAddSize")
                for (index in 0..componentsAddSize) {
                    val decode = ComponentType.ComponentCodec(buf)

                    logger.info(decode.componentType.getTypeString())
                    logger.info(decode.value().toString())
                }
            }
        }

        event.result = PluginMessageEvent.ForwardResult.handled()
    }
}