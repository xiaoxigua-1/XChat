package org.xiaoxigua.xchat.velocity.listeners

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.nbt.*
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.util.adventure.AdventureNBTSerializer
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerNBTQueryResponse
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.scheduler.ScheduledTask
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEvent.ShowItem
import org.xiaoxigua.xchat.velocity.XChat
import java.util.UUID
import java.util.concurrent.TimeUnit

class QueryEntityNBTListener(private val plugin: XChat, private val server: ProxyServer) : PacketListener {
    data class Listener(val task: ScheduledTask, val component: Component)

    private val listeners = mutableMapOf<UUID, Listener>()

    override fun onPacketSend(event: PacketSendEvent?) {
        when (event?.packetType) {
            PacketType.Play.Server.NBT_QUERY_RESPONSE -> {
                val nbtQueryResponse = WrapperPlayServerNBTQueryResponse(event)
                val uuid = event.user.uuid
                val item = nbtQueryResponse.tag.getCompoundListTagOrNull("Inventory")!!.tags[0]
                val contents = NBTCompound()
                val hoverEvent = NBTCompound()

                item.removeTag("Slot")
                contents.setTag("action", NBTString(HoverEvent.Action.SHOW_ITEM.toString()))
                contents.setTag("contents", item)
                hoverEvent.setTag("hoverEvent", contents)

                println(item)

                val style = AdventureNBTSerializer(false).deserializeStyle(hoverEvent)
                val component = Component.text("Hello", style)

                println((style.hoverEvent()?.value() as ShowItem).dataComponents()[Key.key("custom_name")])

                server.sendMessage(component)
//                server.sendMessage(listeners[uuid]?.component ?: Component.text())
                listeners[uuid]?.task?.cancel()
                listeners.remove(uuid)
            }
        }
    }

    fun addListeners(uuid: UUID, component: Component) {
        val task = server.scheduler.buildTask(plugin) { ->
            // Timeout
            listeners.remove(uuid)
        }.delay(1L, TimeUnit.SECONDS).schedule()

        listeners[uuid] = Listener(task, component)
    }
}