package dev.andante.direbats.item

import dev.andante.direbats.Direbats
import dev.andante.direbats.entity.DirebatsEntityTypes
import net.minecraft.item.Item
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

/**
 * Represents Direbats items.
 */
object DirebatsItems {
    /**
     * Represents a Direbat Spawn Egg item.
     */
    val DIREBAT_SPAWN_EGG = register("direbat_spawn_egg", SpawnEggItem(DirebatsEntityTypes.DIREBAT, 0x3E3E3E, 0xF8E1D3, Item.Settings().group(DirebatsItemGroups.ALL)))

    /**
     * Represents a Direbat Fang item.
     */
    val DIREBAT_FANG = register("direbat_fang", ::Item)

    /**
     * Represents a Direbat Fang Arrow item.
     */
    val DIREBAT_FANG_ARROW = register("direbat_fang_arrow", ::DirebatFangArrowItem)

    private fun register(id: String, item: Item): Item {
        return Registry.register(Registry.ITEM, Identifier(Direbats.MOD_ID, id), item)
    }

    private fun register(id: String, item: (Item.Settings) -> Item): Item {
        return register(id, item.invoke(Item.Settings().group(DirebatsItemGroups.ALL)))
    }
}
