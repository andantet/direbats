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
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
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
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.teamhollow.direbats.init.DBGamerules;
import net.teamhollow.direbats.init.DBSoundEvents;

public class DirebatEntity extends PathAwareEntity {
    public static final String id = "direbat";
    public static final int[] spawnEggColors = { 7097929, 986895 };

    private int eatingTime;

    private static final TrackedData<Boolean> HANGING = DataTracker.registerData(DirebatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> ANGRY = DataTracker.registerData(DirebatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TargetPredicate CLOSE_PLAYER_PREDICATE = new TargetPredicate()
        .setBaseMaxDistance(6.5D)
        .includeTeammates()
        .setPredicate(new Predicate<LivingEntity>() {
            @Override
            public boolean test(LivingEntity livingEntity) {
                return !livingEntity.isSneaking();
            }
        });
    private static final Predicate<ItemEntity> PICKABLE_DROP_FILTER = new Predicate<ItemEntity>() {
        @Override
        public boolean test(ItemEntity itemEntity) {
            return itemEntity != null && !itemEntity.cannotPickup();
        }
    };

    private BlockPos targetPos;

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
        this.goalSelector.add(2, new DirebatEntity.FindBlockToHang(1.5D, 14, 8));
        this.targetSelector.add(0, new RevengeGoal(this));
        this.targetSelector.add(0, new TargetGoal<PlayerEntity>(this, PlayerEntity.class));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HANGING, false);
        this.dataTracker.startTracking(ANGRY, false);
    }

