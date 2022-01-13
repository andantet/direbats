package net.moddingplayground.direbats.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.moddingplayground.direbats.Direbats;

public class DirebatsItems {
    public static final Item DIREBAT_FANG = register("direbat_fang", Item::new);
    public static final Item DIREBAT_FANG_ARROW = register("direbat_fang_arrow", DirebatFangArrowItem::new);

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Direbats.MOD_ID, id), item);
    }

    private static Item register(String id, ItemFactory<Item> factory) {
        return register(id, factory.create(new FabricItemSettings().group(Direbats.ITEM_GROUP)));
    }

    @FunctionalInterface
    private interface ItemFactory<T extends Item> {
        T create(FabricItemSettings settings);
    }
}
