package survivalblock.sentient_pants.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Enchantments.class)
public interface EnchantmentsAccessor {

    @Invoker("register")
    static void sentient_pants$invokeRegister(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        throw new UnsupportedOperationException();
    }
}
