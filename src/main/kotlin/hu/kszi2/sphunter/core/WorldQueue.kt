package hu.kszi2.sphunter.core

import hu.kszi2.sphunter.SPHunter.logger
import hu.kszi2.sphunter.networking.getCurrentWorldPair
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

        logger.info("Added world: ${worldEntry.worldNum}")
        queue.add(worldEntry)
        this.sortSelf()
    }

    private fun sortSelf() {
        queue.sortWith { o1, o2 ->
            o1.spTime - o2.spTime
        }
    }
}