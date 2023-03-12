package me.quesia.tautuhi.renderer;

import me.quesia.tautuhi.handler.PlayerHandler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ElytraRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/elytra.png");
    private final ElytraEntityModel<T> model = new ElytraEntityModel<>();

    public ElytraRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.getItem().equals(Items.ELYTRA)) {
            Identifier texture = TEXTURE;
            if (entity instanceof AbstractClientPlayerEntity) {
                AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) entity;
                texture = PlayerHandler.fromPlayer(player).getTexture(player);
            }
            matrices.push();
            matrices.translate(0.0F, 0.0F, 0.125F);
            this.getContextModel().copyStateTo(this.model);
            this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), false, itemStack.hasGlint());
            this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrices.pop();
        }
    }
}
