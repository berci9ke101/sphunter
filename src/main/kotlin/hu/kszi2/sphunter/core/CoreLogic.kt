package hu.kszi2.sphunter.core

import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.exception.WorldNotFoundException
import hu.kszi2.sphunter.networking.sendChatMessage
import hu.kszi2.sphunter.textformat.TF.TFComment
import hu.kszi2.sphunter.textformat.TF.TFInfo
import hu.kszi2.sphunter.textformat.TF.TFRoute
import hu.kszi2.sphunter.textformat.TF.TFWhite
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.concurrent.thread

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
    SPHunter.generating = true //Turn on generating

    val currentQueue = SPHunter.queue
    currentQueue.sortSelf()

    val text = TFComment("-----------------------\n")
    text.append(TFWhite("     The optimal route:\n"))
    currentQueue.forEachIndexed { i, world ->
        text.append(TFRoute(i, world))
    }
    text.append(TFComment("-----------------------"))
    return text
}

internal fun visitedServers(): MutableText {
    if (SPHunter.queue.size() <= 0) {
        throw WorldNotFoundException("No worlds are loaded yet. Can't display visited servers!")
    }

    val sortedList = mutableListOf<Int>()
    SPHunter.queue.forEach {
        sortedList.add(it.worldNum)
    }
    sortedList.sort()

    val text = TFComment("-----------------------\n")
    text.append(TFWhite("   Visited server list:\n"))
    sortedList.forEach { worldNum ->
        text.append(TFComment("["))
        text.append(Text.literal("WC").formatted(Formatting.GRAY))
        text.append(TFWhite("$worldNum"))
        text.append(TFComment("] "))
    }
    text.append(TFComment("\n-----------------------"))
    return text
}

internal fun routeSplash() {
    ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
        if (SPHunter.generating) {
            thread {
                Thread.sleep(2000)
                sendChatMessage(
                    generateRouteOutput()
                )
            }
        } else {
            if (SPHunter.hunting) {
                thread {
                    Thread.sleep(2000)
                    sendChatMessage(visitedServers())
                }
            }
        }
    }
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