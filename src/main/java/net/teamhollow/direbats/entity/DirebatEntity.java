package net.teamhollow.direbats.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.teamhollow.direbats.mixin.LivingEntityInvoker;
import net.teamhollow.direbats.sound.DirebatsSoundEvents;
import net.teamhollow.direbats.world.DirebatsGameRules;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public class DirebatEntity extends PathAwareEntity {
    private static final Predicate<ItemEntity> PICKABLE_DROP_FILTER = i -> i != null && !i.cannotPickup();

    private static final TrackedData<Boolean> HANGING = DataTracker.registerData(DirebatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> EATING_TIME = DataTracker.registerData(DirebatEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private boolean avoidsFallingBlocks = false;
    private int hangingCooldown = 0;
    private int pickupCooldown = 0;

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
        this.targetSelector.add(1, new DirebatTargetGoal<>(this, PlayerEntity.class));

        this.goalSelector.add(1, new DirebatEntity.AttackGoal(this));
        this.goalSelector.add(2, new PickupItemGoal(this));
        this.goalSelector.add(3, new WanderGoal(this));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HANGING, false);
        this.dataTracker.startTracking(EATING_TIME, -1);
    }

    public boolean isHanging() {
        return this.dataTracker.get(HANGING);
    }

    public void setHanging(boolean hanging) {
        this.dataTracker.set(HANGING, hanging);
    }

    public int getEatingTime() {
        return this.dataTracker.get(EATING_TIME);
    }

    public void setEatingTime(int eatingTime) {
        this.dataTracker.set(EATING_TIME, eatingTime);
    }

    public static DefaultAttributeContainer.Builder createDirebatAttributes() {
        return MobEntity.createMobAttributes()
                        .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0D)
                        .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.22D)
                        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22D)
                        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose entityPose, EntityDimensions dimensions) {
        return dimensions.height / 2;
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
    public void tick() {
        super.tick();

        if (this.getY() <= this.world.getBottomY()) {
            Vec3d vel = this.getVelocity();
            this.setVelocity(vel.x, 0.2D, vel.z);
        } else {
            this.setVelocity(this.isHanging() ? Vec3d.ZERO : this.getVelocity().multiply(1.05D));
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.pickupCooldown > 0) this.pickupCooldown--;

        if (!this.world.isClient) {
            if (this.isHanging()) {
                if (!this.canHang()) {
                    this.setHanging(false);
                    Optional.ofNullable(this.getWakingPlayer()).ifPresent(this::setTarget);
                    if (!this.isSilent()) this.world.syncWorldEvent(null, WorldEvents.BAT_TAKES_OFF, this.getBlockPos(), 0);
                }

                ItemStack stack = this.getEquippedStack(EquipmentSlot.MAINHAND);
                if (!stack.isEmpty() && stack.isFood()) {
                    int eatingTime = this.getEatingTime();
                    this.setEatingTime(eatingTime + 1);
                    if (eatingTime > 600) {
                        ItemStack nu = stack.finishUsing(this.world, this);
                        if (!nu.isEmpty()) this.equipStack(EquipmentSlot.MAINHAND, nu);
                        this.setEatingTime(-1);
                    } else if (eatingTime > 560 && this.random.nextFloat() < 0.1F) {
                        this.playSound(this.getEatSound(stack), this.getSoundVolume(), this.getSoundPitch());
                        this.world.sendEntityStatus(this, EntityStatuses.CREATE_EATING_PARTICLES);
                    }
                }
            } else {
                if (this.hangingCooldown > 0) this.hangingCooldown--;

                if (this.world.random.nextFloat() <= 0.5F && this.canHang()) {
                    this.setHanging(true);

                    this.hangingCooldown = 20 * 10;

                    this.getNavigation().stop();

                    BlockPos hpos = this.getBlockPos().up();
                    BlockState hstate = this.world.getBlockState(hpos);
                    if (hstate.getBlock() instanceof FallingBlock) this.avoidsFallingBlocks = true;
                    hstate.updateNeighbors(this.world, hpos, Block.NOTIFY_ALL);
                }
            }
        }
    }

    public boolean canHang() {
        if (this.hangingCooldown > 0 && !this.isHanging()) return false;
        if (this.getTarget() != null || this.getWakingPlayer() != null) return false;
        return this.canHangAt(this.getBlockPos());
    }

    public boolean canHangAt(BlockPos pos) {
        BlockPos hpos = pos.up();
        BlockState hstate = this.world.getBlockState(hpos);
        if (this.avoidsFallingBlocks && hstate.getBlock() instanceof FallingBlock) return false;
        if (!hstate.isFullCube(this.world, hpos)) return false;

        BlockPos.Mutable mutable = pos.mutableCopy();
        for (int i = 0; i < 2; i++) {
            Box box = Box.of(Vec3d.of(mutable), 1.0d, 1.0d, 1.0d);

            if (!this.world.getEntitiesByClass(DirebatEntity.class, box, entity -> entity != this && entity.isHanging()).isEmpty()) return false;
            if (!this.world.getBlockState(mutable).getCollisionShape(this.world, mutable, ShapeContext.of(this)).isEmpty()) return false;

            mutable.move(Direction.DOWN);
        }

        return true;
    }

    public PlayerEntity getWakingPlayer() {
        return this.world.getClosestPlayer(this.getX(), this.getY(), this.getZ(), 8.0D, this::wakesUpDirebat);
    }

    public boolean wakesUpDirebat(Entity entity) {
        if (entity instanceof PlayerEntity player && (player.isCreative() || player.isSpectator())) return false;
        if (this.squaredDistanceTo(entity) <= 6.0D) return true;
        return !entity.isSneaking();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == EntityStatuses.CREATE_EATING_PARTICLES) {
            ItemStack stack = this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (!stack.isEmpty()) {
                for (int i = 0; i < 8; i++) {
                    Vec3d velocity = new Vec3d(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0)
                        .rotateX(-this.getPitch() * ((float)Math.PI / 180))
                        .rotateY(-this.getYaw() * ((float)Math.PI / 180));
                    this.world.addParticle(
                        new ItemStackParticleEffect(ParticleTypes.ITEM, stack),
                        this.getX() + this.getRotationVector().x / 2.0, this.getY(), this.getZ() + this.getRotationVector().z / 2.0,
                        velocity.x, velocity.y + 0.05, velocity.z
                    );
                }
            }
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public void tickMovement() {
        if (this.isAlive()) {
            boolean dl = this.isAffectedByDaylight();
            if (dl) {
                ItemStack stack = this.getEquippedStack(EquipmentSlot.HEAD);
                if (!stack.isEmpty()) {
                    if (stack.isDamageable()) {
                        stack.setDamage(stack.getDamage() + this.random.nextInt(2));
                        if (stack.getDamage() >= stack.getMaxDamage()) {
                            this.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
                            this.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    dl = false;
                }

                if (dl) {
                    this.setOnFireFor(8);
                }
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
    public Vec3d applyMovementInput(Vec3d input, float slipperiness) {
        this.updateVelocity(this.getRelevantMoveFactor(slipperiness), input);
        this.setVelocity(((LivingEntityInvoker) this).invoke_applyClimbingSpeed(this.getVelocity()));
        this.move(MovementType.SELF, this.getVelocity());
        Vec3d motionVector = this.getVelocity();
        if ((this.horizontalCollision || this.jumping) && (this.isClimbing() || this.getBlockStateAtPos().isOf(Blocks.POWDER_SNOW) && PowderSnowBlock.canWalkOnPowderSnow(this))) {
            motionVector = new Vec3d(motionVector.x, 0.2D, motionVector.z);
        }

        return motionVector;
    }

    private float getRelevantMoveFactor(float slipperiness) {
        return this.getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness));
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        if (world.getBlockState(pos).isAir()) {
            float offset = 0.0F;
            if (!world.getBlockState(pos.down()).isAir()) offset -= 2.0F;
            if (this.canHangAt(pos.up())) offset += 2.0F;

            float brightness = world.getDimension().getBrightness(world.getLightLevel(pos));
            return (1F / brightness) + offset;
        }

        return 0.0F;
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (super.tryAttack(target)) {
            if (target instanceof LivingEntity entity) entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, (this.world.getDifficulty() == Difficulty.HARD ? 10 : 5) * 20, 0));
            this.playSound(DirebatsSoundEvents.ENTITY_DIREBAT_ATTACK, 1.0F, 1.0F);
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
            if (!this.world.isClient && this.isHanging()) this.setHanging(false);
            this.dropInventory();
            return super.damage(damageSource, amount);
        }
    }

    @Override
    protected void loot(ItemEntity entity) {
        ItemStack stack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (stack.isEmpty() && this.world.getGameRules().getBoolean(DirebatsGameRules.DIREBAT_ITEM_PICKUP) && PICKABLE_DROP_FILTER.test(entity)) {
            ItemStack nu = entity.getStack();
            this.equipLootStack(EquipmentSlot.MAINHAND, nu);
            this.sendPickup(entity, nu.getCount());
            this.triggerItemPickedUpByEntityCriteria(entity);
            entity.discard();
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
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 0.95F;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return this.isHanging() && this.random.nextInt(4) != 0 ? null : DirebatsSoundEvents.ENTITY_DIREBAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return DirebatsSoundEvents.ENTITY_DIREBAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return DirebatsSoundEvents.ENTITY_DIREBAT_DEATH;
    }

    @Override
    public boolean isPushable() {
        return this.isHanging();
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }

    @Override
    protected void pushAway(Entity entity) {
        if (this.isHanging()) return;
        super.pushAway(entity);
    }

    @Override
    public boolean handleFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPos) {}

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Hanging", this.isHanging());
        nbt.putBoolean("AvoidsFallingBlocks", this.avoidsFallingBlocks);
        nbt.putInt("EatingTime", this.getEatingTime());
        nbt.putInt("HangingCooldown", this.hangingCooldown);
        nbt.putInt("PickupCooldown", this.pickupCooldown);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setHanging(nbt.getBoolean("Hanging"));
        this.avoidsFallingBlocks = nbt.getBoolean("AvoidsFallingBlocks");
        this.setEatingTime(nbt.getInt("EatingTime"));
        this.hangingCooldown = nbt.getInt("HangingCooldown");
        this.pickupCooldown = nbt.getInt("PickupCooldown");
    }

    public static boolean canSpawn(EntityType<DirebatEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (pos.getY() >= 63) {
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
    public boolean cannotDespawn() {
        return super.cannotDespawn() || !this.getMainHandStack().isEmpty();
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    public Optional<ItemEntity> getItemToPickUp() {
        List<ItemEntity> list = this.world.getEntitiesByClass(ItemEntity.class, this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), DirebatEntity.PICKABLE_DROP_FILTER);
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(0));
    }

    public static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(DirebatEntity entity) {
            super(entity, 1.0D, true);
        }

        @Override
        public boolean shouldContinue() {
            if (this.mob.getBrightnessAtEyes() <= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.shouldContinue();
            }
        }
    }

    public static class WanderGoal extends Goal {
        private final DirebatEntity mob;

        WanderGoal(DirebatEntity mob) {
            this.mob = mob;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return this.mob.navigation.isIdle() && !this.mob.isHanging() && this.mob.random.nextInt(5) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return this.mob.navigation.isFollowingPath();
        }

        @Override
        public void start() {
            Optional.ofNullable(this.getRandomLocation()).ifPresent(pos -> this.mob.navigation.startMovingAlong(this.mob.navigation.findPathTo(new BlockPos(pos), 1), 1.0D));
        }

        private Vec3d getRandomLocation() {
            Vec3d rotation;
            if (this.mob.hasPositionTarget() && !this.mob.getPositionTarget().isWithinDistance(this.mob.getPos(), 22)) {
                Vec3d pos = Vec3d.ofCenter(this.mob.getPositionTarget());
                rotation = pos.subtract(this.mob.getPos()).normalize();
            } else {
                rotation = this.mob.getRotationVec(0.0F);
            }

            return Optional
                .ofNullable(AboveGroundTargeting.find(this.mob, 8, 7, rotation.x, rotation.z, ((float) Math.PI / 2F), 2, 1))
                .orElseGet(() -> NoPenaltySolidTargeting.find(this.mob, 8, 4, -2, rotation.x, rotation.z, (float) Math.PI / 2F));
        }
    }

    public static class PickupItemGoal extends Goal {
        private final DirebatEntity mob;

        public PickupItemGoal(DirebatEntity mob) {
            this.mob = mob;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (this.mob.pickupCooldown > 0) return false;
            if (!this.mob.getStackInHand(Hand.MAIN_HAND).isEmpty()) return false;
            if (this.mob.getTarget() != null) return false;
            return this.mob.getItemToPickUp().isPresent();
        }

        @Override
        public boolean shouldContinue() {
            return this.mob.navigation.isFollowingPath();
        }

        @Override
        public void tick() {
            ItemStack itemInHand = this.mob.getStackInHand(Hand.MAIN_HAND);
            if (itemInHand.isEmpty()) this.mob.getItemToPickUp().ifPresent(entity -> this.mob.getNavigation().startMovingTo(entity, 1.2D));
        }

        @Override
        public void start() {
            this.mob.getItemToPickUp().ifPresent(entity -> this.mob.getNavigation().startMovingTo(entity, 1.2D));
            this.mob.pickupCooldown = 20 * 3;
        }
    }

    public static class DirebatTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
        public DirebatTargetGoal(DirebatEntity mob, Class<T> classTarget) {
            super(mob, classTarget, true);
        }

        @Override
        public boolean canStart() {
            boolean sup = super.canStart(); // calculate this.targetEntity
            if (this.targetEntity == null) return false;
            return sup && this.mob.getBrightnessAtEyes() >= 0.5F && !this.targetEntity.isSneaking();
        }
    }
}
