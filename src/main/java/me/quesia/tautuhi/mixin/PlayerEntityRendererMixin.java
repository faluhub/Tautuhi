package me.quesia.tautuhi.mixin;

import me.quesia.tautuhi.renderer.CapeRenderer;
import me.quesia.tautuhi.renderer.ElytraRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(dispatcher, model, shadowRadius);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V", at = @At("TAIL"))
    private void addFeatures(EntityRenderDispatcher dispatcher, boolean bl, CallbackInfo ci) {
        this.addFeature(new CapeRenderer(this));
        this.addFeature(new ElytraRenderer<>(this));
        this.features.removeIf(renderer -> renderer instanceof CapeFeatureRenderer);
        this.features.removeIf(renderer -> renderer instanceof ElytraFeatureRenderer);
    }
}
