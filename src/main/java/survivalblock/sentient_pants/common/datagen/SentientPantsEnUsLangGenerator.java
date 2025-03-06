package survivalblock.sentient_pants.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.sentient_pants.common.init.SentientPantsEntityTypes;
import survivalblock.sentient_pants.common.init.SentientPantsGameRules;

import java.util.concurrent.CompletableFuture;

public class SentientPantsEnUsLangGenerator extends FabricLanguageProvider {

    public SentientPantsEnUsLangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        // entity
        translationBuilder.add(SentientPantsEntityTypes.SENTIENT_PANTS, "Sentient Pants");

        // enchantment
        translationBuilder.add("enchantment.sentient_pants.sentient_pants", "Sentience (For The Pants)");
        translationBuilder.add("enchantment.sentient_pants.sentient_pants.desc", "On being damaged, the leggings will run off and attack entities. Right-click the pants entity to get the leggings (item) back.");

        // packs
        translationBuilder.add("dataPack.sentient_pants.sentient_pants_is_a_curse.name", "Sentient Pants - Is a Curse");

        // gamerules
        translationBuilder.add(SentientPantsGameRules.STRONGER_PANTS.getTranslationKey(), "Sentient Pants - Stronger Pants");
        translationBuilder.add(SentientPantsGameRules.PANTS_SEEK_OUT_TARGETS.getTranslationKey(), "Sentient Pants - Pants Automatically Seek Out Target Enemies");
        translationBuilder.add(SentientPantsGameRules.RETURN_ON_DEATH.getTranslationKey(), "Sentient Pants - Pants Return To Owner Upon Death");
        translationBuilder.add(SentientPantsGameRules.DROP_PANTS_ON_DEATH.getTranslationKey(), "Sentient Pants - Drop Leggings On Death");
        translationBuilder.add(SentientPantsGameRules.NO_KICKING.getTranslationKey(), "Sentient Pants - Prevent Target Knockback");
    }
}
