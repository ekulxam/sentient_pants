package survivalblock.sentient_pants.common.init;

import com.google.common.collect.ImmutableMap;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import survivalblock.atmosphere.atmospheric_api.not_mixin.datagen.EnchantmentRegistryEntryLookupContainer;
import survivalblock.atmosphere.atmospheric_api.not_mixin.datagen.FabricEnchantmentProvider;
import survivalblock.sentient_pants.common.SentientPants;

import java.util.HashMap;
import java.util.Map;

public class SentientPantsEnchantments {

    public static final RegistryKey<Enchantment> SENTIENT_PANTS = RegistryKey.of(RegistryKeys.ENCHANTMENT, SentientPants.id("sentient_pants"));

    public static ImmutableMap<RegistryKey<Enchantment>, Enchantment> asEnchantments(EnchantmentRegistryEntryLookupContainer container) {
        Map<RegistryKey<Enchantment>, Enchantment> enchantments = new HashMap<>();
        RegistryEntryLookup<Item> itemRegistryEntryLookup = container.itemRegistryEntryLookup();
        enchantments.put(SENTIENT_PANTS, Enchantment.builder(Enchantment.definition(
                itemRegistryEntryLookup.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                1,
                1,
                Enchantment.leveledCost(20, 0),
                Enchantment.leveledCost(50, 0),
                16,
                AttributeModifierSlot.LEGS)).build(SENTIENT_PANTS.getValue()));
        return ImmutableMap.copyOf(enchantments);
    }

    public static void bootstrap(Registerable<Enchantment> registry) {
        for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : asEnchantments(new EnchantmentRegistryEntryLookupContainer(registry)).entrySet()) {
            registry.register(entry.getKey(), entry.getValue());
        }
    }
}
