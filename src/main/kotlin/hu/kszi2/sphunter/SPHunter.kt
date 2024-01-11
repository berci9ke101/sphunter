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

    override fun onInitialize() {
        logger.info("SPHunter is running!")

        //Registering the HUNT command
        commandExecute()
    }

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

private fun CommandContext<FabricClientCommandSource>.hunt() {
    TODO("Not yet implemented!")
}

private fun CommandContext<FabricClientCommandSource>.localMessage(text: String) {
    this.source.sendFeedback(Text.literal(text))
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