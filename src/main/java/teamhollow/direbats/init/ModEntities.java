package teamhollow.direbats.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.direbats.Main;
import teamhollow.direbats.entity.mob.DirebatEntity;
import teamhollow.direbats.entity.projectile.FangArrowEntity;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities
{

    public static final EntityType<DirebatEntity> DIREBAT = EntityType.Builder.create(DirebatEntity::new, EntityClassification.CREATURE).size(0.75F, 0.75F).build(prefix("direbat"));
    public static final EntityType<FangArrowEntity> FANGARROW = EntityType.Builder.<FangArrowEntity>create(FangArrowEntity::new, EntityClassification.MISC).setTrackingRange(4).setCustomClientFactory(FangArrowEntity::new).setUpdateInterval(4).setShouldReceiveVelocityUpdates(true).size(0.5F, 0.5F).build(prefix("fangarrow"));


    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityType<?>> event) {
        GlobalEntityTypeAttributes.put(DIREBAT, DirebatEntity.getAttributeMap().create());
        EntitySpawnPlacementRegistry.register(DIREBAT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DirebatEntity::canSpawn);

        event.getRegistry().register(DIREBAT.setRegistryName("direbat"));
        event.getRegistry().register(FANGARROW.setRegistryName("fangarrow"));
    }

    private static String prefix(String path) {

        return Main.MODID + "." + path;

    }
}
