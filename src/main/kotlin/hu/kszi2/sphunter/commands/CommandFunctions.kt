package hu.kszi2.sphunter.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.SPHunter.hunting
import hu.kszi2.sphunter.core.getCurrentWorld
import hu.kszi2.sphunter.core.getSecondsUntilSoulPoint
import hu.kszi2.sphunter.networking.sendChatMessage
import hu.kszi2.sphunter.textformat.TF.TFCommand
import hu.kszi2.sphunter.textformat.TF.TFComment
import hu.kszi2.sphunter.textformat.TF.TFInfo
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

//Command aliases
private val helpAliases = listOf("help", "h")
private val regentimeAliases = listOf("regentime", "rt")
private val aliasAliases = listOf("aliases", "alias", "as")
private val huntAliases = listOf("hunt", "h")

internal var getServers = false

fun LiteralArgumentBuilder<FabricClientCommandSource?>.coreCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    return this.executes { context ->
        context.source!!.sendFeedback(
            TFComment("For help, use: ")
                .append(TFCommand("/sphunter help"))
        )
        1
    }
}

fun LiteralArgumentBuilder<FabricClientCommandSource?>.helpCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    helpAliases.forEach { s ->
        this.then(
            ClientCommandManager.literal(s)
                .executes { context ->
                    context.source.sendFeedback(
                        TFComment(
                            "----------------------" +
                                    "\nSPHunter is a soul point tracker mod, which helps you to get soul points easier." +
                                    "\n" +
                                    "\nAvailable commands are:\n"
                        )
                            .append(TFCommand("\n/sphunter aliases"))
                            .append(TFComment(" - displays the aliases for the commands"))

                            .append(TFCommand("\n/sphunter help"))
                            .append(TFComment(" - displays this message"))

                            .append(TFCommand("\n/sphunter regentime"))
                            .append(TFComment(" - displays the amount of time left for the next soul point"))

                            .append(TFCommand("\n/sphunter hunt"))
                            .append(TFComment(" - toggles the hunter mode"))

                            .append(TFComment("\n----------------------"))
                    )
                    1
                }
        )
    }
    return this
}

fun LiteralArgumentBuilder<FabricClientCommandSource?>.regentimeCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    regentimeAliases.forEach { s ->
        this.then(ClientCommandManager.literal(s)
            .executes { context ->
                val scnds = getSecondsUntilSoulPoint()
                context.source.sendFeedback(
                    TFComment("[WC${getCurrentWorld()}] ").append(
                        TFInfo(
                            String.format(
                                "Time until next soul point is %02d:%02d",
                                scnds / 60,
                                scnds % 60
                            )
                        )
                    )
                )
                1
            }
        )
    }
    return this
}

fun LiteralArgumentBuilder<FabricClientCommandSource?>.aliasesCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    aliasAliases.forEach { s ->
        this.then(ClientCommandManager.literal(s)
            .executes { context ->
                context.source.sendFeedback(
                    TFComment(
                        "----------------------" +
                                "\nAvailable aliases are:\n"
                    )
                        .append(TFCommand("\n/sphunter aliases"))
                        .append(TFComment(" - ${aliasAliases.joinToString(separator = ", ")}"))

                        .append(TFCommand("\n/sphunter help"))
                        .append(TFComment(" - ${helpAliases.joinToString(separator = ", ")}"))

                        .append(TFCommand("\n/sphunter regentime"))
                        .append(TFComment(" - ${regentimeAliases.joinToString(separator = ", ")}"))

                        .append(TFCommand("\n/sphunter hunt"))
                        .append(TFComment(" - ${huntAliases.joinToString(separator = ", ")}"))

                        .append(TFComment("\n----------------------"))
                )
                1
            }
        )
    }
    return this
}

fun LiteralArgumentBuilder<FabricClientCommandSource?>.huntCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    huntAliases.forEach { s ->
        this.then(ClientCommandManager.literal(s)
            .executes { context ->
                val txt: String = if (!hunting) {
                    "Happy hunting!"
                } else {
                    "Stopped Hunting!"
                }

                context.source.sendFeedback(
                    TFInfo(txt)
                )

                //toggles Hunting
                SPHunter.toggleHunting()
                1
            }
        )
    }
    return this
}