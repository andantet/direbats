package net.teamhollow.direbats.entity.direbat_fang_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import net.teamhollow.direbats.init.DBEntities;
import net.teamhollow.direbats.init.DBItems;
import net.teamhollow.direbats.item.DirebatFangArrowItem;

public class DirebatFangArrowEntity extends PersistentProjectileEntity {
    public static final String id = DirebatFangArrowItem.id;

    private int duration = 300;

    public DirebatFangArrowEntity(EntityType<? extends DirebatFangArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public DirebatFangArrowEntity(World world, LivingEntity owner) {
        super(DBEntities.DIREBAT_FANG_ARROW, owner, world);
    }

    public DirebatFangArrowEntity(World world, double x, double y, double z) {
        super(DBEntities.DIREBAT_FANG_ARROW, x, y, z, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(DBItems.DIREBAT_FANG_ARROW);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);

        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.BLINDNESS, this.duration, 0);
        target.addStatusEffect(statusEffectInstance);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        if (tag.contains("Duration")) this.duration = tag.getInt("Duration");
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        tag.putInt("Duration", this.duration);
    }
}
