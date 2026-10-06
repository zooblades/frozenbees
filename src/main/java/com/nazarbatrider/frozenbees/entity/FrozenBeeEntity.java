package com.nazarbatrider.frozenbees.entity;

import com.nazarbatrider.frozenbees.block.FrozenHiveBlock;
import com.nazarbatrider.frozenbees.block.entity.FrozenHiveBlockEntity;
import com.nazarbatrider.frozenbees.block.entity.FrozenHiveTracker;
import com.nazarbatrider.frozenbees.registry.ModTags;
import java.util.EnumSet;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Замороженная пчела. Наследуем BeeEntity (ради рендера, звуков и атрибутов),
 * но целиком заменяем ИИ: она опыляет только цветы из тега frozenbees:frozen_flowers
 * и живёт в замороженных гнёздах/ульях.
 */
public class FrozenBeeEntity extends BeeEntity {
    public int enterCooldown = 0;
    public int pollinateCooldown = 0;
    public BlockPos hivePosF = null;

    public FrozenBeeEntity(EntityType<? extends BeeEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals(); // нужно, чтобы внутренние поля BeeEntity были инициализированы
        this.goalSelector.clear(goal -> true); // убираем ванильный ИИ (цели-«таргеты» оставляем)
        this.goalSelector.add(0, new FrozenStingGoal(this, 1.4, true));
        this.goalSelector.add(1, new EnterFrozenHiveGoal(this));
        this.goalSelector.add(2, new PollinateFrozenFlowerGoal(this));
        this.goalSelector.add(8, new FlyGoal(this, 1.0));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient) {
            if (enterCooldown > 0) enterCooldown--;
            if (pollinateCooldown > 0) pollinateCooldown--;
        }
    }

    /** Пчела морозная: не замерзает. */
    @Override
    public boolean canFreeze() {
        return false;
    }

    /** Жало: вместо яда замедление и немного заморозки. */
    @Override
    public boolean tryAttack(Entity target) {
        boolean hit = super.tryAttack(target);
        if (hit && target instanceof LivingEntity living) {
            living.removeStatusEffect(StatusEffects.POISON);
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1), this);
            living.setFrozenTicks(Math.min(living.getMinFreezeDamageTicks(), living.getFrozenTicks() + 100));
        }
        return hit;
    }

    /** setHasNectar у BeeEntity недоступен снаружи, поэтому меняем флаг через NBT. */
    public void setNectar(boolean value) {
        NbtCompound tag = new NbtCompound();
        this.writeCustomDataToNbt(tag);
        tag.putBoolean("HasNectar", value);
        this.readCustomDataFromNbt(tag);
    }

    // ------------------------------------------------------------------ goals

    static class FrozenStingGoal extends MeleeAttackGoal {
        FrozenStingGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && ((FrozenBeeEntity) this.mob).hasAngerTime();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && ((FrozenBeeEntity) this.mob).hasAngerTime();
        }
    }

    static class EnterFrozenHiveGoal extends Goal {
        private final FrozenBeeEntity bee;
        private int timeout;

        EnterFrozenHiveGoal(FrozenBeeEntity bee) {
            this.bee = bee;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        private boolean wantsHive() {
            return bee.hasNectar() || !bee.getWorld().isDay();
        }

        private static boolean hasSpace(World world, BlockPos pos) {
            return world.getBlockEntity(pos) instanceof FrozenHiveBlockEntity hive && !hive.isFull();
        }

        private BlockPos findHive() {
            World world = bee.getWorld();
            if (bee.hivePosF != null && hasSpace(world, bee.hivePosF)) {
                return bee.hivePosF;
            }
            bee.hivePosF = FrozenHiveTracker.findNearest(world, bee.getBlockPos(), 32, p -> hasSpace(world, p)).orElse(null);
            return bee.hivePosF;
        }

        @Override
        public boolean canStart() {
            return bee.enterCooldown <= 0 && wantsHive() && findHive() != null;
        }

        @Override
        public boolean shouldContinue() {
            return bee.enterCooldown <= 0 && timeout < 600 && wantsHive() && findHive() != null;
        }

        @Override
        public void start() {
            timeout = 0;
        }

        @Override
        public void tick() {
            timeout++;
            BlockPos hive = bee.hivePosF;
            if (hive == null) return;
            World world = bee.getWorld();
            BlockState state = world.getBlockState(hive);
            Direction facing = state.contains(FrozenHiveBlock.FACING) ? state.get(FrozenHiveBlock.FACING) : Direction.NORTH;
            Vec3d door = Vec3d.ofCenter(hive.offset(facing));
            if (bee.squaredDistanceTo(Vec3d.ofCenter(hive)) < 2.5 * 2.5) {
                if (world.getBlockEntity(hive) instanceof FrozenHiveBlockEntity be) {
                    be.tryEnter(bee);
                }
            } else if (bee.getNavigation().isIdle() || timeout % 20 == 0) {
                bee.getNavigation().startMovingTo(door.x, door.y, door.z, 1.0);
            }
        }

        @Override
        public void stop() {
            if (timeout >= 600) {
                bee.enterCooldown = 200;
                bee.hivePosF = null;
            }
            bee.getNavigation().stop();
        }
    }

    static class PollinateFrozenFlowerGoal extends Goal {
        private final FrozenBeeEntity bee;
        private BlockPos flower;
        private int near;
        private int total;

        PollinateFrozenFlowerGoal(FrozenBeeEntity bee) {
            this.bee = bee;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        private boolean isFlower(BlockPos pos) {
            return bee.getWorld().getBlockState(pos).isIn(ModTags.FROZEN_FLOWERS);
        }

        private BlockPos findFlower() {
            World world = bee.getWorld();
            BlockPos origin = bee.getBlockPos();
            BlockPos best = null;
            double bestDist = Double.MAX_VALUE;
            for (BlockPos p : BlockPos.iterate(origin.add(-10, -5, -10), origin.add(10, 5, 10))) {
                if (!world.isChunkLoaded(p)) continue;
                if (isFlower(p)) {
                    double d = p.getSquaredDistance(origin);
                    if (d < bestDist) {
                        bestDist = d;
                        best = p.toImmutable();
                    }
                }
            }
            return best;
        }

        @Override
        public boolean canStart() {
            if (bee.hasNectar() || bee.pollinateCooldown > 0) return false;
            flower = findFlower();
            if (flower == null) {
                bee.pollinateCooldown = 60 + bee.getRandom().nextInt(40);
                return false;
            }
            return true;
        }

        @Override
        public boolean shouldContinue() {
            return !bee.hasNectar() && flower != null && isFlower(flower) && total < 1200;
        }

        @Override
        public void start() {
            near = 0;
            total = 0;
        }

        @Override
        public void tick() {
            total++;
            Vec3d c = new Vec3d(flower.getX() + 0.5, flower.getY() + 0.6, flower.getZ() + 0.5);
            if (bee.squaredDistanceTo(c) < 1.3 * 1.3) {
                bee.getNavigation().stop();
                near++;
                if (near % 5 == 0 && bee.getWorld() instanceof ServerWorld sw) {
                    sw.spawnParticles(ParticleTypes.SNOWFLAKE, c.x, c.y, c.z, 2, 0.2, 0.1, 0.2, 0.01);
                }
                if (near >= 100) {
                    bee.setNectar(true);
                }
            } else if (bee.getNavigation().isIdle() || total % 20 == 0) {
                bee.getNavigation().startMovingTo(c.x, c.y, c.z, 1.0);
            }
        }

        @Override
        public void stop() {
            flower = null;
            bee.pollinateCooldown = 80 + bee.getRandom().nextInt(60);
            bee.getNavigation().stop();
        }
    }
}
