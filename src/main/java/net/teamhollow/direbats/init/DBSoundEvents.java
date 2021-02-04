package net.teamhollow.direbats.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.entity.direbat.DirebatEntity;

@Mod.EventBusSubscriber(modid = Direbats.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DBSoundEvents {
    public static final SoundEvent ENTITY_DIREBAT_AMBIENT = createDirebatSound("ambient");
    public static final SoundEvent ENTITY_DIREBAT_HURT = createDirebatSound("hurt");
    public static final SoundEvent ENTITY_DIREBAT_ATTACK = createDirebatSound("attack");
    public static final SoundEvent ENTITY_DIREBAT_DEATH = createDirebatSound("death");
    private static SoundEvent createDirebatSound(String id) {
        return createEntitySound(DirebatEntity.id, id);
    }

    private static SoundEvent register(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Direbats.MOD_ID, name);
        return new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        for (SoundEvent soundEvent : new SoundEvent[]{ ENTITY_DIREBAT_AMBIENT, ENTITY_DIREBAT_HURT, ENTITY_DIREBAT_ATTACK, ENTITY_DIREBAT_DEATH }) {
            event.getRegistry().register(soundEvent);
        }
    }

    private static SoundEvent createEntitySound(String entity, String id) {
        return register("entity." + entity + "." + id);
    }
}
