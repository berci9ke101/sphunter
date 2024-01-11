package hu.kszi2.sphunter

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.entity.vehicle.MinecartEntity
import net.minecraft.network.listener.ClientPacketListener
import net.minecraft.server.MinecraftServer
import net.minecraft.server.integrated.IntegratedServer
import net.minecraft.text.Text
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread


object SPHunter : ModInitializer {
    private const val MOD_ID = "sphunter";
    private val logger = LoggerFactory.getLogger(MOD_ID)

    private lateinit var playerEntity: ClientPlayerEntity
    private lateinit var serverInstance: IntegratedServer

    private fun storeSessionInformation(){
        getPlayerEntity()
        getServerInstance()
    }

    private fun getPlayerEntity() {
        try {
            playerEntity = MinecraftClient.getInstance().player!!
        } catch (_: Exception) {
            throw RuntimeException("Can't get player!")
        }
    }

    private fun getServerInstance() {
        try {
            serverInstance = MinecraftClient.getInstance().server!!
        } catch (_: Exception) {
            throw RuntimeException("Can't get server!")
        }
    }

    override fun onInitialize() {
        logger.info("SPHunter is online!")

        //Registering the HUNT command
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess? ->
            dispatcher.register(ClientCommandManager.literal("hunt")
                    .executes { context: CommandContext<FabricClientCommandSource> ->
                        context.source.sendFeedback(Text.literal("Hunting will begin shortly!"))

                        //Getting the reference for the player and the server
                        storeSessionInformation()

                        //Doing cheeky stuffs
                        serverInstance.commandManager.executeWithPrefix(serverInstance.commandSource.withEntity(playerEntity), "/class")
                        1
                    })
        })
    }
}