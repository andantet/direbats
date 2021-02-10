package net.teamhollow.direbats.entity.direbat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.teamhollow.direbats.init.DBGamerules;
import net.teamhollow.direbats.init.DBSoundEvents;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class DirebatEntity extends PathAwareEntity {
    public static final String id = "direbat";

    private static final TrackedData<Boolean> HANGING = DataTracker.registerData(DirebatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE = new TargetPredicate()
        .setBaseMaxDistance(4.0D)
        .includeTeammates()
        .setPredicate(livingEntity -> !livingEntity.isSneaking());

    private int eatingTime;

    private static final Predicate<ItemEntity> PICKABLE_DROP_FILTER = item -> item != null && !item.cannotPickup();

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
        this.targetSelector.add(0, new RevengeGoal(this));
        this.goalSelector.add(1, new DirebatEntity.AttackGoal(this));
        this.targetSelector.add(1, new DirebatEntity.TargetGoal<>(this, PlayerEntity.class));
        this.goalSelector.add(2, new DirebatEntity.PickupItemGoal());
        this.goalSelector.add(3, new WanderGoal());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HANGING, false);
    }

    @Override
    protected EntityNavigation createNavigation(World worldIn) {
        BirdNavigation nav = new BirdNavigation(this, worldIn) {
            @Override
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        nav.setCanEnterOpenDoors(false);
        nav.setCanSwim(false);
        nav.setCanPathThroughDoors(true);
        return nav;
    }

    @Override
    public Vec3d method_26318(Vec3d vec3d, float f) {
        this.updateVelocity(this.getRelevantMoveFactor(f), vec3d);
        // this.setVelocity(this.applyClimbingSpeed(this.getVelocity()));
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
        Block block = world.getBlockState(pos).getBlock();
        return (block == Blocks.AIR || block == Blocks.CAVE_AIR || block == Blocks.VOID_AIR) ? 10.0F * (1F / world.getDimension().method_28516(world.getLightLevel(pos))) : 0.0F;
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
        return this.isHanging() && this.random.nextInt(4) != 0 ? null : DBSoundEvents.ENTITY_DIREBAT_AMBIENT;
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
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {}

    @Override
    protected void tickCramming() {}

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPos) {}

    public boolean isHanging() {
        return this.dataTracker.get(HANGING);
    }
    public void setHanging(boolean hanging) {
        this.dataTracker.set(HANGING, hanging);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isHanging()) {
            this.setVelocity(Vec3d.ZERO);
        } else {
            this.setVelocity(this.getVelocity().multiply(1.05));
        }
    }

    @Override
    public void tickMovement() {
        boolean isInDaylight = this.isAffectedByDaylight();
        if (isInDaylight) {
            ItemStack itemstack = this.getEquippedStack(EquipmentSlot.HEAD);
            if (!itemstack.isEmpty()) {
                if (itemstack.isDamageable()) {
                    itemstack.setDamage(itemstack.getDamage() + this.random.nextInt(2));
                    if (itemstack.getDamage() >= itemstack.getMaxDamage()) {
                        this.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
                        this.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    }
                }

                isInDaylight = false;
            }

            if (isInDaylight) {
                this.setFireTicks(8);
            }
        }

        super.tickMovement();
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily()) {
            this.updateVelocity(0.1F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9D));
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPosUp = blockPos.up();
        if (this.isHanging()) {
            boolean isSilent = this.isSilent();

            ItemStack mainhandStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (!mainhandStack.isEmpty() && mainhandStack.isFood()) {
                this.eatingTime++;

                if (this.eatingTime > 600) {
                    ItemStack newStack = mainhandStack.finishUsing(this.world, this);
                    if (!newStack.isEmpty()) this.equipStack(EquipmentSlot.MAINHAND, newStack);

                    this.eatingTime = 0;
                } else if (this.eatingTime > 560 && this.random.nextFloat() < 0.3F) {
                    this.playSound(this.getEatSound(mainhandStack), 1.0F, 1.0F);
                    this.spawnItemParticles(mainhandStack, 10);
                    this.world.sendEntityStatus(this, (byte) 45);
                }
            }

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
                        this.world.syncWorldEvent(null, 1025, blockPos, 0);
                    }
                }
            } else {
                this.setHanging(false);
                if (!isSilent) {
                    this.world.syncWorldEvent(null, 1025, blockPos, 0);
                }
            }

            this.setVelocity(0, 0, 0);
            this.getNavigation().stop();
        } else {
            if (this.getTarget() == null && this.random.nextInt(20) == 0 && this.world.getBlockState(blockPosUp).isFullCube(this.world, blockPosUp) && this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) == null) {
                this.setHanging(true);
            }
        }
    }

    private void spawnItemParticles(ItemStack itemStack, int count) {
        for (int i = 0; i < count; ++i) {
            Vec3d vec3d = new Vec3d(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3d = vec3d.rotateY(-this.pitch * 0.017453292F);
            vec3d = vec3d.rotateZ(-this.yaw * 0.017453292F);
            double d = (double) (-this.random.nextFloat()) * 0.6D - 0.3D;
            Vec3d vec3d2 = new Vec3d(((double) this.random.nextFloat() - 0.5D) * 0.3D, d, 0.6D);
            vec3d2 = vec3d2.rotateY(-this.pitch * 0.017453292F);
            vec3d2 = vec3d2.rotateZ(-this.yaw * 0.017453292F);
            vec3d2 = vec3d2.add(this.getX(), this.getEyeY(), this.getZ());
            this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack), vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (super.tryAttack(target)) {
            if (target instanceof LivingEntity) {
                int seconds = 5;

                if (this.world.getDifficulty() == Difficulty.HARD) {
                    seconds = 10;
                }

                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, seconds * 20, 0));
            }
            this.playSound(DBSoundEvents.ENTITY_DIREBAT_ATTACK, 1.0F, 1.0F);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean damage(DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            if (!this.world.isClient && this.isHanging()) {
                this.setHanging(false);
            }

            this.dropInventory();

            return super.damage(damageSource, amount);
        }
    }

    @Override
    protected void loot(ItemEntity itemEntity) {
        ItemStack newItem = itemEntity.getStack();
        ItemStack currentItem = this.getEquippedStack(EquipmentSlot.MAINHAND);

        if (currentItem.isEmpty() && this.world.getGameRules().getBoolean(DBGamerules.DIREBAT_ITEM_PICKUP) && PICKABLE_DROP_FILTER.test(itemEntity)) {
            this.equipLootStack(EquipmentSlot.MAINHAND, newItem); // insert new item into main hand
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
            this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.AIR));
        }
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.dataTracker.set(HANGING, tag.getBoolean("Hanging"));
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putBoolean("Hanging", this.dataTracker.get(HANGING));
    }

    @SuppressWarnings({"deprecation"})
    public static boolean canSpawn(EntityType<DirebatEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (pos.getY() >= world.getSeaLevel()) {
            return world.toServerWorld().isNight() && world.getMoonSize() == 1.0F;
        } else {
            int worldLight = world.getLightLevel(pos);
            int maximumLight = 4;
            if (isTodayAroundHalloween()) {
                maximumLight = 7;
            }

            return worldLight <= random.nextInt(maximumLight) && canMobSpawn(type, world, spawnReason, pos, random);
        }
    }
    private static boolean isTodayAroundHalloween() {
        LocalDate localDate = LocalDate.now();
        int day = localDate.get(ChronoField.DAY_OF_MONTH);
        int month = localDate.get(ChronoField.MONTH_OF_YEAR);
        return month == 10 && day >= 20 || month == 11 && day <= 3;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return this.getMainHandStack().isEmpty();
    }

    @Override
    protected float getActiveEyeHeight(EntityPose entityPose, EntityDimensions dimensions) {
        return dimensions.height / 2;
    }

    private boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), distance);
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(DirebatEntity entity) {
            super(entity, 1.0D, true);
        }

        @Override
        public boolean canStart() {
            return super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            float mobBrightness = this.mob.getBrightnessAtEyes();
            if (mobBrightness <= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
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

        @Override
        public boolean canStart() {
            return DirebatEntity.this.navigation.isIdle() && !DirebatEntity.this.isHanging() && DirebatEntity.this.random.nextInt(5) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return DirebatEntity.this.navigation.isFollowingPath();
        }

        @Override
        public void start() {
            Vec3d randomLocation = this.getRandomLocation();
            if (randomLocation != null) {
                DirebatEntity.this.navigation.startMovingAlong(DirebatEntity.this.navigation.findPathTo(new BlockPos(randomLocation), 1), 1.0D);
            }

        }

        private Vec3d getRandomLocation() {
            Vec3d vector3d;
            if (DirebatEntity.this.hasPositionTarget() && !DirebatEntity.this.isWithinDistance(DirebatEntity.this.getPositionTarget(), 22)) {
                Vec3d vector3d1 = Vec3d.ofCenter(DirebatEntity.this.getPositionTarget());
                vector3d = vector3d1.subtract(DirebatEntity.this.getPos()).normalize();
            } else {
                vector3d = DirebatEntity.this.getRotationVec(0.0F);
            }

            Vec3d vector3d2 = TargetFinder.findAirTarget(DirebatEntity.this, 8, 7, vector3d, ((float) Math.PI / 2F), 2, 1);
            return vector3d2 != null ? vector3d2 : TargetFinder.findGroundTarget(DirebatEntity.this, 8, 4, -2, vector3d, (float) Math.PI / 2F);
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
            } else if (DirebatEntity.this.getTarget() == null) {
                List<ItemEntity> list = DirebatEntity.this.world.getEntitiesByClass(ItemEntity.class, DirebatEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
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
            List<ItemEntity> list = DirebatEntity.this.world.getEntitiesByClass(ItemEntity.class, DirebatEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
            ItemStack itemInHand = DirebatEntity.this.getStackInHand(Hand.MAIN_HAND);
            if (itemInHand.isEmpty() && !list.isEmpty()) {
                DirebatEntity.this.getNavigation().startMovingTo(list.get(0), 1.2000000476837158D);
            }
        }

        @Override
        public void start() {
            List<ItemEntity> list = DirebatEntity.this.world.getEntitiesByClass(ItemEntity.class, DirebatEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
            if (!list.isEmpty()) {
                DirebatEntity.this.getNavigation().startMovingTo(list.get(0), 1.2000000476837158D);
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
            // get nearby non-sneaking player and set target
            return !(goalOwnerBrightness <= 0.5F) && super.canStart() && !this.targetEntity.isSneaking();
        }
    }
}
