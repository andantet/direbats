package dev.andante.direbats.data.server.tag

import dev.andante.direbats.item.DirebatsItems
import dev.andante.direbats.tag.DirebatsItemTags
import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags

/**
 * Generates Direbats item tags.
 */
class DirebatsItemTagProvider(out: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider.ItemTagProvider(out, registriesFuture) {
    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        getOrCreateTagBuilder(DirebatsItemTags.DIREBAT_PICKS_UP_EGG_ADVANCEMENT_ITEMS).add(
            Items.EGG
        )

        getOrCreateTagBuilder(ItemTags.ARROWS).add(
            DirebatsItems.DIREBAT_FANG_ARROW
        )
    }
}
