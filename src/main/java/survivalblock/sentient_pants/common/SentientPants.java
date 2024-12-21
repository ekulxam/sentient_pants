package survivalblock.sentient_pants.common;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;
import survivalblock.sentient_pants.common.init.SentientPantsEntityTypes;

public class SentientPants implements ModInitializer {

	public static final String MOD_ID = "sentient_pants";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SentientPantsEntityTypes.init();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}