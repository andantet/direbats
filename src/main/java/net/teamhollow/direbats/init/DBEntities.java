package net.teamhollow.direbats.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.entity.direbat.DirebatEntity;
import net.teamhollow.direbats.entity.direbat_fang_arrow.DirebatFangArrowEntity;

public class DBEntities {
    public static final EntityType<DirebatEntity> DIREBAT = register(
        DirebatEntity.id,
        FabricEntityTypeBuilder
            .<DirebatEntity>createMob()
            .entityFactory(DirebatEntity::new)
            .spawnGroup(SpawnGroup.MONSTER)
            .dimensions(EntityDimensions.changing(0.95F, 0.9F))
            .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DirebatEntity::canSpawn)
            .trackRangeChunks(5)
            .defaultAttributes(() ->
                MobEntity.createMobAttributes()
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0D)
                    .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.22D)
                    .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22D)
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D)
            ),
        new int[]{ 7097929, 986895 }
    );
    public static final EntityType<DirebatFangArrowEntity> DIREBAT_FANG_ARROW = register(
        DirebatFangArrowEntity.id,
        FabricEntityTypeBuilder
            .<DirebatFangArrowEntity>create()
            .entityFactory(DirebatFangArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
            .trackRangeChunks(4)
            .trackedUpdateRate(20),
        null
    );

    public DBEntities() {
        DispenserBlock.registerBehavior(DBItems.DIREBAT_FANG_ARROW, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                PersistentProjectileEntity persistentProjectileEntity = new DirebatFangArrowEntity(world, position.getX(), position.getY(), position.getZ());
                persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return persistentProjectileEntity;
            }
        });
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType, int[] spawnEggColors) {
        EntityType<T> builtEntityType = entityType.build();

        if (spawnEggColors != null)
            DBItems.register(id + "_spawn_egg", new SpawnEggItem(builtEntityType, spawnEggColors[0], spawnEggColors[1], new Item.Settings().maxCount(64).group(Direbats.ITEM_GROUP)));

        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Direbats.MOD_ID, id), builtEntityType);
    }

    public static Identifier texture(String path) {
        return Direbats.texture("entity/" + path);
    }
}
