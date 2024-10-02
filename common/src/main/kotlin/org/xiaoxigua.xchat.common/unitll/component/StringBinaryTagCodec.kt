package org.xiaoxigua.xchat.common.unitll.component

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.StringBinaryTag
import org.xiaoxigua.xchat.common.unitll.Codecs

val StringBinaryTagCodec = object : Codecs<StringBinaryTag>() {
    override fun encode(buf: ByteBuf, value: StringBinaryTag) {
        StringCodec.encode(buf, value.value())
    }

    override fun decode(buf: ByteBuf) = StringBinaryTag.stringBinaryTag(StringCodec.decode(buf))
}
