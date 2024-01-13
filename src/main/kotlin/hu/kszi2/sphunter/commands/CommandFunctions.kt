package hu.kszi2.sphunter.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import hu.kszi2.sphunter.core.getSecondsUntilSoulPoint
import hu.kszi2.sphunter.textformat.TF
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
fun LiteralArgumentBuilder<FabricClientCommandSource?>.helpCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    return this.then(
        ClientCommandManager.literal("help")
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

fun LiteralArgumentBuilder<FabricClientCommandSource?>.regentimeCommand(): LiteralArgumentBuilder<FabricClientCommandSource?> {
    return this.then(ClientCommandManager.literal("regentime")
        .executes { context ->
            val scnds = getSecondsUntilSoulPoint()
            context.source.sendFeedback(
                TF.TFWarning(
                    String.format(
                        "\nTime until next soul point is %02d:%02d",
                        scnds / 60,
                        scnds % 60
                    )
                )
            )
            1
        }
    )
}

