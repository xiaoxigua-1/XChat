package org.xiaoxigua.xchat.common

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.event.DataComponentValue
import net.kyori.examination.ExaminableProperty
import java.util.stream.Stream

abstract class NBTDataComponentValue: DataComponentValue {
    data class NBTInt(val element: Int) : NBTDataComponentValue() {
        override fun examinableProperties(): Stream<out ExaminableProperty> {
            return Stream.of(ExaminableProperty.of("element", element))
        }
    }
    data class NBTLong(val element: Long) : NBTDataComponentValue() {
        override fun examinableProperties(): Stream<out ExaminableProperty> {
            return Stream.of(ExaminableProperty.of("element", element))
        }
    }
    data class NBTShort(val element: Short) : NBTDataComponentValue() {
        override fun examinableProperties(): Stream<out ExaminableProperty> {
            return Stream.of(ExaminableProperty.of("element", element))
        }
    }
    data class NBTByte(val element: Byte) : NBTDataComponentValue() {
        override fun examinableProperties(): Stream<out ExaminableProperty> {
            return Stream.of(ExaminableProperty.of("element", element))
        }
    }

    data class NBTFloat(val element: Float) : NBTDataComponentValue() {
        override fun examinableProperties(): Stream<out ExaminableProperty> {
            return Stream.of(ExaminableProperty.of("element", element))
        }
    }
    data class NBTDouble(val element: Double) : NBTDataComponentValue() {
        override fun examinableProperties(): Stream<out ExaminableProperty> {
            return Stream.of(ExaminableProperty.of("element", element))
        }
    }

    data class NBTString(val element: String) : NBTDataComponentValue() {
        override fun examinableProperties(): Stream<out ExaminableProperty> {
            return Stream.of(ExaminableProperty.of("element", element))
        }

        override fun toString(): String {
            return element
        }
    }

    data class NBTComponent(val element: Map<Key, NBTDataComponentValue>) : NBTDataComponentValue() {
        override fun examinableProperties(): Stream<out ExaminableProperty> {
            return Stream.of(ExaminableProperty.of("element", element))
        }
    }

    data class NBTList(val element: List<NBTDataComponentValue>) : NBTDataComponentValue() {
        override fun examinableProperties(): Stream<out ExaminableProperty> {
            return Stream.of(ExaminableProperty.of("element", element))
        }
    }

}