package survivalblock.sentient_pants.common;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import survivalblock.atmosphere.atmospheric_api.not_mixin.resource.AtmosphericResourceManagerHelper;
import survivalblock.sentient_pants.common.init.SentientPantsEntityTypes;
import survivalblock.sentient_pants.common.init.SentientPantsGameRules;

public class SentientPants implements ModInitializer {

	public static final String MOD_ID = "sentient_pants";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier CURSE_DATAPACK = SentientPants.id("sentient_pants_is_a_curse");

	@Override
	public void onInitialize() {
		SentientPantsEntityTypes.init();
		SentientPantsGameRules.init();
		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
			AtmosphericResourceManagerHelper.registerBuiltinDataPack(CURSE_DATAPACK, modContainer, Text.translatable("dataPack.sentient_pants.sentient_pants_is_a_curse.name"), ResourcePackActivationType.NORMAL);
		});

	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}