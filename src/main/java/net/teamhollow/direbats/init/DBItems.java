package net.teamhollow.direbats.init;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.item.*;

public class DBItems {
    public static final Item DIREBAT_FANG_ARROW = register(DirebatFangArrowItem.id, new DirebatFangArrowItem());

    public DBItems() {}

    public static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Direbats.MOD_ID, id), item);
    }
}
