package survivalblock.sentient_pants.common.init;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import survivalblock.sentient_pants.common.SentientPants;
import survivalblock.sentient_pants.mixin.EnchantmentsAccessor;

public class SentientPantsEnchantments {
    public static final RegistryKey<Enchantment> SENTIENT_PANTS = RegistryKey.of(RegistryKeys.ENCHANTMENT, SentientPants.id("sentient_pants"));

    public static void bootstrap(Registerable<Enchantment> registry) {
        RegistryEntryLookup<Item> itemRegistryEntryLookup = registry.getRegistryLookup(RegistryKeys.ITEM);
        register(registry, SENTIENT_PANTS,
                Enchantment.builder(Enchantment.definition(
                        itemRegistryEntryLookup.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        1,
                        1,
                        Enchantment.leveledCost(20, 0),
                        Enchantment.leveledCost(50, 0),
                        1,
                        AttributeModifierSlot.LEGS)));
    }

    public static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        EnchantmentsAccessor.sentient_pants$invokeRegister(registry, key, builder);
    }
}
