package dev.andante.direbats.world

import dev.andante.direbats.Direbats
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry
import net.minecraft.world.GameRules
import net.minecraft.world.GameRules.BooleanRule
import net.minecraft.world.GameRules.Category

/**
 * Represents Direbats game rules.
 */
object DirebatsGameRules {
    /**
     * Whether or not Direbats can locate and loot item entities.
     */
    val DO_DIREBAT_ITEM_PICKUP: GameRules.Key<BooleanRule> =
        GameRuleRegistry.register("${Direbats.MOD_ID}:doDirebatItemPickup",
            Category.MOBS, GameRuleFactory.createBooleanRule(true)
        )
}
