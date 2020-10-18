package net.teamhollow.direbats.init;

import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.entity.direbat.DirebatEntity;
import net.teamhollow.direbats.item.DirebatFangArrowItem;

@Mod.EventBusSubscriber(modid = Direbats.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DBItems {
    public static final Item DIREBAT_FANG = new Item(new Item.Properties().group(Direbats.ITEM_GROUP));
    public static final Item DIREBAT_FANG_ARROW = new DirebatFangArrowItem(new Item.Properties().group(Direbats.ITEM_GROUP));
    public static final Item DIREBAT_SPAWN_EGG = new SpawnEggItem(DBEntities.DIREBAT, 7097929, 986895, new Item.Properties().group(Direbats.ITEM_GROUP));

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(DIREBAT_FANG.setRegistryName("direbat_fang"));
        event.getRegistry().register(DIREBAT_SPAWN_EGG.setRegistryName(DirebatEntity.id + "_spawn_egg"));
        event.getRegistry().register(DIREBAT_FANG_ARROW.setRegistryName(DirebatFangArrowItem.id));
    }
}
