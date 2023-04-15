package dev.andante.direbats.data.server

import dev.andante.direbats.item.DirebatsItems
import java.util.function.Consumer
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory

/**
 * Generates Direbats recipes.
 */
class DirebatsRecipeProvider(out: FabricDataOutput) : FabricRecipeProvider(out) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DirebatsItems.DIREBAT_FANG_ARROW, 4)
            .input('#', Items.ARROW)
            .input('X', DirebatsItems.DIREBAT_FANG)
            .pattern(" # ")
            .pattern("#X#")
            .pattern(" # ")
            .criterion("has_direbat_fang", conditionsFromItem(DirebatsItems.DIREBAT_FANG))
            .offerTo(exporter)
    }
}
