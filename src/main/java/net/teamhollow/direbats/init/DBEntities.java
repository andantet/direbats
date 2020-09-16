package net.teamhollow.direbats.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.entity.direbat.DirebatEntity;
import net.teamhollow.direbats.entity.direbat.DirebatEntityRenderer;

public class DBEntities {
    public static final EntityType<DirebatEntity> DIREBAT = register(
        DirebatEntity.id,
        DirebatEntity.builder,
        DirebatEntity.spawnEggColors
    );

    public DBEntities() {
        registerDefaultAttributes(DIREBAT, DirebatEntity.createDirebatAttributes());
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderers() {
        EntityRendererRegistry INSTANCE = EntityRendererRegistry.INSTANCE;

        INSTANCE.register(
            DIREBAT,
            (entityRenderDispatcher, context) -> new DirebatEntityRenderer(entityRenderDispatcher)
        );
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> entityType,
            int[] spawnEggColors) {
        EntityType<T> builtEntityType = entityType.build(id);

        if (spawnEggColors[0] != 0)
            DBItems.register(id + "_spawn_egg", new SpawnEggItem(builtEntityType, spawnEggColors[0], spawnEggColors[1],
                    new Item.Settings().maxCount(64).group(Direbats.ITEM_GROUP)));

        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Direbats.MOD_ID, id), builtEntityType);
    }

    public static void registerDefaultAttributes(EntityType<? extends LivingEntity> type, DefaultAttributeContainer.Builder builder) {
        FabricDefaultAttributeRegistry.register(type, builder);
    }

    public static Identifier texture(String path) {
        return Direbats.texture("entity/" + path);
    }
}
