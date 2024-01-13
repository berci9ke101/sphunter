package hu.kszi2.sphunter.textformat

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
}