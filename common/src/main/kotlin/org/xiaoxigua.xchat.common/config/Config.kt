package org.xiaoxigua.xchat.common.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val global: GlobalConfig

)

@Serializable
data class GlobalConfig(val messageFormat: String)