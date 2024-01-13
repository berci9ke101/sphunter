package hu.kszi2.sphunter

import com.mojang.brigadier.CommandDispatcher
import hu.kszi2.sphunter.commands.helpCommand
import hu.kszi2.sphunter.commands.regentimeCommand
import hu.kszi2.sphunter.textformat.TF
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.slf4j.LoggerFactory


object SPHunter : ModInitializer {
    private const val MOD_ID = "sphunter";
    private val logger = LoggerFactory.getLogger(MOD_ID)

    internal lateinit var currentServerSession: ClientPlayNetworkHandler

    override fun onInitialize() {
        logger.info("SPHunter is running!")

        //Registering the HUNT command
        registerCommand()

        //Greeting player
        greetPlayer()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun greetPlayer() {
        ClientPlayConnectionEvents.JOIN.register { _, _, client ->
            GlobalScope.launch {
                Thread.sleep(6000)
                client.inGameHud.chatHud.addMessage(
                    Text.literal("\nThank you for using SPHunter!\n").formatted(Formatting.BLUE)
                )
            }
        }
    }

    private fun registerCommand() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, _: CommandRegistryAccess? ->
            dispatcher.register(
                ClientCommandManager.literal("sphunter")
                    .executes { context ->
                        context.source.sendFeedback(
                            TF.TFComment("For help, use: ")
                                .append(TF.TFCommand("/sphunter help"))
                        )
                        1
                    }
                    .helpCommand()
                    .regentimeCommand()
            )
        })
    }
}