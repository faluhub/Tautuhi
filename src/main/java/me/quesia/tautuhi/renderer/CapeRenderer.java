package me.quesia.tautuhi.renderer;

import me.quesia.tautuhi.Tautuhi;
import me.quesia.tautuhi.handler.PlayerHandler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CapeRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public CapeRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        PlayerHandler playerHandler = PlayerHandler.fromPlayer(entity);
        Identifier texture = playerHandler.getTexture(entity);

        if (entity.canRenderCapeTexture() && !entity.isInvisible() && entity.isPartVisible(PlayerModelPart.CAPE) && texture != null) {
            ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.CHEST);
            if (!itemStack.getItem().equals(Items.ELYTRA)) {
                matrices.push();
                matrices.translate(0.0F, 0.0F, 0.125F);
                double d = MathHelper.lerp(tickDelta, entity.prevCapeX, entity.capeX) - MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
                double e = MathHelper.lerp(tickDelta, entity.prevCapeY, entity.capeY) - MathHelper.lerp(tickDelta, entity.prevY, entity.getY());
                double m = MathHelper.lerp(tickDelta, entity.prevCapeZ, entity.capeZ) - MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());
                float n = entity.prevBodyYaw + (entity.bodyYaw - entity.prevBodyYaw);
                double o = MathHelper.sin(n * 0.017453292F);
                double p = -MathHelper.cos(n * 0.017453292F);
                double q = (float) e * 10.0F;
                q = MathHelper.clamp(q, -6.0F, 32.0F);
                double r = (d * o + m * p) * 100.0F;
                r = MathHelper.clamp(r, 0.0F, 150.0F);
                double s = (d * p - m * o) * 100.0F;
                s = MathHelper.clamp(s, -20.0F, 20.0F);
                r = r < 0.0F ? 0.0F : r;
                double t = MathHelper.lerp(tickDelta, entity.prevStrideDistance, entity.strideDistance);
                q += MathHelper.sin(MathHelper.lerp(tickDelta, entity.prevHorizontalSpeed, entity.horizontalSpeed) * 6.0F) * 32.0F * t;
                q += entity.isInSneakingPose() ? 25.0F : 0.0F;
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion((float) (6.0F + r / 2.0F + q)));
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float) (s / 2.0F)));
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) (180.0F - s / 2.0F)));
                VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), false, Tautuhi.GLINT);
                this.getContextModel().renderCape(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
                matrices.pop();
            }
        }
    }
}
