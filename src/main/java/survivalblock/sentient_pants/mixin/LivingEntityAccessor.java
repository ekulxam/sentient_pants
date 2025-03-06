package survivalblock.sentient_pants.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Mutable
    @Accessor("attributes")
    void sentient_pants$setAttributes(AttributeContainer attributeContainer);
}
