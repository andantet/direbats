package dev.andante.direbats.data.server

import dev.andante.direbats.item.DirebatsItems
import java.util.function.Consumer
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items

/**
 * Generates Direbats recipes.
 */
class DirebatsRecipeProvider(generator: FabricDataGenerator) : FabricRecipeProvider(generator) {
    override fun generateRecipes(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(DirebatsItems.DIREBAT_FANG_ARROW, 4)
            .input('#', Items.ARROW)
            .input('X', DirebatsItems.DIREBAT_FANG)
            .pattern(" # ")
            .pattern("#X#")
            .pattern(" # ")
            .criterion("has_direbat_fang", conditionsFromItem(DirebatsItems.DIREBAT_FANG))
            .offerTo(exporter)
    }
}
