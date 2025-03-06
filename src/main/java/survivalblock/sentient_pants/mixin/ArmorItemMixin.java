package survivalblock.sentient_pants.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.atmosphere.atmospheric_api.not_mixin.item.ItemStackOfUndyingS2CPayload;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;
import survivalblock.sentient_pants.common.init.SentientPantsTags;

@Mixin(ArmorItem.class)
public class ArmorItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void spawnPantsOnUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!user.isSneaking()) {
            return;
        }
        ItemStack stack = user.getStackInHand(hand);
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (stack.atmospheric_api$getAbsoluteLevel(SentientPantsTags.SentientPantsEnchantmentTags.SENTIENT_PANTS_EFFECT) <= 0) {
            return;
        }
        if (!world.isClient()) {
            world.spawnEntity(new SentientPantsEntity(world, user, stack));
            if (user instanceof ServerPlayerEntity player) {
                ServerPlayNetworking.send(player, new ItemStackOfUndyingS2CPayload(stack.copyWithCount(1),
                        player.getId(),
                        new ItemStackOfUndyingS2CPayload.ParticleEffectHolder(true),
                        new ItemStackOfUndyingS2CPayload.SoundEventHolder(true)));
            }
            stack.decrementUnlessCreative(1, user);
        }
        cir.setReturnValue(TypedActionResult.success(stack, world.isClient()));
    }
}
