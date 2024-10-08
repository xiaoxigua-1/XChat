package org.xiaoxigua.xchat.fabric

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

class XChat : ModInitializer {
    private val logger = LoggerFactory.getLogger("XChat")

    override fun onInitialize() {
        logger.info("Initialize plugin")
    }
}

