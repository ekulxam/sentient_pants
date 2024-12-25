package survivalblock.sentient_pants.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.atmosphere.atmospheric_api.not_mixin.datagen.EnchantmentRegistryEntryLookupContainer;
import survivalblock.atmosphere.atmospheric_api.not_mixin.datagen.FabricEnchantmentProvider;
import survivalblock.sentient_pants.common.init.SentientPantsEnchantments;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SentientPantsEnchantmentGenerator extends FabricEnchantmentProvider {

    public SentientPantsEnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void addEnchantmentsToEntries(RegistryWrapper.WrapperLookup wrapperLookup, EnchantmentRegistryEntryLookupContainer container, Entries entries) {
        for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : SentientPantsEnchantments.asEnchantments(container).entrySet()) {
            entries.add(entry.getKey(), entry.getValue());
        }
    }
}
