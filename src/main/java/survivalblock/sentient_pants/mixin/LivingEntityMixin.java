package survivalblock.sentient_pants.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.atmosphere.atmospheric_api.not_mixin.item.ItemStackOfUndyingS2CPayload;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;
import survivalblock.sentient_pants.common.init.SentientPantsGameRules;
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
                    player.getId(),
                    new ItemStackOfUndyingS2CPayload.ParticleEffectHolder(true),
                    new ItemStackOfUndyingS2CPayload.SoundEventHolder(true)));
        }
        stack.decrement(1);
    }

    @ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;timeUntilRegen:I", opcode = Opcodes.GETFIELD)))
    private boolean noKnockbackMeansNoCooldown(boolean original, DamageSource source, float amount){
        if (original) {
            return true;
        }
        if (source.getAttacker() instanceof SentientPantsEntity && source.isDirect()) {
            return this.getWorld().getGameRules().getBoolean(SentientPantsGameRules.NO_KICKING);
        }
        return false;
    }
}
