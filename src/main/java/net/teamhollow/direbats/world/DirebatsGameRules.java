package net.teamhollow.direbats.world;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.teamhollow.direbats.Direbats;

import static net.minecraft.world.GameRules.*;

public class DirebatsGameRules {
    public static final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(
        new Identifier(Direbats.MOD_ID, "category"),
        new TranslatableText("gamerule.%s.category".formatted(Direbats.MOD_ID))
            .formatted(Formatting.YELLOW, Formatting.BOLD)
    );

    public static final Key<BooleanRule> DIREBAT_ITEM_PICKUP = register("direbatItemPickup", true);

    private static <T extends Rule<T>> Key<T> register(String id, Type<T> type) {
        return GameRuleRegistry.register("%s:%s".formatted(Direbats.MOD_ID, id), DirebatsGameRules.CATEGORY, type);
    }

    private static Key<BooleanRule> register(String id, boolean defaultValue) {
        return register(id, GameRuleFactory.createBooleanRule(defaultValue));
    }
}
