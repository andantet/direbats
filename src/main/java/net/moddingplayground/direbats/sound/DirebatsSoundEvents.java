package net.moddingplayground.direbats.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.moddingplayground.direbats.Direbats;

public class DirebatsSoundEvents {
    public static final SoundEvent ENTITY_DIREBAT_AMBIENT = direbat("ambient");
    public static final SoundEvent ENTITY_DIREBAT_HURT = direbat("hurt");
    public static final SoundEvent ENTITY_DIREBAT_ATTACK = direbat("attack");
    public static final SoundEvent ENTITY_DIREBAT_DEATH = direbat("death");
    private static SoundEvent direbat(String id) {
        return entity("direbat", id);
    }

    private static SoundEvent entity(String entity, String id) {
        return register("entity." + entity + "." + id);
    }

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(Direbats.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
