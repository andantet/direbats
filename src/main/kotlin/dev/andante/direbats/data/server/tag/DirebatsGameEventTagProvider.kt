package dev.andante.direbats.data.server.tag

import dev.andante.direbats.tag.DirebatsGameEventTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.tag.GameEventTags

/**
 * Generates Direbats game event tags.
 */
class DirebatsGameEventTagProvider(generator: FabricDataGenerator) : FabricTagProvider.GameEventTagProvider(generator)  {
    override fun generateTags() {
        getOrCreateTagBuilder(DirebatsGameEventTags.DIREBAT_CAN_LISTEN).forceAddTag(
            GameEventTags.VIBRATIONS
        )
    }
}
