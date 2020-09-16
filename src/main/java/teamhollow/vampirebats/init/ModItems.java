package teamhollow.direbats.init;

import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.direbats.Main;
import teamhollow.direbats.item.FangArrowItem;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static final Item BATFANG = new Item(new Item.Properties().group(Main.DIREBAT));
    public static final Item DIREBATEGG = new SpawnEggItem(ModEntities.DIREBAT, 12691306, 15058059, (new Item.Properties()).group(Main.DIREBAT));
    public static final Item FANGARROW = new FangArrowItem(new Item.Properties().group(Main.DIREBAT));

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(BATFANG.setRegistryName("batfang"));
        event.getRegistry().register(DIREBATEGG.setRegistryName("direbategg"));
        event.getRegistry().register(FANGARROW.setRegistryName("fangarrow"));
    }
}
