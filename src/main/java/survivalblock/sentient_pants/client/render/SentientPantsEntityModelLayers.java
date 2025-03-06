package survivalblock.sentient_pants.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import survivalblock.sentient_pants.common.SentientPants;

public class SentientPantsEntityModelLayers {

    public static final EntityModelLayer SENTIENT_PANTS = new EntityModelLayer(SentientPants.id("sentient_pants"), "main");
    public static final EntityModelLayer SENTIENT_PANTS_INNER_ARMOR = new EntityModelLayer(SentientPants.id("sentient_pants"), "inner_armor");

    public static void init() {
        EntityModelLayerRegistry.registerModelLayer(SENTIENT_PANTS, SentientPantsEntityModelLayers::getDefault);
        EntityModelLayerRegistry.registerModelLayer(SENTIENT_PANTS_INNER_ARMOR, SentientPantsEntityModelLayers::getInnerArmor);
    }

    private static TexturedModelData getDefault() {
        return TexturedModelData.of(SentientPantsEntityModel.getModelData(Dilation.NONE, 0.0f), 64, 64);
    }

    private static TexturedModelData getInnerArmor() {
        return TexturedModelData.of(ArmorEntityModel.getModelData(new Dilation(0.5f)), 64, 32);
    }
}
