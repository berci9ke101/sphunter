package hu.kszi2.sphunter

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text
import org.slf4j.LoggerFactory


object SPHunter : ModInitializer {
    private const val MOD_ID = "sphunter";
    private val logger = LoggerFactory.getLogger(MOD_ID)

    private lateinit var currentServerSession: ClientPlayNetworkHandler

    private fun loadCurrentServerSession() {
        currentServerSession = MinecraftClient.getInstance().networkHandler!!
    }

    private fun sendChatCommand(command: String) {
        currentServerSession.sendChatCommand(command)
    }

    private fun commandExecute() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess? ->
            dispatcher.register(ClientCommandManager.literal("hunt")
                    .executes { context: CommandContext<FabricClientCommandSource> ->
                        //Getting the actual server session
                        loadCurrentServerSession()

                        //Greeting the player and counting down
                        context.localMessage("Hunting will begin shortly!")
                        GlobalScope.launch {
                            Thread.sleep(1000)
                            context.localMessage("3..")
                            Thread.sleep(1000)
                            context.localMessage("2.")
                            Thread.sleep(1000)
                            context.localMessage("1!")
                            Thread.sleep(1000)
                        }

                        1//We need to return one according to the documentation
                    })
        })
    }

    override fun onInitialize() {
        logger.info("SPHunter is running!")

        //Registering the HUNT command
        commandExecute()
    }
}

fun CommandContext<FabricClientCommandSource>.localMessage(text: String) {
    this.source.sendFeedback(Text.literal(text))
}