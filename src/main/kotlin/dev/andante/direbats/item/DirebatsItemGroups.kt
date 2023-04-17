package dev.andante.direbats.item

import dev.andante.direbats.Direbats
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object DirebatsItemGroups {
    val ALL = FabricItemGroupBuilder.create(Identifier(Direbats.MOD_ID, "all"))
        .icon { ItemStack(DirebatsItems.DIREBAT_FANG) }
        .build()!!
}
