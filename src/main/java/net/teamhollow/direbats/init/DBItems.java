package net.teamhollow.direbats.init;

import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.entity.direbat.DirebatEntity;
import net.teamhollow.direbats.item.FangArrowItem;

@Mod.EventBusSubscriber(modid = Direbats.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DBItems {
    public static final Item DIREBAT_SPAWN_EGG = new SpawnEggItem(DBEntities.DIREBAT, 12691306, 15058059, (new Item.Properties()).group(Direbats.DIREBAT));

    public static final Item BAT_FANG = new Item(new Item.Properties().group(Direbats.DIREBAT));
    public static final Item FANG_ARROW = new FangArrowItem(new Item.Properties().group(Direbats.DIREBAT));

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(BAT_FANG.setRegistryName("bat_fang"));
        event.getRegistry().register(DIREBAT_SPAWN_EGG.setRegistryName(DirebatEntity.id + "_spawn_egg"));
        event.getRegistry().register(FANG_ARROW.setRegistryName(FangArrowItem.id));
    }
}
