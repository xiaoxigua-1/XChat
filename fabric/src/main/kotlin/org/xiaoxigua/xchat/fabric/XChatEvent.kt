package org.xiaoxigua.xchat.fabric

import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import org.xiaoxigua.xchat.common.XChatEventType
import org.xiaoxigua.xchat.common.unitll.Codecs
import java.util.*

data class XChatEvent(val uuid: UUID, val type: Byte, val data: ByteBuf? = null) : CustomPayload {
    companion object {
        private val identifier: Identifier = Identifier.of("xchat", "hi")
        private val UUID_PACKET = object : PacketCodec<ByteBuf, UUID> {
            override fun decode(buf: ByteBuf): UUID {
                return Codecs.UUIDCodec.decode(buf)
            }

            override fun encode(buf: ByteBuf, value: UUID) {
                return Codecs.UUIDCodec.encode(buf, value)
            }
        }
        private val DATA_PACKET = object : PacketCodec<RegistryByteBuf, ByteBuf?> {
            override fun decode(buf: RegistryByteBuf): ByteBuf? {
                return if (buf.readableBytes() != 0) buf
                else null
            }

            override fun encode(buf: RegistryByteBuf, value: ByteBuf?) {
                if (value != null) buf.writeBytes(value)
            }
        }
        val ID: CustomPayload.Id<XChatEvent> = CustomPayload.Id(identifier)

        val CODEC: PacketCodec<RegistryByteBuf, XChatEvent> =
            PacketCodec.tuple(UUID_PACKET, XChatEvent::uuid, PacketCodecs.BYTE, XChatEvent::type, DATA_PACKET, XChatEvent::data, ::XChatEvent)
    }

    constructor(uuid: UUID, type: XChatEventType) : this(uuid, type.ordinal.toByte())

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }
}