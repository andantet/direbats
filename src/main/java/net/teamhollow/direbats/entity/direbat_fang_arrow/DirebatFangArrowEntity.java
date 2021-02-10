package net.teamhollow.direbats.entity.direbat_fang_arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.teamhollow.direbats.init.DBEntities;
import net.teamhollow.direbats.init.DBItems;
import net.teamhollow.direbats.item.DirebatFangArrowItem;

public class DirebatFangArrowEntity extends AbstractArrowEntity {
    public static final String id = DirebatFangArrowItem.id;

    private int duration = 300;

    public DirebatFangArrowEntity(EntityType<? extends DirebatFangArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public DirebatFangArrowEntity(World world, LivingEntity owner) {
        super(DBEntities.DIREBAT_FANG_ARROW, owner, world);
    }

    public DirebatFangArrowEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(DBEntities.DIREBAT_FANG_ARROW, world);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        if (result.getEntity() instanceof LivingEntity) {
            ((LivingEntity) result.getEntity()).addPotionEffect(new EffectInstance(Effects.BLINDNESS, this.duration));
        }

        super.onEntityHit(result);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(DBItems.DIREBAT_FANG_ARROW);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        tag.putInt("Damage", this.duration);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        this.duration = tag.getInt("Damage");
    }
}
