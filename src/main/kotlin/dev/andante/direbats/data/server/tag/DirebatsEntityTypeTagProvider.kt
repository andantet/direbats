package dev.andante.direbats.data.server.tag

import dev.andante.direbats.entity.DirebatsEntityTypes
import dev.andante.direbats.tag.DirebatsEntityTypeTags
import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.EntityTypeTags

/**
 * Generates Direbats entity type tags.
 */
class DirebatsEntityTypeTagProvider(out: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider.EntityTypeTagProvider(out, registriesFuture) {
    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        getOrCreateTagBuilder(DirebatsEntityTypeTags.DIREBAT_FANG_ARROW_EFFECTS_IMMUNE).add(
            DirebatsEntityTypes.DIREBAT
        ).forceAddTag(ConventionalEntityTypeTags.BOSSES)

        getOrCreateTagBuilder(EntityTypeTags.ARROWS).add(
            DirebatsEntityTypes.DIREBAT
        )
    }
}
