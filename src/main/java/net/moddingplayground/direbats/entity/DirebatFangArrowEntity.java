package net.moddingplayground.direbats.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.moddingplayground.direbats.item.DirebatsItems;

public class DirebatFangArrowEntity extends PersistentProjectileEntity {
    private int duration = 300;

    public DirebatFangArrowEntity(EntityType<? extends DirebatFangArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public DirebatFangArrowEntity(World world, LivingEntity owner) {
        super(DirebatsEntities.DIREBAT_FANG_ARROW, owner, world);
    }

    public DirebatFangArrowEntity(World world, double x, double y, double z) {
        super(DirebatsEntities.DIREBAT_FANG_ARROW, x, y, z, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.world.isClient) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) this.spawnParticles(1);
            } else {
                this.spawnParticles(2);
            }
        }
    }

    private void spawnParticles(int amount) {
        int i = StatusEffects.BLINDNESS.getColor();
        if (i != -1 && amount > 0) {
            double d = (i >> 16 & 255) / 255.0D;
            double e = (i >> 8  & 255) / 255.0D;
            double f = (i       & 255) / 255.0D;

            for(int j = 0; j < amount; ++j) {
                this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), d, e, f);
            }
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, this.duration, 0), this.getEffectCause());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Duration", this.duration);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.duration = nbt.getInt("Duration");
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(DirebatsItems.DIREBAT_FANG_ARROW);
    }
}
