package org.xiaoxigua.xchat.velocity.listeners

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.nbt.*
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerNBTQueryResponse
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.scheduler.ScheduledTask
import net.kyori.adventure.internal.Internals
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.xiaoxigua.xchat.common.NBTDataComponentValue
import org.xiaoxigua.xchat.velocity.XChat
import java.util.UUID
import java.util.concurrent.TimeUnit

class QueryEntityNBTListener(private val plugin: XChat, private val server: ProxyServer) : PacketListener {
    data class Listener(val task: ScheduledTask, val component: Component)

    private val listeners = mutableMapOf<UUID, Listener>()
    private object NBTDecode : NBTDataComponentValue() {
        fun <T: NBT> decode(data: T): NBTDataComponentValue {
            return when (data) {
                is com.github.retrooper.packetevents.protocol.nbt.NBTList<*> -> {
                    NBTList(data.tags.map { decode(it) })
                }
                is NBTCompound -> {
                    NBTComponent(data.tags.map {(key, value) ->
                        Key.key(key.lowercase()) to decode(value)
                    }.toMap())
                }
                is com.github.retrooper.packetevents.protocol.nbt.NBTInt -> NBTInt(data.asInt)
                is com.github.retrooper.packetevents.protocol.nbt.NBTByte -> NBTByte(data.asByte)
                is com.github.retrooper.packetevents.protocol.nbt.NBTLong -> NBTLong(data.asLong)
                is com.github.retrooper.packetevents.protocol.nbt.NBTShort -> NBTShort(data.asShort)
                is com.github.retrooper.packetevents.protocol.nbt.NBTFloat -> NBTFloat(data.asFloat)
                is com.github.retrooper.packetevents.protocol.nbt.NBTDouble -> NBTDouble(data.asDouble)
                is com.github.retrooper.packetevents.protocol.nbt.NBTString -> NBTString(data.value)
                else -> throw Error("")
            }
        }
    }

    override fun onPacketSend(event: PacketSendEvent?) {
        when (event?.packetType) {
            PacketType.Play.Server.NBT_QUERY_RESPONSE -> {
                val nbtQueryResponse = WrapperPlayServerNBTQueryResponse(event)
                val uuid = event.user.uuid

                val componentValue = NBTDecode.decode(nbtQueryResponse.tag.getCompoundListTagOrNull("Inventory")!!)
                println(componentValue)
                server.sendMessage(listeners[uuid]?.component ?: Component.text())
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