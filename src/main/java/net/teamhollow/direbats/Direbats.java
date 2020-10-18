package net.teamhollow.direbats;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityClassification;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.teamhollow.direbats.entity.direbat.DirebatEntityRenderer;
import net.teamhollow.direbats.entity.direbat_fang_arrow.DirebatFangArrowEntityRenderer;
import net.teamhollow.direbats.init.DBEntities;
import net.teamhollow.direbats.init.DBItems;

@Mod(Direbats.MOD_ID)
public class Direbats {
    public static Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "direbats";
    public static final String MOD_NAME = "Direbats";

	public static final ItemGroup ITEM_GROUP = new ItemGroup(new ResourceLocation(MOD_ID, "item_group").toString()) {
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(DBItems.DIREBAT_FANG);
		}
    };

	public Direbats() {
        log("Initializing");

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.addListener(Direbats::onBiomesLoaded);
        MinecraftForge.EVENT_BUS.register(this);

        log("Initialized");
	}

	public static void onBiomesLoaded(final BiomeLoadingEvent event) {
			if (event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.THEEND && event.getCategory() != Biome.Category.NONE) {
				event.getSpawns().withSpawner(EntityClassification.AMBIENT, new MobSpawnInfo.Spawners(DBEntities.DIREBAT, 5, 1, 2));
			}
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(DBEntities.DIREBAT, DirebatEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(DBEntities.DIREBAT_FANG_ARROW, DirebatFangArrowEntityRenderer::new);
	}

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
    public static void log(String message) {
        log(Level.INFO, message);
    }
}
