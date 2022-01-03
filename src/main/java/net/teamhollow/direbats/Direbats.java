package net.teamhollow.direbats;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.teamhollow.direbats.entity.DirebatsEntities;
import net.teamhollow.direbats.item.DirebatsItems;
import net.teamhollow.direbats.sound.DirebatsSoundEvents;
import net.teamhollow.direbats.world.DirebatsGameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Direbats implements ModInitializer {
    public static final String MOD_ID = "direbats";
    public static final String MOD_NAME = "Direbats";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "item_group"), () -> new ItemStack(DirebatsItems.DIREBAT_FANG));

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {}", MOD_NAME);

        Reflection.initialize(
            DirebatsGameRules.class,
            DirebatsSoundEvents.class,
            DirebatsItems.class,
            DirebatsEntities.class
        );

        LOGGER.info("Initialized {}", MOD_NAME);
    }
}
