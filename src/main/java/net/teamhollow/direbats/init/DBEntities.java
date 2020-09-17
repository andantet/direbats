package net.teamhollow.direbats.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.entity.direbat.DirebatEntity;
import net.teamhollow.direbats.entity.direbat.DirebatEntityRenderer;
import net.teamhollow.direbats.entity.direbat_fang_arrow.DirebatFangArrowEntity;
import net.teamhollow.direbats.entity.direbat_fang_arrow.DirebatFangArrowEntityRenderer;

public class DBEntities {
    public static final EntityType<DirebatEntity> DIREBAT = register(
        DirebatEntity.id,
        DirebatEntity.builder,
        DirebatEntity.spawnEggColors
    );
    public static final EntityType<DirebatFangArrowEntity> DIREBAT_FANG_ARROW = register(
        DirebatFangArrowEntity.id,
        DirebatFangArrowEntity.builder,
        null
    );

    public DBEntities() {
        registerDefaultAttributes(DIREBAT, DirebatEntity.createDirebatAttributes());

        DispenserBlock.registerBehavior(DBItems.DIREBAT_FANG_ARROW, new ProjectileDispenserBehavior() {
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                PersistentProjectileEntity persistentProjectileEntity = new DirebatFangArrowEntity(world, position.getX(), position.getY(), position.getZ());
                persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return persistentProjectileEntity;
            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderers() {
        EntityRendererRegistry INSTANCE = EntityRendererRegistry.INSTANCE;

        INSTANCE.register(
            DIREBAT,
            (entityRenderDispatcher, context) -> new DirebatEntityRenderer(entityRenderDispatcher)
        );
        INSTANCE.register(
            DIREBAT_FANG_ARROW,
            (entityRenderDispatcher, context) -> new DirebatFangArrowEntityRenderer(entityRenderDispatcher)
        );
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> entityType,
            int[] spawnEggColors) {
        EntityType<T> builtEntityType = entityType.build(id);

        if (spawnEggColors != null)
            DBItems.register(id + "_spawn_egg", new SpawnEggItem(builtEntityType, spawnEggColors[0], spawnEggColors[1], new Item.Settings().maxCount(64).group(Direbats.ITEM_GROUP)));

        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Direbats.MOD_ID, id), builtEntityType);
    }

    public static void registerDefaultAttributes(EntityType<? extends LivingEntity> type, DefaultAttributeContainer.Builder builder) {
        FabricDefaultAttributeRegistry.register(type, builder);
    }

    public static Identifier texture(String path) {
        return Direbats.texture("entity/" + path);
    }
}
