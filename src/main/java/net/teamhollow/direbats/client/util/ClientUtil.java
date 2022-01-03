package net.teamhollow.direbats.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.teamhollow.direbats.Direbats;

@Environment(EnvType.CLIENT)
public class ClientUtil {
    public static Identifier texture(String s) {
        return new Identifier(Direbats.MOD_ID, "textures/%s.png".formatted(s));
    }

    public static Identifier entityTexture(String s) {
        return texture("entity/%s".formatted(s));
    }
}
