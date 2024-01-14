package hu.kszi2.sphunter.core

import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.SPHunter.logger
import hu.kszi2.sphunter.commands.getServers
import hu.kszi2.sphunter.exception.WorldNotFoundException
import hu.kszi2.sphunter.networking.executeCommand
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import java.util.concurrent.CopyOnWriteArrayList

class WorldQueue {
    /**
     * Key = WynnCraft server id
     * Value = Seconds until next soul point
     */
    private val queue = CopyOnWriteArrayList<WorldEntry>()

    fun log() {
        queue.forEach {
            logger.info(
                "Soul Point status: [WC${it.worldNum}] - ${
                    String.format(
                        "%02d:%02d",
                        it.spTime / 60,
                        it.spTime % 60
                    )
                }"
            )
        }
    }

    fun autoAdd() {
        val worldEntry = getCurrentWorldPair()
        if (worldEntry.worldNum == -1) {
            return
        }
        this.add(worldEntry)
        logger.info("Added world: ${worldEntry.worldNum}")
    }

    fun age() {
        queue.forEach {
            it.age()
        }
    }

    private fun add(worldEntry: WorldEntry) {
        queue.forEach {
            if (it.worldNum == worldEntry.worldNum) {
                return
            }
        }

        queue.add(worldEntry)
        this.sortSelf()
    }

    private fun sortSelf() {
        queue.sortWith { o1, o2 ->
            o1.spTime - o2.spTime
        }
    }
}

internal fun getSecondsUntilSoulPoint(): Int {
    return try {
        (24000 - (MinecraftClient.getInstance().world!!.timeOfDay.toInt() % 24000)) / 20
    } catch (_: Exception) {
        throw WorldNotFoundException("Could not find wynncraft world!")
    }
}

internal fun getCurrentWorld(): Int {
    val playerList = MinecraftClient.getInstance().networkHandler!!.playerList
    val regex = Regex("\\[WC(?<id>\\d+)]")

    playerList.forEach {
        if (regex.containsMatchIn(it.displayName.toString())) {
            return regex.find(it.displayName.toString())!!.groups["id"]!!.value.toInt()
        }
    }
    return -1
}

internal fun getCurrentWorldPair(): WorldEntry {
    var pair = WorldEntry()
    //while (!onWorld()) {
    pair = WorldEntry(getCurrentWorld(), getSecondsUntilSoulPoint())
    //}
    return pair
}

internal fun onWorld(): Boolean {
    MinecraftClient.getInstance().networkHandler!!.playerList.forEach {
        if (Regex("\\[WC(?<id>\\d+)]").containsMatchIn(it.displayName.toString())) {
            return true
        }
    }
    return false
}

internal fun parseServers(message: Text) {
    if (getServers) {
        val regex = Regex("WC(?<id>\\d+)")
        if (regex.containsMatchIn(message.toString())) {
            val results = regex.findAll(message.toString())
            results.forEach {
                SPHunter.logger.info("SPHunter found server: WC" + it.groups["id"]!!.value.toInt())
            }
        }
    }
}

//-----------For auto-hunt
internal fun getAllWorlds() {
    getServers = true
    executeCommand("servers list")
    getServers = false
}