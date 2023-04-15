package dev.andante.direbats.data.server

import dev.andante.direbats.Direbats
import dev.andante.direbats.data.DirebatsAdvancementLanguageStrings
import dev.andante.direbats.entity.DirebatsEntityTypes
import dev.andante.direbats.tag.DirebatsItemTags
import java.util.function.Consumer
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementDisplay
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.criterion.ThrownItemPickedUpByEntityCriterion
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.loot.condition.EntityPropertiesLootCondition
import net.minecraft.loot.context.LootContext.EntityTarget
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.text.Text
import net.minecraft.util.Identifier

/**
 * Generates Direbats advancements.
 */
class DirebatsAdvancementProvider(out: FabricDataOutput?) : FabricAdvancementProvider(out) {
    override fun generateAdvancement(exporter: Consumer<Advancement>) {
        exporter.accept(Advancement(
            Identifier(Direbats.MOD_ID, "direbat_picks_up_egg"),
            Advancement(Identifier("adventure/root"), null, null, null, emptyMap(), null),
            AdvancementDisplay(
                ItemStack(Items.EGG),
                Text.translatable(DirebatsAdvancementLanguageStrings.DIREBAT_PICKS_UP_EGG_TITLE),
                Text.translatable(DirebatsAdvancementLanguageStrings.DIREBAT_PICKS_UP_EGG_DESCRIPTION),
                null, AdvancementFrame.CHALLENGE, true, true, false
            ),
            AdvancementRewards.NONE,
            mapOf("pick_up_egg" to AdvancementCriterion(ThrownItemPickedUpByEntityCriterion.Conditions.createThrownItemPickedUpByEntity(
                EntityPredicate.Extended.EMPTY,
                ItemPredicate.Builder.create()
                    .tag(DirebatsItemTags.DIREBAT_PICKS_UP_EGG_ADVANCEMENT_ITEMS)
                    .build(),
                EntityPredicate.Extended.create(EntityPropertiesLootCondition(
                    EntityPredicate.Builder.create()
                        .type(DirebatsEntityTypes.DIREBAT)
                        .build(),
                    EntityTarget.THIS
                ))
            ))),
            arrayOf(arrayOf("pick_up_egg"))
        ))
    }
}
