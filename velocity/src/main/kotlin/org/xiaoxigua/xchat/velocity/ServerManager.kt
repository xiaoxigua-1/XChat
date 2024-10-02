package org.xiaoxigua.xchat.velocity

import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import com.velocitypowered.api.proxy.server.ServerInfo
import org.slf4j.Logger

class ServerManager(private val server: ProxyServer, private val logger: Logger) {
    companion object {
        val IDENTIFIER: MinecraftChannelIdentifier = MinecraftChannelIdentifier.create("xchat", "hi")
    }

    private val servers = mutableListOf<String>()

    fun addServer(server: ServerInfo) {
        servers.add(server.name)
    }
}