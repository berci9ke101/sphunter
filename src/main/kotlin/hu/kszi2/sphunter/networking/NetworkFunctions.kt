package hu.kszi2.sphunter.networking

import hu.kszi2.sphunter.exception.ServerNotFoundException
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.text.Text

internal fun checkWCNetwork(): ClientPlayNetworkHandler {
    //checking whether the client is connected to the wynncraft network
    return try {
        val session = MinecraftClient.getInstance().networkHandler!!
        if (session.serverInfo!!.address!!.contains("wynncraft").not()) {
            throw ServerNotFoundException("You are not connected to the wynncraft network!")
        }
        session
    } catch (_: Exception) {
        throw ServerNotFoundException("You are not connected to any server!")
    }
}

internal fun executeCommand(
    command: String,
    session: ClientPlayNetworkHandler = checkWCNetwork()
) {
    try {
        session.sendChatCommand(command)
    } catch (_: Exception) {
        throw ServerNotFoundException("You are not connected to any server!")
    }
}

internal fun sendChatMessage(text: Text) {
    MinecraftClient.getInstance().inGameHud.chatHud.addMessage(text)
}