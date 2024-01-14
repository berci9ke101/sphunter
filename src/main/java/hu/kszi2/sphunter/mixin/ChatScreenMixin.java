package hu.kszi2.sphunter.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static hu.kszi2.sphunter.networking.NetworkFunctionsKt.parseServers;

//------------For auto-hunt
@Mixin(ChatHud.class)
public class ChatScreenMixin {
    @Inject(at = @At("RETURN"), method = "logChatMessage")
    public void onGameMessage(Text message, MessageIndicator indicator, CallbackInfo ci) {
        parseServers(message);
    }


}
