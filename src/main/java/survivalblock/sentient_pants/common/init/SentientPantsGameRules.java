package survivalblock.sentient_pants.common.init;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class SentientPantsGameRules {

    public static final GameRules.Key<GameRules.BooleanRule> STRONGER_PANTS =
            GameRuleRegistry.register("sentient_pants:stronger_pants", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static final GameRules.Key<GameRules.BooleanRule> PANTS_SEEK_OUT_TARGETS =
            GameRuleRegistry.register("sentient_pants:pants_seek_out_targets", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static final GameRules.Key<GameRules.BooleanRule> RETURN_ON_DEATH =
            GameRuleRegistry.register("sentient_pants:return_on_death", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> DROP_PANTS_ON_DEATH =
            GameRuleRegistry.register("sentient_pants:drop_pants_on_death", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> NO_KICKING =
            GameRuleRegistry.register("sentient_pants:no_kicking", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static void init() {

    }
}
