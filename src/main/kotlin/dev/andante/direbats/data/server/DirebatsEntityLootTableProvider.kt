package dev.andante.direbats.data.server

import dev.andante.direbats.entity.DirebatsEntityTypes
import dev.andante.direbats.item.DirebatsItems
import java.util.function.BiConsumer
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.KilledByPlayerLootCondition
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.LootingEnchantLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.util.Identifier

/**
 * Generates Direbats entity loot tables.
 */
class DirebatsEntityLootTableProvider(out: FabricDataOutput) : SimpleFabricLootTableProvider(out, LootContextTypes.ENTITY) {
    override fun accept(exporter: BiConsumer<Identifier, LootTable.Builder>) {
        exporter.accept(DirebatsEntityTypes.DIREBAT.lootTableId,
            LootTable.builder()
                .pool(
                    LootPool.builder()
                        .with(
                            ItemEntry.builder(DirebatsItems.DIREBAT_FANG)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 2.0f)))
                                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 4.0f)))
                        )
                        .conditionally(
                            KilledByPlayerLootCondition.builder()
                        )
                )
        )
    }
}
