package net.teamhollow.direbats.init;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.item.DirebatFangArrowItem;

public class DBItems {
    public static final Item DIREBAT_FANG = register("direbat_fang", new Item(new Item.Settings().group(Direbats.ITEM_GROUP)));
    public static final Item DIREBAT_FANG_ARROW = register(DirebatFangArrowItem.id, new DirebatFangArrowItem());

    public static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Direbats.MOD_ID, id), item);
    }
}
