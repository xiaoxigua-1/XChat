package org.xiaoxigua.xchat.fabric

import io.netty.buffer.Unpooled
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import org.slf4j.LoggerFactory
import org.xiaoxigua.xchat.common.XChatEventType

class XChat : ModInitializer {
    private val logger = LoggerFactory.getLogger("XChat")

    override fun onInitialize() {
        logger.info("Initialize plugin")

        PayloadTypeRegistry.playC2S().register(XChatEvent.ID, XChatEvent.CODEC)
        PayloadTypeRegistry.playS2C().register(XChatEvent.ID, XChatEvent.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(XChatEvent.ID) { payload, context ->
            context.server().execute {
                val data = when (XChatEventType.getByValue(payload.type)) {
                    XChatEventType.CheckServerSupport ->
                        XChatEvent(payload.uuid, XChatEventType.CheckServerSupport)
                    XChatEventType.GetRegistry -> XChatEvent(payload.uuid, XChatEventType.GetRegistry)
                    XChatEventType.GetPlayerItemData -> {
                        val registryByteBuf = RegistryByteBuf(Unpooled.buffer(), context.server().registryManager)

                        ItemStack.OPTIONAL_PACKET_CODEC.encode(registryByteBuf, context.player().mainHandStack)

                        XChatEvent(payload.uuid, payload.type, registryByteBuf)
                    }
                }

                ServerPlayNetworking.send(context.player(), data)
            }

            logger.info(context.server().registryManager.get(RegistryKeys.ENCHANTMENT).ids.toString())
            logger.info(Registries.ITEM.ids.toString())
        }
    }
}

