package survivalblock.sentient_pants.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import survivalblock.sentient_pants.common.init.SentientPantsEnchantments;
import survivalblock.sentient_pants.common.init.SentientPantsTags;

import java.util.concurrent.CompletableFuture;

public class SentientPantsTagGenerator {

    public static class SentientPantsEnchantmentTagGenerator extends FabricTagProvider.EnchantmentTagProvider {
        public SentientPantsEnchantmentTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(SentientPantsTags.SentientPantsEnchantmentTags.SENTIENT_PANTS_EFFECT).add(SentientPantsEnchantments.SENTIENT_PANTS);
            getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE).add(SentientPantsEnchantments.SENTIENT_PANTS);
        }
    }

    public static class SentientPantsCurseTagGenerator extends FabricTagProvider.EnchantmentTagProvider {
        public SentientPantsCurseTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(EnchantmentTags.CURSE).add(SentientPantsEnchantments.SENTIENT_PANTS);
        }
    }
}
