package dev.andante.direbats.data.client

import dev.andante.direbats.item.DirebatsItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.ModelIds
import net.minecraft.data.client.Models

/**
 * Generates Direbats item models.
 */
class DirebatsModelProvider(generator: FabricDataGenerator) : FabricModelProvider(generator) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        generator.run {
            registerParentedItemModel(DirebatsItems.DIREBAT_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"))
        }
    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        generator.run {
            register(DirebatsItems.DIREBAT_FANG, Models.GENERATED)
            register(DirebatsItems.DIREBAT_FANG_ARROW, Models.GENERATED)
        }
    }
}
