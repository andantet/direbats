package dev.andante.direbats.data.server.tag

import dev.andante.direbats.tag.DirebatsGameEventTags
import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.GameEventTags

/**
 * Generates Direbats game event tags.
 */
class DirebatsGameEventTagProvider(out: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider.GameEventTagProvider(out, registriesFuture)  {
    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        getOrCreateTagBuilder(DirebatsGameEventTags.DIREBAT_CAN_LISTEN)
            .forceAddTag(GameEventTags.VIBRATIONS)
    }
}
