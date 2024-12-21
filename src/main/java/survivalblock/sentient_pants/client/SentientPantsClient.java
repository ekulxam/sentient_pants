package survivalblock.sentient_pants.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import survivalblock.sentient_pants.client.render.SentientPantsEntityModelLayers;
import survivalblock.sentient_pants.client.render.SentientPantsEntityRenderer;
import survivalblock.sentient_pants.common.init.SentientPantsEntityTypes;

public class SentientPantsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(SentientPantsEntityTypes.SENTIENT_PANTS, SentientPantsEntityRenderer::new);
		SentientPantsEntityModelLayers.init();
	}
}
