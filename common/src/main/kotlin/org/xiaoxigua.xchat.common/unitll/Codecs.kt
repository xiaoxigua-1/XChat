package org.xiaoxigua.xchat.common.unitll

import io.netty.buffer.ByteBuf
import org.xiaoxigua.xchat.common.XChatEventType
import java.util.*

abstract class Codecs<out T> {
    companion object {
        private const val SEGMENT_BITS: Int = 0x7f
        private const val CONTINUE_BIT: Int = 0x80
    }

    abstract fun encode(buf: ByteBuf, value: @UnsafeVariance T)
    abstract fun decode(buf: ByteBuf): T

    data object VarIntCodec : Codecs<Int>() {
        override fun encode(buf: ByteBuf, value: Int) {
            var tempValue = value

            while (true) {
                val part = tempValue and SEGMENT_BITS
                tempValue = tempValue shr 7

                if (tempValue != 0) {
                    buf.writeByte(part or CONTINUE_BIT)
                } else {
                    buf.writeByte(part)
                    break
                }
            }
        }

        override fun decode(buf: ByteBuf): Int {
            var value = 0
            var position = 0
            while (true) {
                val byte = buf.readByte().toInt() // Read the next byte
                // Combine the 7 bits with the previous value
                value = value or ((byte and SEGMENT_BITS) shl position)
                position += 7
                // If the continuation bit is not set, break
                if (byte and CONTINUE_BIT == 0) break
            }
            return value
        }
    }

    data object VarLongCodec : Codecs<Long>() {
        override fun encode(buf: ByteBuf, value: Long) {
            var tempValue = value

            while (true) {
                if ((tempValue and SEGMENT_BITS.inv().toLong()) == 0L) {
                    buf.writeByte(tempValue.toInt())
                    return
                }

                buf.writeByte((tempValue and SEGMENT_BITS.toLong() or CONTINUE_BIT.toLong()).toInt())

                // Unsigned right shift by 7 bits (>>>)
                tempValue = tempValue ushr 7
            }
        }

        override fun decode(buf: ByteBuf): Long {
            var value: Long = 0
            var position = 0
            var currentByte: Int

            while (true) {
                // Read the next byte
                currentByte = buf.readByte().toInt()

                // Combine the 7 bits of the current byte with the previous value
                value = value or ((currentByte and SEGMENT_BITS).toLong() shl position)

                // If the continuation bit (bit 7) is not set, the VarLong is complete
                if (currentByte and CONTINUE_BIT == 0) {
                    break
                }

                // Increment the position by 7 bits for the next iteration
                position += 7

                // VarLongs are at most 10 bytes long (70 bits)
                if (position >= 64) {
                    throw RuntimeException("VarLong is too big")
                }
            }

            return value
        }
    }

    data object ByteCodec : Codecs<Byte>() {
        override fun encode(buf: ByteBuf, value: Byte) {
            buf.writeByte(value.toInt())
        }

        override fun decode(buf: ByteBuf): Byte = buf.readByte()
    }

    data object StringCodec : Codecs<String>() {
        override fun encode(buf: ByteBuf, value: String) {
            val strByteArray = value.toByteArray()

            VarIntCodec.encode(buf, strByteArray.size)
            buf.writeBytes(strByteArray)
        }

        override fun decode(buf: ByteBuf): String {
            val strSize = VarIntCodec.decode(buf)
            val stringBytes = ByteArray(strSize)

            buf.readBytes(stringBytes)

            return stringBytes.decodeToString()
        }
    }

    data object UUIDCodec : Codecs<UUID>() {
        override fun encode(buf: ByteBuf, value: UUID) {
            VarLongCodec.encode(buf, value.mostSignificantBits)
            VarLongCodec.encode(buf, value.leastSignificantBits)
        }

        override fun decode(buf: ByteBuf): UUID {
            val mostSignificantBits = VarLongCodec.decode(buf)
            val leastSignificantBits = VarLongCodec.decode(buf)

            return UUID(mostSignificantBits, leastSignificantBits)
        }
    }

    data object EventTypeCodec : Codecs<XChatEventType>() {
        override fun encode(buf: ByteBuf, value: XChatEventType) {
            buf.writeByte(value.ordinal)
        }

        override fun decode(buf: ByteBuf): XChatEventType {
            return XChatEventType.getByValue(buf.readByte())
        }
    }
}