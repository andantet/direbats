package dev.andante.direbats.data

import dev.andante.direbats.data.client.DirebatsLanguageProvider
import dev.andante.direbats.data.client.DirebatsModelProvider
import dev.andante.direbats.data.server.DirebatsAdvancementProvider
import dev.andante.direbats.data.server.DirebatsEntityLootTableProvider
import dev.andante.direbats.data.server.DirebatsRecipeProvider
import dev.andante.direbats.data.server.tag.DirebatsBiomeTagProvider
import dev.andante.direbats.data.server.tag.DirebatsEntityTypeTagProvider
import dev.andante.direbats.data.server.tag.DirebatsGameEventTagProvider
import dev.andante.direbats.data.server.tag.DirebatsItemTagProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

/**
 * Generates Direbats assets and data.
 */
object DirebatsDataGeneration : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
        val pack: FabricDataGenerator.Pack = generator.createPack()

        pack.addProvider(::DirebatsModelProvider)
        pack.addProvider(::DirebatsLanguageProvider)

        pack.addProvider(::DirebatsAdvancementProvider)
        pack.addProvider(::DirebatsEntityLootTableProvider)
        pack.addProvider(::DirebatsRecipeProvider)

        pack.addProvider(::DirebatsBiomeTagProvider)
        pack.addProvider(::DirebatsEntityTypeTagProvider)
        pack.addProvider(::DirebatsGameEventTagProvider)
        pack.addProvider(::DirebatsItemTagProvider)
    }
}
