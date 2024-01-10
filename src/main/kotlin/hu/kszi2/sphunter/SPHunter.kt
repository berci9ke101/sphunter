package hu.kszi2.sphunter

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object SPHunter : ModInitializer {
    private const val MOD_ID = "sphunter";
	private val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		logger.info("Hello Fabric world!")
	}
}