package net.teamhollow.direbats.init;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.entity.direbat.DirebatEntity;

public class DBSoundEvents {
    public static final SoundEvent ENTITY_DIREBAT_AMBIENT = createDirebatSound("ambient");
    public static final SoundEvent ENTITY_DIREBAT_HURT = createDirebatSound("hurt");
    public static final SoundEvent ENTITY_DIREBAT_ATTACK = createDirebatSound("attack");
    public static final SoundEvent ENTITY_DIREBAT_DEATH = createDirebatSound("death");
    private static SoundEvent createDirebatSound(String id) {
        return createEntitySound(DirebatEntity.id, id);
    }

    private static SoundEvent createEntitySound(String entity, String id) {
        return register("entity." + entity + "." + id);
    }

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(Direbats.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
