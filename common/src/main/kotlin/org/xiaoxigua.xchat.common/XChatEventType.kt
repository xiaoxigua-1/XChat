package org.xiaoxigua.xchat.common

enum class XChatEventType {
    CheckServerSupport,
    GetRegistry,
    GetPlayerItemData;

    companion object {
        fun getByValue(value: Byte) = entries[value.toInt()]
    }
}