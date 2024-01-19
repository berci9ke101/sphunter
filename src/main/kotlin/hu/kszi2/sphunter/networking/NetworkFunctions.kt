package hu.kszi2.sphunter.networking

import hu.kszi2.sphunter.SPHunter
import hu.kszi2.sphunter.SPHunter.getServers
import hu.kszi2.sphunter.core.WorldEntry
import hu.kszi2.sphunter.exception.ServerNotFoundException
import hu.kszi2.sphunter.exception.WorldNotFoundException
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

internal fun getSecondsUntilSoulPoint(): Int {
    return try {
        (24000 - (MinecraftClient.getInstance().world!!.timeOfDay.toInt() % 24000)) / 20
    } catch (_: Exception) {
        throw WorldNotFoundException("Could not find wynncraft world!")
    }
}

internal fun getCurrentWorld(): Int {
    val playerList = MinecraftClient.getInstance().networkHandler!!.playerList
    val regex = Regex("Global \\[WC(?<id>\\d+)]")

    playerList.forEach {
        if (regex.containsMatchIn(it.displayName.toString())) {
            return regex.find(it.displayName.toString())!!.groups["id"]!!.value.toInt()
        }
    }
    return -1
}

internal fun getCurrentWorldPair(): WorldEntry {
    var pair = WorldEntry()
    if (onWorld()) {
        pair = WorldEntry(getCurrentWorld(), getSecondsUntilSoulPoint())
    }
    return pair
}

internal fun onWorld(): Boolean {
    MinecraftClient.getInstance().networkHandler!!.playerList.forEach {
        if (Regex("Global \\[WC(?<id>\\d+)]").containsMatchIn(it.displayName.toString())) {
            return true
        }
    }
    return false
}

//-----------For auto-hunt
internal fun parseServers(message: Text) {
    if (getServers) {
        val regex = Regex("WC(?<id>\\d+)")
        if (regex.containsMatchIn(message.toString())) {
            val results = regex.findAll(message.toString())
            results.forEach {
                SPHunter.logger.info("SPHunter found server: WC" + it.groups["id"]!!.value.toInt())
            }
        }
    }
}

internal fun getAllWorlds() {
    getServers = true
    executeCommand("servers list")
    getServers = false
}