package teamhollow.direbats.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import teamhollow.direbats.init.ModEntities;
import teamhollow.direbats.init.ModItems;

public class FangArrowEntity extends AbstractArrowEntity {

    public FangArrowEntity(EntityType<? extends FangArrowEntity> entityType, World world) {
        super(entityType, world);
        this.setDamage(30f);
    }

    public FangArrowEntity(World world, double x, double y, double z) {
        super(ModEntities.FANGARROW, x, y, z, world);
    }

    public FangArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.FANGARROW, owner, world);
    }

    public FangArrowEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(ModEntities.FANGARROW, world);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        if (p_213868_1_.getEntity() != null && p_213868_1_.getEntity() instanceof LivingEntity) {
            ((LivingEntity) p_213868_1_.getEntity()).addPotionEffect(new EffectInstance(Effects.BLINDNESS, 300));
        }

        super.onEntityHit(p_213868_1_);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.FANGARROW);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
