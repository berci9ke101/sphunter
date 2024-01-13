package hu.kszi2.sphunter.networking

import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.exception.ServerNotFoundException
import net.minecraft.client.MinecraftClient

internal fun checkWCNetwork() {
    //checking whether the client is connected to the wynncraft network
    try {
        if (SPHunter.currentServerSession.serverInfo?.address?.contains("wynncraft")?.not() == true) {
            throw ServerNotFoundException("You are not connected to the wynncraft network!")
        }
    } catch (_: Exception) {
        throw ServerNotFoundException("You are not connected to any server!")
    }
}

internal fun loadCurrentServerSession() {
    try {
        //checking whether the client is connected to a server
        SPHunter.currentServerSession = MinecraftClient.getInstance().networkHandler!!
    } catch (_: Exception) {
        throw ServerNotFoundException("Could not find server!")
    }
    checkWCNetwork()
}

internal fun executeCommand(command: String) {
    try {
        SPHunter.currentServerSession.sendChatCommand(command)
    } catch (_: Exception) {
        throw ServerNotFoundException("You are not connected to any server!")
    }
}