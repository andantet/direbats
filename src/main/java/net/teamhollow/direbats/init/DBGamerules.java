package net.teamhollow.direbats.init;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.teamhollow.direbats.Direbats;

public class DBGamerules {
    public static CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(new Identifier(Direbats.MOD_ID, "category"), new TranslatableText("gamerule." + Direbats.MOD_ID + ".category").formatted(Formatting.BOLD).formatted(Formatting.YELLOW));

    public static GameRules.Key<GameRules.BooleanRule> DIREBAT_ITEM_PICKUP = register("direbatItemPickup", GameRuleFactory.createBooleanRule(true));

    public DBGamerules() {}

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String id, GameRules.Type<T> type) {
        return GameRuleRegistry.register(Direbats.MOD_ID + "." + id, CATEGORY, type);
    }
}
