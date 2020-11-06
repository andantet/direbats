package net.teamhollow.direbats.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamhollow.direbats.Direbats;

@Mod.EventBusSubscriber(modid = Direbats.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DBSoundEvents {
    public static final SoundEvent ENTITY_DIREBAT_AMBIENT = register("entity.direbat.ambient");
    public static final SoundEvent ENTITY_DIREBAT_HURT = register("entity.direbat.hurt");
    public static final SoundEvent ENTITY_DIREBAT_ATTACK = register("entity.direbat.attack");
    public static final SoundEvent ENTITY_DIREBAT_DEATH = register("entity.direbat.death");

    private static SoundEvent register(String name) {
        SoundEvent sound = new SoundEvent(new ResourceLocation(Direbats.MOD_ID, name));
        sound.setRegistryName(new ResourceLocation(Direbats.MOD_ID, name));
        return sound;
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt) {
        evt.getRegistry().register(ENTITY_DIREBAT_AMBIENT);
        evt.getRegistry().register(ENTITY_DIREBAT_HURT);
        evt.getRegistry().register(ENTITY_DIREBAT_ATTACK);
        evt.getRegistry().register(ENTITY_DIREBAT_DEATH);
    }
}
