package survivalblock.sentient_pants.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.sentient_pants.common.SentientPants;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;

public class SentientPantsEntityTypes {

    public static final EntityType<SentientPantsEntity> SENTIENT_PANTS = registerEntity("sentient_pants", EntityType.Builder.<SentientPantsEntity>create(SentientPantsEntity::new, SpawnGroup.MISC).dimensions(0.6f, 1f));

    @SuppressWarnings("SameParameterValue")
    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> builder) {
        return Registry.register(Registries.ENTITY_TYPE, SentientPants.id(name),
                builder.build());
    }

    public static void init() {
        FabricDefaultAttributeRegistry.register(SENTIENT_PANTS, SentientPantsEntity.createPantsAttributes());
    }
}
