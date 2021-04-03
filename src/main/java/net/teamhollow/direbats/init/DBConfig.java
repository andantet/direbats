package net.teamhollow.direbats.init;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.teamhollow.direbats.Direbats;

import java.io.File;

@Mod.EventBusSubscriber
public class DBConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec INSTANCE;

    public static final ForgeConfigSpec.BooleanValue DIREBAT_ITEM_PICKUP;

    static {
        DIREBAT_ITEM_PICKUP = BUILDER
            .comment("Direbats pick up items")
            .define("direbatItemPickup", true);

        INSTANCE = BUILDER.build();
    }

    public static void loadConfig() {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(FMLPaths.CONFIGDIR.get().resolve(Direbats.MOD_ID).toString() + ".toml")).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        INSTANCE.setConfig(file);
    }
}
