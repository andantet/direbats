package dev.andante.direbats.data.client

import dev.andante.direbats.Direbats
import dev.andante.direbats.data.DirebatsAdvancementLanguageStrings
import dev.andante.direbats.data.DirebatsSubtitleNames
import dev.andante.direbats.entity.DirebatsEntityTypes
import dev.andante.direbats.item.DirebatsItemGroups
import dev.andante.direbats.item.DirebatsItems
import dev.andante.direbats.world.DirebatsGameRules
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.entity.EntityType
import net.minecraft.registry.Registries
import net.minecraft.world.GameRules

/**
 * Generates Direbats language files.
 */
class DirebatsLanguageProvider(out: FabricDataOutput) : FabricLanguageProvider(out) {
    override fun generateTranslations(builder: TranslationBuilder) {
        builder.add(DirebatsItemGroups.ALL, Direbats.MOD_NAME)

        builder.add(DirebatsEntityTypes.DIREBAT, "Direbat")
        builder.add(DirebatsEntityTypes.DIREBAT_FANG_ARROW, "Direbat Fang Arrow")

        builder.add(DirebatsItems.DIREBAT_SPAWN_EGG, "Direbat Spawn Egg")
        builder.add(DirebatsItems.DIREBAT_FANG, "Direbat Fang")
        builder.add(DirebatsItems.DIREBAT_FANG_ARROW, "Direbat Fang Arrow")

        DirebatsEntityTypes.DIREBAT.run {
            builder.subtitle(this, DirebatsSubtitleNames.AMBIENT, "Direbat squeaks menacingly")
            builder.subtitle(this, DirebatsSubtitleNames.HURT, "Direbat hurts")
            builder.subtitle(this, DirebatsSubtitleNames.ATTACK, "Direbat sinks its teeth")
            builder.subtitle(this, DirebatsSubtitleNames.DEATH, "Direbat dies")
        }

        builder.add(DirebatsGameRules.DO_DIREBAT_ITEM_PICKUP, "Direbats pick up items")

        builder.add(DirebatsAdvancementLanguageStrings.DIREBAT_PICKS_UP_EGG_TITLE, "Angry Bats")
        builder.add(DirebatsAdvancementLanguageStrings.DIREBAT_PICKS_UP_EGG_DESCRIPTION, "Drop an Egg on the ground, and have a Direbat steal it")
    }

    companion object {
        fun TranslationBuilder.add(rule: GameRules.Key<*>, value: String) {
            return add(rule.translationKey, value)
        }

        fun TranslationBuilder.subtitle(entity: EntityType<*>, id: String, value: String) {
            return add(entity.getSubtitle(id), value)
        }

        fun EntityType<*>.getSubtitle(id: String): String {
            val identifier = Registries.ENTITY_TYPE.getId(this)
            return "subtitles.${identifier.namespace}.entity.${identifier.path}.$id"
        }
    }
}
