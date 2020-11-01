package net.teamhollow.direbats.init;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.direbats.Direbats;

public class DBSoundEvents {
    public static final SoundEvent ENTITY_DIREBAT_AMBIENT = register("entity.direbat.ambient");
    public static final SoundEvent ENTITY_DIREBAT_HURT = register("entity.direbat.hurt");
    public static final SoundEvent ENTITY_DIREBAT_ATTACK = register("entity.direbat.attack");
    public static final SoundEvent ENTITY_DIREBAT_DEATH = register("entity.direbat.death");

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(Direbats.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
