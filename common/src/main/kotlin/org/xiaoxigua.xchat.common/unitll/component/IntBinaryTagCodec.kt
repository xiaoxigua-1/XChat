package org.xiaoxigua.xchat.common.unitll.component

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.IntBinaryTag
import org.xiaoxigua.xchat.common.unitll.Codecs

val IntBinaryTagCodec = object : Codecs<IntBinaryTag>() {
    override fun encode(buf: ByteBuf, value: IntBinaryTag) {
        VarIntCodec.encode(buf, value.value())
    }

    override fun decode(buf: ByteBuf) = IntBinaryTag.intBinaryTag(VarIntCodec.decode(buf))
}