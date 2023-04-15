package dev.andante.direbats.item

import dev.andante.direbats.Direbats
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object DirebatsItemGroups {
    val ALL = FabricItemGroup.builder(Identifier(Direbats.MOD_ID, "all"))
        .entries { _, entries ->
            entries.add(DirebatsItems.DIREBAT_SPAWN_EGG)
            entries.add(DirebatsItems.DIREBAT_FANG)
            entries.add(DirebatsItems.DIREBAT_FANG_ARROW)
        }
        .icon { ItemStack(DirebatsItems.DIREBAT_FANG) }
        .build()!!
}
