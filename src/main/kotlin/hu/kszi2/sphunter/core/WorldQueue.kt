package hu.kszi2.sphunter.core

import hu.kszi2.sphunter.SPHunter.logger
import hu.kszi2.sphunter.networking.getCurrentWorldPair
import java.util.concurrent.CopyOnWriteArrayList

class WorldQueue : Iterable<WorldEntry> {
    private val queue = CopyOnWriteArrayList<WorldEntry>()

    fun size(): Int {
        return queue.size
    }

    fun log() {
        queue.forEach {
            logger.info(
                "Soul Point status: [WC${it.worldNum}] - ${parseTime(it.spTime)}"
            )
        }
    }

    fun addCurrentWorld() {
        val worldEntry = getCurrentWorldPair()

        if (worldEntry.worldNum == -1) {
            return
        }
        this.add(worldEntry)
    }

    fun age() {
        queue.forEach {
            it.age()
        }
    }

    private fun add(worldEntry: WorldEntry) {
        queue.forEach {
            if (it == worldEntry) {
                //Adjusting time dilatation
                it.spTime = worldEntry.spTime
                return
            }
        }

        queue.add(worldEntry)
        logger.info("Added world: ${worldEntry.worldNum}")
        this.sortSelf()
    }

    private fun sortSelf() {
        queue.sortWith { o1, o2 ->
            o1.spTime - o2.spTime
        }
    }

    override fun iterator(): Iterator<WorldEntry> {
        return queue.iterator()
    }
}