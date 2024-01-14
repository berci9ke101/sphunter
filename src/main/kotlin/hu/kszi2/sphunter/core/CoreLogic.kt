package hu.kszi2.sphunter.core

import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.exception.WorldNotFoundException
import hu.kszi2.sphunter.networking.sendChatMessage
import hu.kszi2.sphunter.textformat.TF.TFInfo

fun parseTime(seconds: Int): String {
    return String.format(
        "%02d:%02d",
        seconds / 60,
        seconds % 60
    )
}

//FIXME: when player changes world-> chat clears...
internal fun generateRouteOutput(): String {
    if (SPHunter.queue.size() <= 0) {
        throw WorldNotFoundException("No worlds are loaded yet. Try again later!.")
    }

    SPHunter.offHunting() //Turn off hunting
    val currentQueue = SPHunter.queue

    var text = "--->The optimal route<---\n"

    currentQueue.forEachIndexed { i, world ->
        text += "${(i + 1).toString().padStart(2, '0')}. " +
                "[WC${
                    world.worldNum.toString().padStart(2, '0')
                }] - Time left: ${parseTime(world.spTime)}\n"
    }
    text += "-----------------------"
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