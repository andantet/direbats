package dev.andante.direbats.entity

import dev.andante.direbats.Direbats
import dev.andante.direbats.item.DirebatsItems
import dev.andante.direbats.tag.DirebatsBiomeTags
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.block.DispenserBlock
import net.minecraft.block.dispenser.ProjectileDispenserBehavior
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.SpawnRestriction
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Position
import net.minecraft.util.registry.Registry
import net.minecraft.world.Heightmap
import net.minecraft.world.World

/**
 * Represents Direbats entities.
 */
object DirebatsEntityTypes {
    /**
     * Represents a Direbat entity.
     * @see [DirebatEntity]
     */
    val DIREBAT: EntityType<DirebatEntity> = register("direbat",
        FabricEntityTypeBuilder.createMob<DirebatEntity>()
            .entityFactory(::DirebatEntity)
            .spawnGroup(SpawnGroup.MONSTER)
            .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DirebatEntity::canSpawn)
            .defaultAttributes(DirebatEntity::createDirebatAttributes)
            .dimensions(EntityDimensions.changing(0.95F, 0.9F))
            .build()
    )

    /**
     * Represents a Direbat Fang Arrow entity.
     * @see [DirebatFangArrowEntity]
     */
    val DIREBAT_FANG_ARROW: EntityType<DirebatFangArrowEntity> = register("direbat_fang_arrow",
        FabricEntityTypeBuilder.create<DirebatFangArrowEntity>()
            .entityFactory(::DirebatFangArrowEntity)
            .spawnGroup(SpawnGroup.MISC)
            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
            .trackRangeChunks(4)
            .trackedUpdateRate(20)
            .build()
    )

    init {
        DispenserBlock.registerBehavior(DirebatsItems.DIREBAT_FANG_ARROW, object : ProjectileDispenserBehavior() {
            override fun createProjectile(world: World, pos: Position, stack: ItemStack): ProjectileEntity {
                val entity = DirebatFangArrowEntity(world, pos.x, pos.y, pos.z)
                entity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED
                return entity
            }
        })

        BiomeModifications.addSpawn(BiomeSelectors.tag(DirebatsBiomeTags.DIREBAT_CAN_SPAWN), SpawnGroup.MONSTER, DIREBAT, 36, 1, 3)
    }

    private fun <T : Entity> register(id: String, build: EntityType<T>): EntityType<T> {
        return Registry.register(Registry.ENTITY_TYPE, Identifier(Direbats.MOD_ID, id), build)
    }
}
