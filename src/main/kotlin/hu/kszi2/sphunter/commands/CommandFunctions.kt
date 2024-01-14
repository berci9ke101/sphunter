package hu.kszi2.sphunter.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.SPHunter.hunting
import hu.kszi2.sphunter.core.generateRouteOutput
import hu.kszi2.sphunter.core.parseTime
import hu.kszi2.sphunter.networking.checkWCNetwork
import hu.kszi2.sphunter.networking.getCurrentWorld
import hu.kszi2.sphunter.networking.getSecondsUntilSoulPoint
import hu.kszi2.sphunter.textformat.TF.TFCommand
import hu.kszi2.sphunter.textformat.TF.TFComment
import hu.kszi2.sphunter.textformat.TF.TFInfo
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

//Command aliases
private val helpAliases = listOf("help")
private val regentimeAliases = listOf("regentime", "rt")
private val aliasAliases = listOf("aliases", "alias", "as")
private val huntAliases = listOf("hunt", "h")
private val generaterouteAliases = listOf("generateroute", "genroute", "genr", "gr")

fun LiteralArgumentBuilder<FabricClientCommandSource?>.coreCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    return this.executes { context ->
        //Checking whether the client is connected to the wynncraft network
        checkWCNetwork()
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
                    //Checking whether the client is connected to the wynncraft network
                    checkWCNetwork()
                    context.source.sendFeedback(
                        TFComment(
                            "-----------------------" +
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
                            .append(TFComment(" - toggles the hunter mode [on/off]"))

                            .append(TFCommand("\n/sphunter generateroute"))
                            .append(TFComment(" - generates an optimal route to take to regenerate soul points"))

                            .append(TFComment("\n-----------------------"))
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
                //Checking whether the client is connected to the wynncraft network
                checkWCNetwork()

                val seconds = getSecondsUntilSoulPoint()
                context.source.sendFeedback(
                    TFComment("[WC${getCurrentWorld()}] ")
                        .append(TFInfo(parseTime(seconds)))
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
                //Checking whether the client is connected to the wynncraft network
                checkWCNetwork()

                context.source.sendFeedback(
                    TFComment(
                        "-----------------------" +
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

                        .append(TFCommand("\n/sphunter generateroute"))
                        .append(TFComment(" - ${generaterouteAliases.joinToString(separator = ", ")}"))

                        .append(TFComment("\n-----------------------"))
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
                //Checking whether the client is connected to the wynncraft network
                checkWCNetwork()

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

fun LiteralArgumentBuilder<FabricClientCommandSource?>.generaterouteCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    generaterouteAliases.forEach { s ->
        this.then(ClientCommandManager.literal(s)
            .executes { context ->
                //Checking whether the client is connected to the wynncraft network
                checkWCNetwork()

                //Sending back the optimal route
                context.source.sendFeedback(generateRouteOutput())
                1
            }
        )
    }
    return this
}