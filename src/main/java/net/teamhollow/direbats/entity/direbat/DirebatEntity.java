package net.teamhollow.direbats.entity.direbat;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class DirebatEntity extends PathAwareEntity {
    public static final String id = "direbat";
    public static final EntityType.Builder<DirebatEntity> builder = EntityType.Builder
        .create(DirebatEntity::new, SpawnGroup.CREATURE)
        .setDimensions(0.75F, 0.75F)
        .maxTrackingRange(5);
    public static final int[] spawnEggColors = { 5065037, 9433559 };

    private static final TrackedData<Byte> DIREBAT_FLAGS = DataTracker.registerData(DirebatEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE = new TargetPredicate().setBaseMaxDistance(4.0D).includeTeammates();

    private static final Predicate<ItemEntity> PICKABLE_DROP_FILTER = new Predicate<ItemEntity>() {
        public boolean test(ItemEntity itemEntity) {
            return itemEntity != null && !itemEntity.cannotPickup();
        }
    };

    public DirebatEntity(EntityType<? extends DirebatEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.STICKY_HONEY, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);

        this.setCanPickUpLoot(true);
        this.experiencePoints = 5;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AttackGoal(this));
        this.goalSelector.add(2, new DirebatEntity.PickupItemGoal());
        this.goalSelector.add(3, new WanderGoal());
        this.targetSelector.add(0, new RevengeGoal(this));
        this.targetSelector.add(1, new TargetGoal<PlayerEntity>(this, PlayerEntity.class));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DIREBAT_FLAGS, (byte) 0);
    }

    public static DefaultAttributeContainer.Builder createDirebatAttributes() {
        return MobEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
            .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.22D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22D)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation flyingpathnavigator = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        flyingpathnavigator.setCanPathThroughDoors(false);
        flyingpathnavigator.setCanSwim(false);
        flyingpathnavigator.setCanEnterOpenDoors(true);
        return flyingpathnavigator;
    }

    @Override
    public Vec3d method_26318(Vec3d vec3d, float f) {
        this.updateVelocity(this.getRelevantMoveFactor(f), vec3d);
        // this.setMotion(this.handleOnClimbable(this.getVelocity()));
        this.move(MovementType.SELF, this.getVelocity());
        Vec3d motionVector = this.getVelocity();
        if ((this.horizontalCollision || this.jumping) && this.isHoldingOntoLadder()) {
            motionVector = new Vec3d(motionVector.x, 0.2D, motionVector.z);
        }

        return motionVector;
    }

    private float getRelevantMoveFactor(float slipperiness) {
        return this.getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness));
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F * (1F / world.getBrightness(pos)) : 0.0F;
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
        return this.isHanging() && this.random.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_BAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BAT_DEATH;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    protected void tickCramming() {}

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {}

    public boolean isHanging() {
        return ((Byte) this.dataTracker.get(DIREBAT_FLAGS) & 1) != 0;
    }

    public void setHanging(boolean roosting) {
        byte batFlag = (Byte) this.dataTracker.get(DIREBAT_FLAGS);
        if (roosting) {
            this.dataTracker.set(DIREBAT_FLAGS, (byte) (batFlag | 1));
        } else {
            this.dataTracker.set(DIREBAT_FLAGS, (byte) (batFlag & -2));
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.isHanging()) {
            this.setVelocity(Vec3d.ZERO);
            this.updatePosition(this.getX(), (double) MathHelper.floor(this.getY()) + 1.0D - (double) this.getHeight(), this.getZ());
        } else {
            this.setVelocity(this.getVelocity().multiply(1.0D, 0.6D, 1.0D));
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPosUp = blockPos.up();
        if (this.isHanging()) {
            boolean isSilent = this.isSilent();

            if (this.getTarget() != null) {
                this.setHanging(false);
            }

            if (this.world.getBlockState(blockPosUp).isFullCube(this.world, blockPos)) {
                if (this.random.nextInt(200) == 0) {
                    this.headYaw = (float) this.random.nextInt(360);
                }

                if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
                    this.setHanging(false);
                    if (!isSilent) {
                        this.world.syncWorldEvent((PlayerEntity) null, 1025, blockPos, 0);
                    }
                }
            } else {
                this.setHanging(false);
                if (!isSilent) {
                    this.world.syncWorldEvent((PlayerEntity) null, 1025, blockPos, 0);
                }
            }

            this.setVelocity(0, 0, 0);
            this.getNavigation().stop();
        } else {
            if (this.random.nextInt(100) == 0 && this.world.getBlockState(blockPosUp).isFullCube(this.world, blockPosUp)) {
                this.setHanging(true);
            }
        }
    }

    @Override
    public void setTarget(LivingEntity livingEntity) {
        super.setTarget(livingEntity);

        ItemStack currentItem = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!currentItem.isEmpty()) {
            this.dropStack(currentItem);
            this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.AIR));
        }
    }

    @Override
    public boolean tryAttack(Entity entity) {
        if (super.tryAttack(entity)) {
            if (entity instanceof LivingEntity) {
                int seconds = 5;

                if (this.world.getDifficulty() == Difficulty.HARD) {
                    seconds = 10;
                }

                ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, seconds * 20, 0));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.world.isClient && this.isHanging()) {
                this.setHanging(false);
            }

            ItemStack currentItem = this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (!currentItem.isEmpty()) {
                this.dropStack(currentItem);
                this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.AIR));
            }

            return super.damage(source, amount);
        }
    }

    @Override
    protected void loot(ItemEntity itemEntity) {
        ItemStack newItem = itemEntity.getStack();

        if (PICKABLE_DROP_FILTER.test(itemEntity)) {
            ItemStack currentItem = this.getEquippedStack(EquipmentSlot.MAINHAND);

            if (!currentItem.isEmpty()) {
                this.dropStack(currentItem);
            }

            this.equipLootStack(EquipmentSlot.MAINHAND, newItem);
            this.method_29499(itemEntity);
            this.sendPickup(itemEntity, newItem.getCount());
            itemEntity.remove();
        }
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (!this.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            this.dropStack(this.getMainHandStack());
        }
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compound) {
        super.readCustomDataFromTag(compound);
        this.dataTracker.set(DIREBAT_FLAGS, compound.getByte("DirebatFlags"));
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compound) {
        super.writeCustomDataToTag(compound);
        compound.putByte("DirebatFlags", (Byte) this.dataTracker.get(DIREBAT_FLAGS));
    }

    public static boolean canSpawn(EntityType<DirebatEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (pos.getY() >= world.getSeaLevel() && world.getMoonPhase() < 1.0F) {
            return false;
        } else {
            int worldLight = world.getLightLevel(pos);
            int maximumLight = 4;
            if (isTodayAroundHalloween()) {
                maximumLight = 7;
            } else if (random.nextBoolean()) {
                return false;
            }

            return worldLight > random.nextInt(maximumLight)
                    ? false : canMobSpawn(type, world, spawnReason, pos, random);
        }
    }

    private static boolean isTodayAroundHalloween() {
        LocalDate localDate = LocalDate.now();
        int day = localDate.get(ChronoField.DAY_OF_MONTH);
        int month = localDate.get(ChronoField.MONTH_OF_YEAR);
        return month == 10 && day >= 20 || month == 11 && day <= 3;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height / 2;
    }

    private boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), (double) distance);
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(DirebatEntity entity) {
            super(entity, 1.0D, true);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canStart() {
            return super.canStart();
        }

        public boolean shouldContinue() {
            float attackerBrightness = this.mob.getBrightnessAtEyes();
            if (attackerBrightness <= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget((LivingEntity) null);
                return false;
            } else {
                return super.shouldContinue();
            }
        }
    }

    class WanderGoal extends Goal {
        WanderGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canStart() {
            return DirebatEntity.this.navigation.isIdle() && !DirebatEntity.this.isHanging() && DirebatEntity.this.random.nextInt(5) == 0;
        }

        public boolean shouldContinue() {
            return DirebatEntity.this.navigation.isFollowingPath();
        }

        public void start() {
            Vec3d randomLocation = this.getRandomLocation();
            if (randomLocation != null) {
                DirebatEntity.this.navigation.startMovingAlong(DirebatEntity.this.navigation.findPathTo(new BlockPos(randomLocation), 1), 1.0D);
            }

        }

        private Vec3d getRandomLocation() {
            Vec3d vec3d1;
            if (DirebatEntity.this.hasPositionTarget() && !DirebatEntity.this.isWithinDistance(DirebatEntity.this.getPositionTarget(), 22)) {
                Vec3d vec3d2 = Vec3d.ofCenter(DirebatEntity.this.getPositionTarget());
                vec3d1 = vec3d2.subtract(DirebatEntity.this.getPos()).normalize();
            } else {
                vec3d1 = DirebatEntity.this.getRotationVec(0.0F);
            }

            Vec3d Vec3d2 = TargetFinder.findAirTarget(DirebatEntity.this, 8, 7, vec3d1, ((float) Math.PI / 2F), 2, 1);
            return Vec3d2 != null ? Vec3d2 : TargetFinder.findGroundTarget(DirebatEntity.this, 8, 4, -2, vec3d1, (double) ((float) Math.PI / 2F));
        }
    }

    class PickupItemGoal extends Goal {
        public PickupItemGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (!DirebatEntity.this.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
                return false;
            } else if (DirebatEntity.this.getTarget() == null && DirebatEntity.this.getAttacker() == null) {
                List<ItemEntity> list = DirebatEntity.this.world.getEntities(ItemEntity.class, DirebatEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
                return !list.isEmpty() && DirebatEntity.this.getStackInHand(Hand.MAIN_HAND).isEmpty();
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinue() {
            return DirebatEntity.this.navigation.isFollowingPath();
        }

        @Override
        public void tick() {
            List<ItemEntity> list = DirebatEntity.this.world.getEntities(ItemEntity.class, DirebatEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
            ItemStack itemInHand = DirebatEntity.this.getStackInHand(Hand.MAIN_HAND);
            if (itemInHand.isEmpty() && !list.isEmpty()) {
                DirebatEntity.this.getNavigation().startMovingTo((Entity) list.get(0), 1.2000000476837158D);
            }
        }

        @Override
        public void start() {
            List<ItemEntity> list = DirebatEntity.this.world.getEntities(ItemEntity.class, DirebatEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
            if (!list.isEmpty()) {
                DirebatEntity.this.getNavigation().startMovingTo((Entity) list.get(0), 1.2000000476837158D);
            }

            DirebatEntity.this.setHanging(false);
        }
    }

    static class TargetGoal<T extends LivingEntity> extends FollowTargetGoal<T> {
        public TargetGoal(DirebatEntity entity, Class<T> classTarget) {
            super(entity, classTarget, true);
        }

        @Override
        public boolean canStart() {
            float goalOwnerBrightness = this.mob.getBrightnessAtEyes();
            return goalOwnerBrightness <= 0.5F ? false : super.canStart();
        }
    }
}
