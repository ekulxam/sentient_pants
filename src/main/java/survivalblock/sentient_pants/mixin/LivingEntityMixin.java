package survivalblock.sentient_pants.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.atmosphere.atmospheric_api.not_mixin.item.ItemStackOfUndyingS2CPayload;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;
import survivalblock.sentient_pants.common.init.SentientPantsTags;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "applyArmorToDamage", at = @At("RETURN"))
    private void spawnPants(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        World world = this.getWorld();
        if (world.isClient()) {
            return;
        }
        ItemStack stack = this.getEquippedStack(EquipmentSlot.LEGS);
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (stack.atmospheric_api$getAbsoluteLevel(SentientPantsTags.SentientPantsEnchantmentTags.SENTIENT_PANTS_EFFECT) <= 0) {
            return;
        }
        LivingEntity living = (LivingEntity) (Object) this;
        if (living instanceof SentientPantsEntity) {
            return;
        }
        world.spawnEntity(new SentientPantsEntity(world, living, stack));
        if (living instanceof ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, new ItemStackOfUndyingS2CPayload(stack.copyWithCount(1),
                    new ItemStackOfUndyingS2CPayload.ParticleEffectHolder(true),
                    new ItemStackOfUndyingS2CPayload.SoundEventHolder(true)));
        }
        stack.decrement(1);
    }
}
