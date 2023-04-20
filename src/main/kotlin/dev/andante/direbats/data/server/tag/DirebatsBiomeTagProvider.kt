package dev.andante.direbats.data.server.tag

import dev.andante.direbats.tag.DirebatsBiomeTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome

/**
 * Generates Direbats biome tags.
 */
class DirebatsBiomeTagProvider(out: FabricDataGenerator) : FabricTagProvider.DynamicRegistryTagProvider<Biome>(out, Registry.BIOME_KEY)  {
    override fun generateTags() {
        getOrCreateTagBuilder(DirebatsBiomeTags.DIREBAT_CAN_SPAWN)
            .forceAddTag(ConventionalBiomeTags.FOREST)
            .forceAddTag(ConventionalBiomeTags.TAIGA)
    }
}
