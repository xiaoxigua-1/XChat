package org.xiaoxigua.velocity.xchat;

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import java.nio.file.Path

@Plugin(
    id = "xchat", name = "XChat", version = BuildConstants.VERSION
)
class XChat @Inject constructor(private val server: ProxyServer, private val logger: Logger, @DataDirectory val dataDirectory: Path) {

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        logger.info("Initialization XChat plugin")
    }
}
