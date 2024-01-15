package hu.kszi2.sphunter.textformat

import hu.kszi2.sphunter.core.WorldEntry
import hu.kszi2.sphunter.core.parseTime
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object TF {
    fun TFCommand(text: String): MutableText {
        return Text.literal(text).formatted(Formatting.GREEN)
    }

    fun TFComment(text: String): MutableText {
        return Text.literal(text).formatted(Formatting.BLUE)
    }

    fun TFInfo(text: String): MutableText {
        return Text.literal(text).formatted(Formatting.GOLD)
    }

    fun TFWhite(text: String): MutableText {
        return Text.literal(text).formatted(Formatting.WHITE)
    }

    fun TFRoute(i: Int, worldInstance: WorldEntry): MutableText {
        val numbering = TFInfo("${(i + 1).toString().padStart(2, '0')}. ")
        val worldPrefix = TFComment("[")
        val wc = Text.literal("WC").formatted(Formatting.GRAY)
        val worldNum = TFWhite(
            worldInstance.worldNum.toString().padStart(2, '0')
        )
        val worldSuffix = TFComment("]")
        val dash = Text.literal(" - ")
        val trailing = TFComment("Time left: ")
        val timeLeft = TFInfo(parseTime(worldInstance.spTime) + "\n")

        return numbering
            .append(worldPrefix)
            .append(wc)
            .append(worldNum)
            .append(worldSuffix)
            .append(dash)
            .append(trailing)
            .append(timeLeft)
    }

    fun TFError(text: String): MutableText {
        return Text.literal("[SPHunter]: $text").formatted(Formatting.RED)
    }
}