package survivalblock.sentient_pants.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.atmosphere.atmospheric_api.not_mixin.datagen.AtmosphericDynamicRegistryProvider;
import survivalblock.atmosphere.atmospheric_api.not_mixin.datagen.RegistryEntryLookupContainer;
import survivalblock.sentient_pants.common.init.SentientPantsEnchantments;

import java.util.concurrent.CompletableFuture;

public class SentientPantsEnchantmentGenerator extends AtmosphericDynamicRegistryProvider<Enchantment> {

    public SentientPantsEnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries, RegistryEntryLookupContainer container) {
        SentientPantsEnchantments.asEnchantments(container).forEach(entries::add);
    }
}
