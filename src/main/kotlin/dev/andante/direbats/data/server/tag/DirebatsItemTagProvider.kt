package dev.andante.direbats.data.server.tag

import dev.andante.direbats.item.DirebatsItems
import dev.andante.direbats.tag.DirebatsItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags
import net.minecraft.item.AxeItem
import net.minecraft.item.HoeItem
import net.minecraft.item.Items
import net.minecraft.item.PickaxeItem
import net.minecraft.item.ShovelItem
import net.minecraft.item.SwordItem
import net.minecraft.item.TridentItem
import net.minecraft.tag.ItemTags
import net.minecraft.util.registry.Registry

/**
 * Generates Direbats item tags.
 */
class DirebatsItemTagProvider(generator: FabricDataGenerator) : FabricTagProvider.ItemTagProvider(generator) {
    override fun generateTags() {
        getOrCreateTagBuilder(DirebatsItemTags.PICKED_UP_BY_DIREBAT)
            .forceAddTag(ItemTags.ARROWS)
            .forceAddTag(ConventionalItemTags.EMPTY_BUCKETS)
            .forceAddTag(ConventionalItemTags.WATER_BUCKETS)
            .forceAddTag(ConventionalItemTags.LAVA_BUCKETS)
            .forceAddTag(ConventionalItemTags.MILK_BUCKETS)
            .forceAddTag(ConventionalItemTags.FOODS)
            .forceAddTag(DirebatsItemTags.DIREBAT_PICKS_UP_EGG_ADVANCEMENT_ITEMS)

        Registry.ITEM.filter { it is SwordItem || it is AxeItem || it is PickaxeItem || it is ShovelItem || it is HoeItem || it is TridentItem }.forEach {
            getOrCreateTagBuilder(DirebatsItemTags.PICKED_UP_BY_DIREBAT).add(it)
        }

        getOrCreateTagBuilder(DirebatsItemTags.DIREBAT_PICKS_UP_EGG_ADVANCEMENT_ITEMS).add(
            Items.EGG,
            Items.DRAGON_EGG,
            Items.TURTLE_EGG
        )

        getOrCreateTagBuilder(ItemTags.ARROWS).add(
            DirebatsItems.DIREBAT_FANG_ARROW
        )
    }
}
