package net.teamhollow.direbats.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.entity.direbat.DirebatEntity;
import net.teamhollow.direbats.entity.direbat_fang_arrow.DirebatFangArrowEntity;

@Mod.EventBusSubscriber(modid = Direbats.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DBEntities {

    public static final EntityType<DirebatEntity> DIREBAT = EntityType.Builder.create(DirebatEntity::new, EntityClassification.CREATURE).size(1.0F, 1.0F).build(new ResourceLocation(DirebatEntity.id).toString());
    public static final EntityType<DirebatFangArrowEntity> DIREBAT_FANG_ARROW = EntityType.Builder.<DirebatFangArrowEntity>create(DirebatFangArrowEntity::new, EntityClassification.MISC).setTrackingRange(4).setCustomClientFactory(DirebatFangArrowEntity::new).setUpdateInterval(4).setShouldReceiveVelocityUpdates(true).size(0.5F, 0.5F).build(new ResourceLocation(DirebatFangArrowEntity.id).toString());


    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityType<?>> event) {
        GlobalEntityTypeAttributes.put(DIREBAT, DirebatEntity.getAttributeMap().create());
        EntitySpawnPlacementRegistry.register(DIREBAT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DirebatEntity::canSpawn);

        event.getRegistry().register(DIREBAT.setRegistryName(DirebatEntity.id));
        event.getRegistry().register(DIREBAT_FANG_ARROW.setRegistryName(DirebatFangArrowEntity.id));
    }
}