    public static DefaultAttributeContainer.Builder createDirebatAttributes() {
        return MobEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0D)
            .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.22D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22D)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public Vec3d method_26318(Vec3d vec3d, float f) {
        this.updateVelocity(this.getRelevantMoveFactor(f), vec3d);
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
    protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {}

    public boolean isHanging() {
        return this.dataTracker.get(HANGING);
    }
    public void setHanging(boolean hanging) {
        this.dataTracker.set(HANGING, hanging);
    }

    public boolean isAngry() {
        return this.dataTracker.get(ANGRY);
    }
    public void setAngry(boolean angry) {
        this.dataTracker.set(ANGRY, angry);
    }

    public static boolean isSpawnDark(ServerWorldAccess serverWorldAccess, BlockPos pos, Random random) {
        if (serverWorldAccess.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
            return false;
        } else {
            int i = serverWorldAccess.toServerWorld().isThundering() ? serverWorldAccess.getLightLevel(pos, 10) : serverWorldAccess.getLightLevel(pos);
            return i <= random.nextInt(8);
        }
    }
    public static boolean canSpawnInDark(EntityType<? extends DirebatEntity> type, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos pos, Random random) {
        return isSpawnDark(serverWorldAccess, pos, random) && canMobSpawn(type, serverWorldAccess, spawnReason, pos, random);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isHanging()) {
            this.setVelocity(Vec3d.ZERO);
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();

        if (!this.isAlive()) return;

        if (isHanging()) {
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

            // get nearby non-sneaking player and set target
            PlayerEntity closestPlayer = this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this);
            if (closestPlayer != null)
                this.setTarget(closestPlayer);
            else {
                // anchor direbat to block (preferably ceiling)
                Vec3d pos = this.getPos().floorAlongAxes(EnumSet.allOf(Direction.Axis.class));
                this.setPos(pos.getX() + 0.5D, pos.getY() + (this.world.getBlockState(this.getBlockPos().up()).isAir() && this.world.getBlockState(this.getBlockPos().up(2)).isFullCube(world, this.getBlockPos().up(2)) ? 1.0D : 0.0D), pos.getZ() + 0.5D);

                // reset velocity
                this.setVelocity(Vec3d.ZERO);

                // verify position validity
                if (this.getTarget() != null || !(this.world.getBlockState(this.getBlockPos().up()).isFullCube(world, this.getBlockPos().up()))) {
                    this.setHanging(false);
                }
            }
        } else {
            if (this.world.getGameRules().getBoolean(DBGamerules.DIREBAT_ITEM_PICKUP) && this.random.nextInt(10) == 0) {
                List<ItemEntity> list = this.world.getEntitiesByClass(ItemEntity.class, this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), PICKABLE_DROP_FILTER);
                if (this.getStackInHand(Hand.MAIN_HAND).isEmpty() && !list.isEmpty()) this.targetPos = list.get(0).getBlockPos();
            }

            BlockPos pos = this.getBlockPos();
            BlockState blockState = this.world.getBlockState(pos);
            BlockState blockStateUp = this.world.getBlockState(pos.up());
            BlockState blockStateUp2 = this.world.getBlockState(pos.up(2));
            if (this.random.nextInt(60) == 0 && this.getTarget() == null && blockState.isAir() && blockStateUp.isAir() && blockStateUp2.isFullCube(world, pos.up(2))) this.setHanging(true);
            else {
                // get target and verify position
                LivingEntity targetEntity = this.getTarget();
                targetPos = targetEntity != null ? targetEntity.getBlockPos() : (targetPos != null ? targetPos : this.getBlockPos());

                // randomise target pos
                BlockPos randomPos = targetPos;
                if (randomPos == null) randomPos = new BlockPos(this.getX(), this.getY(), this.getZ());
                randomPos = new BlockPos(
                    randomPos.getX() + this.random.nextInt(7) - this.random.nextInt(7),
                    randomPos.getY() + this.random.nextInt(3),
                    randomPos.getZ() + this.random.nextInt(7) - this.random.nextInt(7)
                );

                // get velocity from target pos
                double x = randomPos.getX() + 0.5D - this.getX();
                double y = randomPos.getY() + 0.1D - this.getY();
                double z = randomPos.getZ() + 0.5D - this.getZ();

                Vec3d vec3d = this.getVelocity();
                vec3d = vec3d.add(
                    (Math.signum(x) * 0.5D - vec3d.x) * 0.10000000149011612D,
                    ((Math.signum(y) * 0.699999988079071D - vec3d.y) * 0.10000000149011612D) + (targetPos.getY() > this.getY() ? 0.2D : 0.0D),
                    (Math.signum(z) * 0.5D - vec3d.z) * 0.10000000149011612D
                );

                // set velocity
                this.setVelocity(vec3d);
                this.forwardSpeed = 0.5F;

                // randomise rotation
                this.yaw += MathHelper.wrapDegrees(((float) (MathHelper.atan2(vec3d.z, vec3d.x) * 57.2957763671875D) - 90.0F) - this.yaw);

                if (this.getTarget() != null) this.lookAtEntity(this.getTarget(), 10.0F, 10.0F);

                // pass out anger
                if ((this.getTarget() != null) != this.isAngry()) this.setAngry(this.getTarget() != null);
            }
        }
    }

    private void spawnItemParticles(ItemStack stack, int count) {
        for (int i = 0; i < count; ++i) {
            Vec3d vec3d = new Vec3d(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3d = vec3d.rotateX(-this.pitch * 0.017453292F);
            vec3d = vec3d.rotateY(-this.yaw * 0.017453292F);
            double d = (double) (-this.random.nextFloat()) * 0.6D - 0.3D;
            Vec3d vec3d2 = new Vec3d(((double) this.random.nextFloat() - 0.5D) * 0.3D, d, 0.6D);
            vec3d2 = vec3d2.rotateX(-this.pitch * 0.017453292F);
            vec3d2 = vec3d2.rotateY(-this.yaw * 0.017453292F);
            vec3d2 = vec3d2.add(this.getX(), this.getEyeY(), this.getZ());
            this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);
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

                this.playSound(DBSoundEvents.ENTITY_DIREBAT_ATTACK, 10.0F, 0.95F + this.random.nextFloat() * 0.1F);
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
        if (this.world.getGameRules().getBoolean(DBGamerules.DIREBAT_ITEM_PICKUP)) {
            ItemStack currentItem = this.getEquippedStack(EquipmentSlot.MAINHAND);

            if (currentItem.isEmpty() && PICKABLE_DROP_FILTER.test(itemEntity)) {
                ItemStack newItem = itemEntity.getStack();

                this.equipLootStack(EquipmentSlot.MAINHAND, newItem);
                this.method_29499(itemEntity);
                this.sendPickup(itemEntity, newItem.getCount());
                itemEntity.remove();
            }
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
        this.dataTracker.set(HANGING, compound.getBoolean("Hanging"));
        this.dataTracker.set(ANGRY, compound.getBoolean("Angry"));
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compound) {
        super.writeCustomDataToTag(compound);
        compound.putBoolean("Hanging", this.dataTracker.get(HANGING));
        compound.putBoolean("Angry", this.dataTracker.get(ANGRY));
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

            return worldLight > random.nextInt(maximumLight) ? false : canMobSpawn(type, world, spawnReason, pos, random);
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
        return this.getMainHandStack() != null;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height / 2;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(DirebatEntity entity) {
            super(entity, 1.0D, true);
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

    class FindBlockToHang extends MoveToTargetPosGoal {
        public FindBlockToHang(double speed, int range, int maxYDifference) {
            super(DirebatEntity.this, speed, range, maxYDifference);
        }

        @Override
        public boolean canStart() {
            return !DirebatEntity.this.isHanging() && DirebatEntity.this.targetPos != null && super.canStart();
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            BlockState blockState = world.getBlockState(pos);
            BlockState blockStateUp = world.getBlockState(pos.up());
            BlockState blockStateUp2 = world.getBlockState(pos.up(2));

            return blockState.isAir() && blockStateUp.isAir() && blockStateUp2.isFullCube(world, pos.up(2));
        }

        @Override
        protected int getInterval(PathAwareEntity mob) {
            return 40 + mob.getRandom().nextInt(40);
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            DirebatEntity entity = (DirebatEntity) mob;
            entity.targetPos = this.targetPos;

            super.start();
        }
    }
}
