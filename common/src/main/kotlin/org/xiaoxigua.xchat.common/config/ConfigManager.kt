package org.xiaoxigua.xchat.common.config

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createParentDirectories

class ConfigManager(configPath: Path) {
    private val configFile = File(configPath.absolutePathString())
    lateinit var config: Config
        private set

    init {
        if (!configFile.exists()) {
            val defaultConfig = createDefaultConfig()
            configPath.createParentDirectories()
            configFile.createNewFile()
            configFile.writeText(Toml.encodeToString(defaultConfig))

            config = defaultConfig
        } else {
            loadConfig()
        }
    }

    private fun createDefaultConfig(): Config {
        return Config(GlobalConfig("<color:#b617ff>[<server>]</color> <player>: <message>"))
    }

    fun loadConfig() {
        config = Toml.decodeFromString(configFile.readText())
    }
}