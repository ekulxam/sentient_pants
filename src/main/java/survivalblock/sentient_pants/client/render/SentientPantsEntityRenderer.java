package survivalblock.sentient_pants.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import survivalblock.sentient_pants.common.SentientPants;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;

public class SentientPantsEntityRenderer<T extends SentientPantsEntity, M extends SentientPantsEntityModel<T>> extends MobEntityRenderer<T, M> {

    private static final Identifier TEXTURE = SentientPants.id("textures/entity/sentient_pants.png");

    public SentientPantsEntityRenderer(EntityRendererFactory.Context ctx) {
        this(ctx, SentientPantsEntityModelLayers.SENTIENT_PANTS, SentientPantsEntityModelLayers.SENTIENT_PANTS_INNER_ARMOR);
    }

    @SuppressWarnings({"unchecked"})
    public SentientPantsEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer) {
        super(ctx, (M) new SentientPantsEntityModel<T>(ctx.getPart(layer)), 0.5f);
        SentientPantsEntityModel<T> armorModel = new SentientPantsEntityModel<>(ctx.getPart(legsArmorLayer));
        this.addFeature(new ArmorFeatureRenderer<>(this, armorModel, armorModel, ctx.getModelManager())); // It's safe to do this because I won't be rendering chestplate or helmet anyways
    }

    @Override
    public Identifier getTexture(SentientPantsEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(T pants, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.getModel().sneaking = pants.isInSneakingPose();
        super.render(pants, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Vec3d getPositionOffset(T entity, float tickDelta) {
        if (entity.isInSneakingPose()) {
            return new Vec3d(0.0, -0.125, 0.0);
        }
        return super.getPositionOffset(entity, tickDelta);
    }

    @Override
    protected void setupTransforms(T pants, MatrixStack matrixStack, float animationProgress, float bodyYaw, float tickDelta, float scale) {
        if (!SentientPantsEntityRenderer.shouldFlipUpsideDown(pants)) {
            matrixStack.translate(0, -0.16, 0);
        }
        super.setupTransforms(pants, matrixStack, animationProgress, bodyYaw, tickDelta, scale);
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = this.getTexture(entity);
        if (translucent || showBody) {
            return RenderLayer.getItemEntityTranslucentCull(identifier);
        }
        if (showOutline) {
            return RenderLayer.getOutline(identifier);
        }
        return null;
    }
}
