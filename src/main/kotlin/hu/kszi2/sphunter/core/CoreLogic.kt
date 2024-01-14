package hu.kszi2.sphunter.core

import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.exception.WorldNotFoundException
import hu.kszi2.sphunter.networking.sendChatMessage
import hu.kszi2.sphunter.textformat.TF.TFInfo
import hu.kszi2.sphunter.textformat.TF.TFRoute
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

fun parseTime(seconds: Int): String {
    return String.format(
        "%02d:%02d",
        seconds / 60,
        seconds % 60
    )
}

//FIXME: when player changes world-> chat clears...
internal fun generateRouteOutput(): MutableText {
    if (SPHunter.queue.size() <= 0) {
        throw WorldNotFoundException("No worlds are loaded yet. Try again later!")
    }

    SPHunter.offHunting() //Turn off hunting
    val currentQueue = SPHunter.queue

    val text = Text.literal("     The optimal route:\n").formatted(Formatting.WHITE)

    currentQueue.forEachIndexed { i, world ->
        text.append(TFRoute(i, world))
    }
    text.append(Text.literal("-----------------------").formatted(Formatting.WHITE))
    return text
}

//-------------For AutoHunt
private fun countDown() {
    sendChatMessage(TFInfo("Hunting will begin shortly!"))
    Thread.sleep(1000)
    sendChatMessage(TFInfo("3.."))
    Thread.sleep(1000)
    sendChatMessage(TFInfo("2."))
    Thread.sleep(1000)
    sendChatMessage(TFInfo("1!"))
    Thread.sleep(1000)
}