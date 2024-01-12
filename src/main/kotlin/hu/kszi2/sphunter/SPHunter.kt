package hu.kszi2.sphunter

import com.mojang.authlib.GameProfile
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import hu.kszi2.sphunter.exception.ServerNotFoundException
import hu.kszi2.sphunter.exception.WorldNotFoundException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import org.slf4j.LoggerFactory


object SPHunter : ModInitializer {
    private const val MOD_ID = "sphunter";
    private val logger = LoggerFactory.getLogger(MOD_ID)

    private lateinit var currentServerSession: ClientPlayNetworkHandler

    override fun onInitialize() {
        logger.info("SPHunter is running!")

        //Registering the HUNT command
        registerCommand()

        //Greeting player
        //FIXME: nem tudom mi van XD
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            val player: ServerPlayerEntity = handler.getPlayer()
            player.sendMessage(
                Text.literal("\nHi!\n").formatted(Formatting.BLUE)
            )
        }
    }

    private fun loadCurrentServerSession() {
        try {
            currentServerSession = MinecraftClient.getInstance().networkHandler!!
            //checking whether the client is connected to a server
        } catch (_: Exception) {
            throw ServerNotFoundException("Could not find server!")
        }
        try {
            //checking whether the client is connected to the wynncraft network
            if (!currentServerSession.serverInfo?.address!!.contains("wynncraft")) {
                throw ServerNotFoundException()
            }
        } catch (_: Exception) {
            throw ServerNotFoundException("You are not connected to the wynncraft network!")
        }
    }

    private fun executeCommand(command: String) {
        try {
            currentServerSession.sendChatCommand(command)
        } catch (_: Exception) {
            throw ServerNotFoundException("You are not connected to any server!")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun registerCommand() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess? ->
            dispatcher.register(ClientCommandManager.literal("hunt")
                .executes { context: CommandContext<FabricClientCommandSource> ->
                    //Loading server session
                    loadCurrentServerSession()

                    //Greeting the player and counting down and begin hunting
                    GlobalScope.launch {
                        context.apply {
                            countDown()
                            hunt()
                        }
                    }

                    1//We need to return one according to the documentation
                })
        })
    }
}

private fun getSecondsUntilSoulPoint(): Int {
    return try {
        (24000 - (MinecraftClient.getInstance().world!!.timeOfDay.toInt() % 24000)) / 20
    } catch (_: Exception) {
        throw WorldNotFoundException("Could not find wynncraft world!")
    }
}

private fun CommandContext<FabricClientCommandSource>.hunt() {
    //Get the current seconds in the world until the soulpoint
    val scnds = getSecondsUntilSoulPoint()
    this.localWarning(String.format("\nSeconds until next soulpoint is %02d:%02d", scnds / 60, scnds % 60))

    //TODO: maga a logika
}

private fun CommandContext<FabricClientCommandSource>.localMessage(text: String) {
    this.source.sendFeedback(Text.literal(text).formatted(Formatting.GREEN))
}

private fun CommandContext<FabricClientCommandSource>.localMessage(text: MutableText) {
    this.source.sendFeedback(text)
}

private fun CommandContext<FabricClientCommandSource>.localWarning(text: String) {
    this.source.sendFeedback(Text.literal(text).formatted(Formatting.GOLD).formatted(Formatting.BOLD))
}

private fun CommandContext<FabricClientCommandSource>.countDown() {
    this.localMessage("Hunting will begin shortly!")
    Thread.sleep(1000)
    this.localMessage("3..")
    Thread.sleep(1000)
    this.localMessage("2.")
    Thread.sleep(1000)
    this.localMessage("1!")
    Thread.sleep(1000)
}