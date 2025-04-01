package survivalblock.sentient_pants.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.sentient_pants.common.SentientPants;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;

import java.util.Locale;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {

    @Unique
    private static final Identifier sentient_pants$AMONG_US = SentientPants.id("textures/models/armor/among_us.png");

    @WrapOperation(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArmorMaterial$Layer;getTexture(Z)Lnet/minecraft/util/Identifier;"))
    private Identifier useAmongUsTextureInstead(ArmorMaterial.Layer instance, boolean secondLayer, Operation<Identifier> original, @Local(argsOnly = true)LivingEntity living, @Local(argsOnly = true)EquipmentSlot slot) {
        if (!(living instanceof SentientPantsEntity sentientPants) || slot != EquipmentSlot.LEGS) {
            return original.call(instance, secondLayer);
        }
        if (!sentientPants.hasCustomName()) {
            return original.call(instance, secondLayer);
        }
        //noinspection DataFlowIssue (this shouldn't be null)
        String name = sentientPants.getCustomName().getString().toLowerCase(Locale.ROOT);
        if (name.contains("among") && name.contains("us")) {
            return sentient_pants$AMONG_US;
        }
        return original.call(instance, secondLayer);
    }
}
