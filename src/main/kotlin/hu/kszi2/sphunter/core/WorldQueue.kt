package hu.kszi2.sphunter.core

import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.SPHunter.logger
import hu.kszi2.sphunter.commands.getServers
import hu.kszi2.sphunter.exception.WorldNotFoundException
import hu.kszi2.sphunter.networking.executeCommand
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

class WorldQueue {
    /**
     * Key = WynnCraft server id
     * Value = Seconds until next soul point
     */
    private val queue: MutableList<Pair<Int, Int>> = mutableListOf()

    fun log() {
        queue.forEach {
            logger.info(
                "Soul Point status: [WC${it.first}] - ${
                    String.format(
                        "%02d:%02d",
                        it.second / 60,
                        it.second % 60
                    )
                }"
            )
        }
    }

    fun autoAdd() {
        synchronized(SPHunter) {
            val pair = getCurrentWorldPair()
            if (pair.first == -1) {
                return
            }
            this.add(pair)
            logger.info("Added world: ${pair.first}")
        }
    }

    fun age() {
        synchronized(SPHunter) {
            queue.forEach {
                val new = it.copy(it.first, it.second - 1)

                if (new.second <= 10) {
                    return
                }

                queue.remove(it)
                queue.add(new)
            }
            sortSelf()
        }
    }

    private fun add(worldEntry: Pair<Int, Int>) {
        if (queue.contains(worldEntry)) {
            return
        }
        queue.add(worldEntry)
        this.sortSelf()
    }

    private fun sortSelf() {
        queue.sortWith { o1, o2 ->
            o1.second - o2.second
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

internal fun getCurrentWorldPair(): Pair<Int, Int> {
    val currworldID = getCurrentWorld()
    return Pair(currworldID, getSecondsUntilSoulPoint())
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