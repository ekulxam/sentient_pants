package survivalblock.sentient_pants.client.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class SentientPantsEntityModel<T extends LivingEntity>
        extends BipedEntityModel<T> {

    public SentientPantsEntityModel(ModelPart root) {
        super(root);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.leftLeg, this.rightLeg);
    }

    public static ModelData getModelData(Dilation dilation, float pivotOffsetY) {
        ModelData modelData = BipedEntityModel.getModelData(dilation, pivotOffsetY);
        ModelPartData modelPartData = modelData.getRoot();
        // effectively stops the parts from rendering at all
        modelPartData.addChild(
                EntityModelPartNames.BODY,
                ModelPartBuilder.create().uv(16, 16),
                ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F)
        );
        modelPartData.addChild(
                EntityModelPartNames.RIGHT_LEG,
                ModelPartBuilder.create().uv(0, 16),
                ModelTransform.pivot(-1.9F, 12.0F + pivotOffsetY, 0.0F)
        );
        modelPartData.addChild(
                EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create().uv(0, 16),
                ModelTransform.pivot(1.9F, 12.0F + pivotOffsetY, 0.0F)
        );
        return modelData;
    }
}