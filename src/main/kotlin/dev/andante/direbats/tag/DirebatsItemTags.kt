package dev.andante.direbats.tag

import dev.andante.direbats.Direbats
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object DirebatsItemTags {
    /**
     * Represents a list of items that grant the Direbat picks up egg advancement.
     */
    val DIREBAT_PICKS_UP_EGG_ADVANCEMENT_ITEMS = register("direbat_picks_up_egg_advancement_items")

    private fun register(id: String): TagKey<Item> {
        return TagKey.of(RegistryKeys.ITEM, Identifier(Direbats.MOD_ID, id))
    }
}
