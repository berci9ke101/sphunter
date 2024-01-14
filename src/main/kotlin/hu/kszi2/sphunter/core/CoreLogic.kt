package hu.kszi2.sphunter.core

import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.networking.sendChatMessage
import hu.kszi2.sphunter.textformat.TF.TFInfo

internal fun registerWorld() {
    SPHunter.queue.autoAdd()
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