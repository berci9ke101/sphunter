package hu.kszi2.sphunter

import hu.kszi2.sphunter.commands.*
import hu.kszi2.sphunter.core.WorldQueue
import hu.kszi2.sphunter.networking.sendChatMessage
import hu.kszi2.sphunter.textformat.TF.TFComment
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import org.slf4j.LoggerFactory
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.thread


object SPHunter : ModInitializer {
    private const val MOD_ID = "sphunter";
    internal val logger = LoggerFactory.getLogger(MOD_ID)

    private var greet = false
    var hunting = false
        private set
    var queue = WorldQueue()
        private set

    internal var getServers = false

    override fun onInitialize() {
        logger.info("SPHunter: Starting beta build!")

        //Registering the HUNT command
        registerCommand()

        //Greeting player
        greetPlayer()

        //Registering hunting
        registerHunting()
    }

    fun toggleHunting() {
        when (hunting) {
            true -> hunting = false
            false -> {
                queue = WorldQueue()
                hunting = true
            }
        }
    }

    internal fun offHunting() {
        hunting = false
    }

    private fun greetPlayer() {
        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            if (!greet) {
                thread {
                    Thread.sleep(5000)
                    sendChatMessage(
                        TFComment("\nThank you for using SPHunter!\n")
                    )
                    greet = true
                }
            }
        }
    }

    private fun registerHunting() {
        fixedRateTimer("SPHunter-SoulTicker", false, 0, 1000) {
            queue.age() //Stepping one second
            if (hunting) {
                queue.addCurrentWorld()
            }
        }

        fixedRateTimer("SPHunter-WorldManager", false, 0, 5000) {
            queue.log()
        }
    }

    private fun registerCommand() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("sphunter")
                    .coreCommand()
                    .helpCommand()
                    .regentimeCommand()
                    .aliasesCommand()
                    .huntCommand()
                    .generaterouteCommand()
            )
        })
    }
}