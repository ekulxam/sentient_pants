package survivalblock.sentient_pants.common.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import survivalblock.sentient_pants.common.SentientPants;

public class SentientPantsTags {

    public static class SentientPantsEnchantmentTags {
        public static final TagKey<Enchantment> SENTIENT_PANTS_EFFECT = TagKey.of(RegistryKeys.ENCHANTMENT, SentientPants.id("sentient_pants_effect"));
    }
}