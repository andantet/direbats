package dev.andante.direbats.tag

import dev.andante.direbats.Direbats
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome

object DirebatsBiomeTags {
    /**
     * Represents the list of biomes that can spawn Direbats.
     */
    val DIREBAT_CAN_SPAWN = register("direbat_can_spawn")

    private fun register(id: String): TagKey<Biome> {
        return TagKey.of(Registry.BIOME_KEY, Identifier(Direbats.MOD_ID, id))
    }
}
