package net.teamhollow.direbats.init;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.direbats.Direbats;

public class DBItems {
    public DBItems() {}

    public static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Direbats.MOD_ID, id), item);
    }
}
