package survivalblock.sentient_pants.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import survivalblock.sentient_pants.common.init.SentientPantsEnchantments;

public class SentientPantsDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(SentientPantsEnUsLangGenerator::new);
		pack.addProvider(SentientPantsTagGenerator.SentientPantsEnchantmentTagGenerator::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, SentientPantsEnchantments::bootstrap);
	}
}
