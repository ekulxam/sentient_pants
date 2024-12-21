package survivalblock.sentient_pants.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.sentient_pants.common.SentientPants;
import survivalblock.sentient_pants.common.init.SentientPantsTags;

import java.util.concurrent.CompletableFuture;

public class SentientPantsTagGenerator {

    public static class SentientPantsEnchantmentTagGenerator extends FabricTagProvider.EnchantmentTagProvider {
        public SentientPantsEnchantmentTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(SentientPantsTags.SentientPantsEnchantmentTags.SENTIENT_PANTS_EFFECT).add(SentientPants.id("sentient_pants"));
        }
    }
}
