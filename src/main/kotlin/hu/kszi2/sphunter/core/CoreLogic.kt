package hu.kszi2.sphunter.core

import hu.kszi2.sphunter.exception.WorldNotFoundException
import hu.kszi2.sphunter.textformat.TF
import net.minecraft.client.MinecraftClient


internal fun getSecondsUntilSoulPoint(): Int {
    return try {
        (24000 - (MinecraftClient.getInstance().world!!.timeOfDay.toInt() % 24000)) / 20
    } catch (_: Exception) {
        throw WorldNotFoundException("Could not find wynncraft world!")
    }
}

internal fun countDown() {
    val chatHud = MinecraftClient.getInstance().inGameHud.chatHud
    chatHud.addMessage(TF.TFComment("Hunting will begin shortly!"))
    Thread.sleep(1000)
    chatHud.addMessage(TF.TFComment("3.."))
    Thread.sleep(1000)
    chatHud.addMessage(TF.TFComment("2."))
    Thread.sleep(1000)
    chatHud.addMessage(TF.TFComment("1!"))
    Thread.sleep(1000)
}

internal fun hunt() {
    //Get the current seconds in the world until the soul point
    //val scnds = getSecondsUntilSoulPoint()

    //TODO: maga a logika
}