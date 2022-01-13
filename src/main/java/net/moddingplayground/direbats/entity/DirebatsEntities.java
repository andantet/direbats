package net.moddingplayground.direbats.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.moddingplayground.direbats.Direbats;
import net.moddingplayground.direbats.item.DirebatsItems;

public class DirebatsEntities {
    public static final EntityType<DirebatEntity> DIREBAT = register(
        "direbat",
        FabricEntityTypeBuilder
            .<DirebatEntity>createMob()
            .entityFactory(DirebatEntity::new).spawnGroup(SpawnGroup.MONSTER)
            .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DirebatEntity::canSpawn)
            .defaultAttributes(DirebatEntity::createDirebatAttributes).dimensions(EntityDimensions.changing(0.95F, 0.9F)),
        colors(0x3E3E3E, 0xF8E1D3)
    );

    public static final EntityType<DirebatFangArrowEntity> DIREBAT_FANG_ARROW = register(
        "direbat_fang_arrow",
        FabricEntityTypeBuilder
            .<DirebatFangArrowEntity>create()
            .entityFactory(DirebatFangArrowEntity::new).spawnGroup(SpawnGroup.MISC)
            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
            .trackRangeChunks(4).trackedUpdateRate(20)
    );

    static {
        DispenserBlock.registerBehavior(DirebatsItems.DIREBAT_FANG_ARROW, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position pos, ItemStack stack) {
                PersistentProjectileEntity entity = new DirebatFangArrowEntity(world, pos.getX(), pos.getY(), pos.getZ());
                entity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entity;
            }
        });

        BiomeModifications.addSpawn(
            ctx -> BiomeSelectors.foundInOverworld().test(ctx) && BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE).test(ctx),
            SpawnGroup.MONSTER, DirebatsEntities.DIREBAT, 47, 2, 4
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType, Pair<Integer, Integer> colors, SpawnEggFactory eggFactory) {
        EntityType<T> builtEntityType = entityType.build();
        if (eggFactory != null) {
            Item.Settings settings = new FabricItemSettings().maxCount(64).group(Direbats.ITEM_GROUP);
            Item item = eggFactory.apply((EntityType<? extends MobEntity>) builtEntityType, colors.getLeft(), colors.getRight(), settings);
            Registry.register(Registry.ITEM,  new Identifier(Direbats.MOD_ID, "%s_spawn_egg".formatted(id)), item);
        }
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Direbats.MOD_ID, id), builtEntityType);
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType, Pair<Integer, Integer> colors) {
        return register(id, entityType, colors, SpawnEggItem::new);
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType) {
        return register(id, entityType, null, null);
    }

    private static Pair<Integer, Integer> colors(int primary, int secondary) {
        return new Pair<>(primary, secondary);
    }

    @FunctionalInterface
    private interface SpawnEggFactory {
        SpawnEggItem apply(EntityType<? extends MobEntity> type, int primaryColor, int secondaryColor, Item.Settings settings);
    }
}
