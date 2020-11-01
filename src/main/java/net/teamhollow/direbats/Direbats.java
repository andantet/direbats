package net.teamhollow.direbats;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.teamhollow.direbats.init.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Direbats implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "direbats";
    public static final String MOD_NAME = "Direbats";

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "item_group"),
        () -> new ItemStack(DBItems.DIREBAT_FANG)
    );

    @Override
    public void onInitialize() {
        log("Initializing");

        new DBSoundEvents();
        new DBItems();
        new DBEntities();

        log("Initialized");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static Identifier texture(String path) {
        return new Identifier(MOD_ID, "textures/" + path + ".png");
    }
}
