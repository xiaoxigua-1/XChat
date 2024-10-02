package org.xiaoxigua.xchat.common.unitll

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class Codec(private val buf: ByteBuf = Unpooled.buffer()) {
    constructor(byteArray: ByteArray) : this(Unpooled.wrappedBuffer(byteArray))

    fun <T> tupleEncode(vararg encodes: Pair<Codecs<T>, T>) {
        for (encode in encodes) {
            encode.first.encode(buf, encode.second)
        }
    }

    fun array(): ByteArray {
        val bytesArray = ByteArray(buf.readableBytes())

        buf.readBytes(bytesArray)

        return bytesArray
    }
}
