package org.xiaoxigua.xchat.velocity;

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import org.xiaoxigua.xchat.common.config.ConfigManager
import org.xiaoxigua.xchat.velocity.listeners.PlayerChat
import org.xiaoxigua.xchat.velocity.listeners.ServerConnect
import java.nio.file.Path

@Plugin(
    id = "xchat", name = "XChat", version = BuildConstants.VERSION,
    dependencies = [
        Dependency(id = "signedvelocity")
    ]
)
class XChat @Inject constructor(private val server: ProxyServer, private val logger: Logger, @DataDirectory val dataDirectory: Path) {

    private val configManager = ConfigManager(dataDirectory.resolve("config.toml"))
    private val serverManager = ServerManager(server, logger)


    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        logger.info("Initialization XChat plugin")

        server.channelRegistrar.register(ServerManager.IDENTIFIER)
        server.eventManager.register(this, PlayerChat(logger, server, configManager))
        server.eventManager.register(this, ServerConnect(logger, server, serverManager))
    }
}
