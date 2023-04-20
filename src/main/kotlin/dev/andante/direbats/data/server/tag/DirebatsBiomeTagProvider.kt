package dev.andante.direbats.data.server.tag

import dev.andante.direbats.tag.DirebatsBiomeTags
import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.biome.Biome

/**
 * Generates Direbats biome tags.
 */
class DirebatsBiomeTagProvider(out: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider<Biome>(out, RegistryKeys.BIOME, registriesFuture)  {
    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        getOrCreateTagBuilder(DirebatsBiomeTags.DIREBAT_CAN_SPAWN)
            .forceAddTag(ConventionalBiomeTags.FOREST)
            .forceAddTag(ConventionalBiomeTags.TAIGA)
    }
}
