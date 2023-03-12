package me.quesia.tautuhi.mixin;

import me.quesia.tautuhi.handler.PlayerHandler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "addPlayer", at = @At("TAIL"))
    private void addPlayer(int id, AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerHandler.onPlayerJoin(player);
    }
}
