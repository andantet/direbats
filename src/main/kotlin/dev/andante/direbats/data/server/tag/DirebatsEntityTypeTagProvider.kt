package dev.andante.direbats.data.server.tag

import dev.andante.direbats.entity.DirebatsEntityTypes
import dev.andante.direbats.tag.DirebatsEntityTypeTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags
import net.minecraft.tag.EntityTypeTags

/**
 * Generates Direbats entity type tags.
 */
class DirebatsEntityTypeTagProvider(generator: FabricDataGenerator) : FabricTagProvider.EntityTypeTagProvider(generator) {
    override fun generateTags() {
        getOrCreateTagBuilder(DirebatsEntityTypeTags.DIREBAT_FANG_ARROW_EFFECTS_IMMUNE).add(
            DirebatsEntityTypes.DIREBAT
        ).forceAddTag(ConventionalEntityTypeTags.BOSSES)

        getOrCreateTagBuilder(EntityTypeTags.ARROWS).add(
            DirebatsEntityTypes.DIREBAT
        )
    }
}
