package hu.kszi2.sphunter.textformat

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object TF {
    fun TFCommand(text: String): MutableText {
        return Text.literal(text).formatted(Formatting.BOLD).formatted(Formatting.GREEN)
    }

    fun TFComment(text: String): MutableText {
        return Text.literal(text).formatted(Formatting.AQUA)
    }

    fun TFWarning(text: String): MutableText {
        return Text.literal(text).formatted(Formatting.GOLD).formatted(Formatting.BOLD)
    }
}