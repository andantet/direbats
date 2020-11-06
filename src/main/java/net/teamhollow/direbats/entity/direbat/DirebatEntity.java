package net.teamhollow.direbats.entity.direbat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.teamhollow.direbats.init.DBSoundEvents;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class DirebatEntity extends CreatureEntity {
    public static final String id = "direbat";

    private static final DataParameter<Boolean> HANGING = EntityDataManager.createKey(DirebatEntity.class, DataSerializers.BOOLEAN);
    private static final EntityPredicate CLOSE_PLAYER_PREDICATE = (new EntityPredicate()).setDistance(4.0D).allowFriendlyFire().setCustomPredicate(new Predicate<LivingEntity>() {
        @Override
        public boolean test(LivingEntity livingEntity) {
            return !livingEntity.isSneaking();
        }
    });

    private int eatingTime;

    private static final Predicate<ItemEntity> PICKABLE_DROP_FILTER = new Predicate<ItemEntity>() {
        public boolean test(@Nullable ItemEntity item) {
            return item != null && !item.cannotPickup();
        }
    };

    public DirebatEntity(EntityType<? extends DirebatEntity> entityType, World world) {
        super(entityType, world);
        this.moveController = new FlyingMovementController(this, 20, true);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathPriority(PathNodeType.STICKY_HONEY, -1.0F);
        this.setPathPriority(PathNodeType.COCOA, -1.0F);
        this.setPathPriority(PathNodeType.FENCE, -1.0F);

        this.setCanPickUpLoot(true);
        this.experienceValue = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AttackGoal(this));
        this.goalSelector.addGoal(2, new DirebatEntity.PickupItemGoal());
        this.goalSelector.addGoal(3, new WanderGoal());
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new TargetGoal<>(this, PlayerEntity.class));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(HANGING, false);
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 16.0D)
                .createMutableAttribute(Attributes.FLYING_SPEED, 0.22D) // previously (double) 0.22F
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.22D) // previously (double) 0.22F
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator nav = new FlyingPathNavigator(this, worldIn) {
            public boolean canEntityStandOnPos(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        nav.setCanOpenDoors(false);
        nav.setCanSwim(false);
        nav.setCanEnterDoors(true);
        return nav;
    }

    @Override
    public Vector3d func_233633_a_(Vector3d p_233633_1_, float p_233633_2_) {
        this.moveRelative(this.getRelevantMoveFactor(p_233633_2_), p_233633_1_);
        //this.setMotion(this.handleOnClimbable(this.getMotion()));
        this.move(MoverType.SELF, this.getMotion());
        Vector3d motionVector = this.getMotion();
        if ((this.collidedHorizontally || this.isJumping) && this.isOnLadder()) {
            motionVector = new Vector3d(motionVector.x, 0.2D, motionVector.z);
        }

        return motionVector;
    }

    private float getRelevantMoveFactor(float slipperiness) {
        return this.getAIMoveSpeed() * (0.20600002F / (slipperiness * slipperiness * slipperiness));
    }

    @Override
    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
        Block block = worldIn.getBlockState(pos).getBlock();
        return (block == Blocks.AIR || block == Blocks.CAVE_AIR || block == Blocks.VOID_AIR) ? 10.0F * (1F / worldIn.getDimensionType().getAmbientLight(worldIn.getLight(pos))) : 0.0F;
    }

    @Override
    protected float getSoundVolume() {
        return 0.15F;
    }

    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.95F;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return this.isHanging() && this.rand.nextInt(4) != 0 ? null : DBSoundEvents.ENTITY_DIREBAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return DBSoundEvents.ENTITY_DIREBAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return DBSoundEvents.ENTITY_DIREBAT_DEATH;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean isHanging() {
        return this.dataManager.get(HANGING);
    }

    public void setHanging(boolean hanging) {
        this.dataManager.set(HANGING, hanging);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isHanging()) {
            this.setMotion(Vector3d.ZERO);
        }
    }

    @Override
    public void travel(Vector3d travelVector) {
        if (this.isServerWorld()) {
            this.moveRelative(0.1F, travelVector);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.9D));
        }
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        BlockPos blockPos = this.getPosition();
        BlockPos blockPosUp = blockPos.up();
        if (this.isHanging()) {
            boolean isSilent = this.isSilent();

            ItemStack mainhandStack = this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            if (!mainhandStack.isEmpty() && mainhandStack.isFood()) {
                this.eatingTime++;

                if (this.eatingTime > 600) {
                    ItemStack newStack = mainhandStack.onItemUseFinish(this.world, this);
                    if (!newStack.isEmpty()) this.setItemStackToSlot(EquipmentSlotType.MAINHAND, newStack);

                    this.eatingTime = 0;
                } else if (this.eatingTime > 560 && this.rand.nextFloat() < 0.3F) {
                    this.playSound(this.getEatSound(mainhandStack), 1.0F, 1.0F);
                    this.spawnItemParticles(mainhandStack, 10);
                    this.world.setEntityState(this, (byte) 45);
                }
            }

            if (this.getAttackTarget() != null) {
                this.setHanging(false);
            }

            if (this.world.getBlockState(blockPosUp).isNormalCube(this.world, blockPos)) {
                if (this.rand.nextInt(200) == 0) {
                    this.rotationYawHead = (float) this.rand.nextInt(360);
                }

                if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
                    this.setHanging(false);
                    if (!isSilent) {
                        this.world.playEvent((PlayerEntity) null, 1025, blockPos, 0);
                    }
                }
            } else {
                this.setHanging(false);
                if (!isSilent) {
                    this.world.playEvent((PlayerEntity) null, 1025, blockPos, 0);
                }
            }

            this.setMotion(0, 0, 0);
            this.getNavigator().clearPath();
        } else {
            if (this.rand.nextInt(20) == 0 && this.world.getBlockState(blockPosUp).isNormalCube(this.world, blockPosUp) && this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) == null) {
                this.setHanging(true);
            }
        }
    }

    private void spawnItemParticles(ItemStack stack, int count) {
        for (int i = 0; i < count; ++i) {
            Vector3d vec3d = new Vector3d(((double) this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
            vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
            double d = (double) (-this.rand.nextFloat()) * 0.6D - 0.3D;
            Vector3d vec3d2 = new Vector3d(((double) this.rand.nextFloat() - 0.5D) * 0.3D, d, 0.6D);
            vec3d2 = vec3d2.rotatePitch(-this.rotationPitch * 0.017453292F);
            vec3d2 = vec3d2.rotateYaw(-this.rotationYaw * 0.017453292F);
            vec3d2 = vec3d2.add(this.getPosX(), this.getPosYEye(), this.getPosZ());
            this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (super.attackEntityAsMob(entityIn)) {
            if (entityIn instanceof LivingEntity) {
                int seconds = 5;

                if (this.world.getDifficulty() == Difficulty.HARD) {
                    seconds = 10;
                }

                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.BLINDNESS, seconds * 20, 0));
            }
            this.playSound(DBSoundEvents.ENTITY_DIREBAT_ATTACK, 1.0F, 1.0F);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.world.isRemote && this.isHanging()) {
                this.setHanging(false);
            }

            this.dropInventory();

            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    protected void updateEquipmentIfNeeded(ItemEntity itemEntity) {
        ItemStack newItem = itemEntity.getItem();
        ItemStack currentItem = this.getItemStackFromSlot(EquipmentSlotType.MAINHAND);

        if (currentItem.isEmpty() && PICKABLE_DROP_FILTER.test(itemEntity)) {
            this.func_233657_b_(EquipmentSlotType.MAINHAND, newItem); // insert new item into main hand
            this.triggerItemPickupTrigger(itemEntity);
            this.onItemPickup(itemEntity, newItem.getCount());
            itemEntity.remove();
        }
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (!this.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            this.entityDropItem(this.getHeldItemMainhand());
            this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.AIR));
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(HANGING, compound.getBoolean("Hanging"));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Hanging", this.dataManager.get(HANGING));
    }

    public static boolean canSpawn(EntityType<DirebatEntity> type, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (pos.getY() >= 63 && world.getMoonFactor() < 1.0F) {
            return false;
        } else {
            int worldLight = world.getLight(pos);
            int maximumLight = 4;
            if (isTodayAroundHalloween()) {
                maximumLight = 7;
            } else if (random.nextBoolean()) {
                return false;
            }

            return worldLight > random.nextInt(maximumLight)
                    ? false : canSpawnOn(type, world, spawnReason, pos, random);
        }
    }

    private static boolean isTodayAroundHalloween() {
        LocalDate localDate = LocalDate.now();
        int day = localDate.get(ChronoField.DAY_OF_MONTH);
        int month = localDate.get(ChronoField.MONTH_OF_YEAR);
        return month == 10 && day >= 20 || month == 11 && day <= 3;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height / 2;
    }

    private boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.withinDistance(this.getPosition(), (double) distance);
    }

    @Override
    protected boolean isDespawnPeaceful() {
        return true;
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(DirebatEntity entity) {
            super(entity, 1.0D, true);
        }

        public boolean shouldExecute() {
            return super.shouldExecute();
        }

        public boolean shouldContinueExecuting() {
            float attackerBrightness = this.attacker.getBrightness();
            if (attackerBrightness <= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget((LivingEntity) null);
                return false;
            } else {
                return super.shouldContinueExecuting();
            }
        }
    }

    class WanderGoal extends Goal {
        WanderGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return DirebatEntity.this.navigator.noPath() && !DirebatEntity.this.isHanging() && DirebatEntity.this.rand.nextInt(5) == 0;
        }

        public boolean shouldContinueExecuting() {
            return DirebatEntity.this.navigator.hasPath();
        }

        public void startExecuting() {
            Vector3d randomLocation = this.getRandomLocation();
            if (randomLocation != null) {
                DirebatEntity.this.navigator.setPath(DirebatEntity.this.navigator.getPathToPos(new BlockPos(randomLocation), 1), 1.0D);
            }

        }

        @Nullable
        private Vector3d getRandomLocation() {
            Vector3d vector3d;
            if (DirebatEntity.this.detachHome() && !DirebatEntity.this.isWithinDistance(DirebatEntity.this.getHomePosition(), 22)) {
                Vector3d vector3d1 = Vector3d.copyCentered(DirebatEntity.this.getHomePosition());
                vector3d = vector3d1.subtract(DirebatEntity.this.getPositionVec()).normalize();
            } else {
                vector3d = DirebatEntity.this.getLook(0.0F);
            }

            Vector3d vector3d2 = RandomPositionGenerator.findAirTarget(DirebatEntity.this, 8, 7, vector3d, ((float) Math.PI / 2F), 2, 1);
            return vector3d2 != null ? vector3d2 : RandomPositionGenerator.findGroundTarget(DirebatEntity.this, 8, 4, -2, vector3d, (double) ((float) Math.PI / 2F));
        }
    }

    class PickupItemGoal extends Goal {
        public PickupItemGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            if (!DirebatEntity.this.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
                return false;
            } else if (DirebatEntity.this.getAttackTarget() == null && DirebatEntity.this.getAttackingEntity() == null) {
                List<ItemEntity> list = DirebatEntity.this.world.getEntitiesWithinAABB(ItemEntity.class, DirebatEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
                return !list.isEmpty() && DirebatEntity.this.getHeldItem(Hand.MAIN_HAND).isEmpty();
            } else {
                return false;
            }
        }

        public boolean shouldContinueExecuting() {
            return DirebatEntity.this.navigator.hasPath();
        }

        public void tick() {
            List<ItemEntity> list = DirebatEntity.this.world.getEntitiesWithinAABB(ItemEntity.class, DirebatEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
            ItemStack itemInHand = DirebatEntity.this.getHeldItem(Hand.MAIN_HAND);
            if (itemInHand.isEmpty() && !list.isEmpty()) {
                DirebatEntity.this.getNavigator().tryMoveToEntityLiving((Entity) list.get(0), 1.2000000476837158D);
            }
        }

        @Override
        public void startExecuting() {
            List<ItemEntity> list = DirebatEntity.this.world.getEntitiesWithinAABB(ItemEntity.class, DirebatEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
            if (!list.isEmpty()) {
                DirebatEntity.this.getNavigator().tryMoveToEntityLiving((Entity) list.get(0), 1.2000000476837158D);
            }

            DirebatEntity.this.setHanging(false);
        }
    }

    static class TargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public TargetGoal(DirebatEntity entity, Class<T> classTarget) {
            super(entity, classTarget, true);
        }

        public boolean shouldExecute() {
            float goalOwnerBrightness = this.goalOwner.getBrightness();
            // get nearby non-sneaking player and set target
            return goalOwnerBrightness <= 0.5F ? false : super.shouldExecute() && !this.nearestTarget.isSneaking();
        }
    }
}
