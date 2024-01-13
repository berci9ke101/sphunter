package hu.kszi2.sphunter.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import hu.kszi2.sphunter.core.getSecondsUntilSoulPoint
import hu.kszi2.sphunter.textformat.TF
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource


private val helpAliases = listOf("help", "h")
private val regentimeAliases = listOf("regentime", "rt")
private val aliasAliases = listOf("aliases", "alias", "as")

fun LiteralArgumentBuilder<FabricClientCommandSource?>.coreCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    return this.executes { context ->
        context.source!!.sendFeedback(
            TF.TFComment("For help, use: ")
                .append(TF.TFCommand("/sphunter help"))
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
                        TF.TFComment(
                            "----------------------" +
                                    "\nSPHunter is a soul point tracker mod, which helps you to get soul points easier." +
                                    "\n" +
                                    "\nAvailable commands are:\n"
                        )
                            .append(TF.TFCommand("\n/sphunter aliases"))
                            .append(TF.TFComment(" - displays the aliases for the commands"))

                            .append(TF.TFCommand("\n/sphunter help"))
                            .append(TF.TFComment(" - displays this message"))

                            .append(TF.TFCommand("\n/sphunter regentime"))
                            .append(TF.TFComment(" - displays the amount of time left for the next soul point"))

                            .append(TF.TFComment("\n----------------------"))
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
                    TF.TFWarning(
                        String.format(
                            "Time until next soul point is %02d:%02d",
                            scnds / 60,
                            scnds % 60
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
                    TF.TFComment(
                        "----------------------" +
                                "\nAvailable aliases are:\n"
                    )
                        .append(TF.TFCommand("\n/sphunter aliases"))
                        .append(TF.TFComment(" - ${aliasAliases.joinToString(separator = ", ")}"))

                        .append(TF.TFCommand("\n/sphunter help"))
                        .append(TF.TFComment(" - ${helpAliases.joinToString(separator = ", ")}"))

                        .append(TF.TFCommand("\n/sphunter regentime"))
                        .append(TF.TFComment(" - ${regentimeAliases.joinToString(separator = ", ")}"))

                        .append(TF.TFComment("\n----------------------"))
                )
                1
            }
        )
    }
    return this
}
