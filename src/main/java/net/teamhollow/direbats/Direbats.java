package net.teamhollow.direbats;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.teamhollow.direbats.init.DBEntities;
import net.teamhollow.direbats.init.DBGamerules;
import net.teamhollow.direbats.init.DBItems;
import net.teamhollow.direbats.init.DBSoundEvents;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Direbats implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "direbats";
    public static final String MOD_NAME = "Direbats";

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "item_group"),
        () -> new ItemStack(DBItems.DIREBAT_FANG)
    );

    @SuppressWarnings("deprecation")
    @Override
    public void onInitialize() {
        log("Initializing");

        new DBGamerules();
        new DBSoundEvents();

        new DBItems();
        new DBEntities();

        BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.FOREST, Biome.Category.TAIGA), SpawnGroup.MONSTER, DBEntities.DIREBAT, 200, 1, 4);

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
