package hu.kszi2.sphunter.core

class WorldEntry(val worldNum: Int = -1, var spTime: Int = -1) {
    fun age() {
        if (spTime >= 1) {
            spTime--
        }
        if (spTime == 0) {
            spTime = 1200
        }
    }

    override operator fun equals(other: Any?): Boolean {
        if (other !is WorldEntry) {
            return false
        }

        return this.worldNum == other.worldNum
    }

    override fun hashCode(): Int {
        var result = worldNum
        result = 31 * result + spTime
        return result
    }
}