package dev.andante.direbats.entity

import dev.andante.direbats.item.DirebatsItems
import dev.andante.direbats.tag.DirebatsEntityTypeTags
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.world.World

/**
 * Represents a Direbat Fang Arrow entity.
 * @see [DirebatsEntityTypes.DIREBAT_FANG_ARROW]
 */
class DirebatFangArrowEntity : PersistentProjectileEntity {
    private var duration = 300

    constructor(entityType: EntityType<out DirebatFangArrowEntity>, world: World) : super(entityType, world)
    constructor(world: World, owner: LivingEntity) : super(DirebatsEntityTypes.DIREBAT_FANG_ARROW, owner, world)
    constructor(world: World, x: Double, y: Double, z: Double) : super(DirebatsEntityTypes.DIREBAT_FANG_ARROW, x, y, z, world)

    override fun tick() {
        super.tick()

        if (world.isClient) {
            if (inGround) {
                if (inGroundTime % 5 == 0) {
                    spawnParticles(1)
                }
            } else {
                spawnParticles(2)
            }
        }
    }

    private fun spawnParticles(amount: Int) {
        val color = StatusEffects.BLINDNESS.color
        if (color != -1 && amount > 0) {
            val r = (color shr 16 and 0xFF) / 255.0
            val g = (color shr 8 and 0xFF) / 255.0
            val b = (color and 0xFF) / 255.0
            for (i in 1..amount) {
                world.addParticle(ParticleTypes.ENTITY_EFFECT, getParticleX(0.5), this.randomBodyY, getParticleZ(0.5), r, g, b)
            }
        }
    }

    override fun onHit(target: LivingEntity) {
        super.onHit(target)

        if (!target.type.isIn(DirebatsEntityTypeTags.DIREBAT_FANG_ARROW_EFFECTS_IMMUNE)) {
            // blind
            target.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, duration, 0), this.effectCause)

            // simulate confusion caused by blindness if mob
            if (target is MobEntity && target.target != null) {
                // clear visibility cache
                target.visibilityCache.clear()

                // cancel all goals (simulate confusion)
                listOf(target.goalSelector, target.targetSelector).forEach { it.goals.forEach(Goal::stop) }

                // clear targetting variables
                target.target = null
                target.attacker = null
            }
        }
    }

    override fun asItemStack(): ItemStack {
        return ItemStack(DirebatsItems.DIREBAT_FANG_ARROW)
    }

    /* NBT */

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt(DURATION_KEY, duration)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        duration = nbt.getInt(DURATION_KEY)
    }

    companion object {
        const val DURATION_KEY = "Duration"
    }
}
