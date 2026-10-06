package com.nazarbatrider.frozenbees.block.entity;

import com.nazarbatrider.frozenbees.block.FrozenHiveBlock;
import com.nazarbatrider.frozenbees.entity.FrozenBeeEntity;
import com.nazarbatrider.frozenbees.registry.ModBlockEntities;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/** Хранит пчёл внутри гнезда/улья и выпускает их обратно. */
public class FrozenHiveBlockEntity extends BlockEntity {
    public static final int MAX_BEES = 3;
    public static final int MIN_TICKS_WITH_NECTAR = 1200;
    public static final int MIN_TICKS_EMPTY = 400;

    private static final class Occupant {
        final NbtCompound data;
        int ticks;
        final int minTicks;

        Occupant(NbtCompound data, int ticks, int minTicks) {
            this.data = data;
            this.ticks = ticks;
            this.minTicks = minTicks;
        }
    }

    private final List<Occupant> occupants = new ArrayList<>();

    public FrozenHiveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FROZEN_HIVE, pos, state);
    }

    public boolean isFull() {
        return occupants.size() >= MAX_BEES;
    }

    public boolean hasBees() {
        return !occupants.isEmpty();
    }

    /** Пчела залетает внутрь. */
    public boolean tryEnter(FrozenBeeEntity bee) {
        if (world == null || world.isClient || isFull()) return false;
        bee.stopRiding();
        bee.removeAllPassengers();
        NbtCompound tag = new NbtCompound();
        bee.saveNbt(tag);
        tag.putString("id", "frozenbees:frozen_bee");
        tag.remove("UUID");
        tag.remove("Air");
        tag.remove("Passengers");
        tag.remove("leash");
        occupants.add(new Occupant(tag, 0, bee.hasNectar() ? MIN_TICKS_WITH_NECTAR : MIN_TICKS_EMPTY));
        world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0f, 1.0f);
        bee.discard();
        markDirty();
        return true;
    }

    /** Добавить пчелу без реальной сущности (для генерации мира). */
    public void addBee(NbtCompound entityData, int ticks, int minTicks) {
        if (!isFull()) {
            occupants.add(new Occupant(entityData, ticks, minTicks));
            markDirty();
        }
    }

    /** Выпускает всех пчёл злыми (когда улей сломали). */
    public void releaseAllAngry(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.isClient) return;
        for (Occupant o : occupants) {
            spawnBee(world, pos, state, o, player, false);
        }
        occupants.clear();
        markDirty();
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, FrozenHiveBlockEntity be) {
        if (be.occupants.isEmpty()) return;
        if (world.random.nextInt(250) == 0) {
            world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_WORK, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
        boolean changed = false;
        Iterator<Occupant> it = be.occupants.iterator();
        while (it.hasNext()) {
            Occupant o = it.next();
            o.ticks++;
            if (o.ticks > o.minTicks && canRelease(world, pos, state)) {
                if (spawnBee(world, pos, state, o, null, true)) {
                    it.remove();
                    changed = true;
                }
            }
        }
        if (changed) be.markDirty();
    }

    private static boolean canRelease(World world, BlockPos pos, BlockState state) {
        if (!world.isDay()) return false;
        Direction facing = state.contains(FrozenHiveBlock.FACING) ? state.get(FrozenHiveBlock.FACING) : Direction.NORTH;
        BlockPos front = pos.offset(facing);
        return world.getBlockState(front).getCollisionShape(world, front).isEmpty();
    }

    private static boolean spawnBee(World world, BlockPos pos, BlockState state, Occupant o, PlayerEntity angryAt, boolean deliverHoney) {
        Optional<Entity> opt = EntityType_getEntity(o.data.copy(), world);
        if (opt.isEmpty()) return true; // битые данные просто выбрасываем
        Entity entity = opt.get();
        Direction facing = state.contains(FrozenHiveBlock.FACING) ? state.get(FrozenHiveBlock.FACING) : Direction.NORTH;
        double x = pos.getX() + 0.5 + facing.getOffsetX() * 0.9;
        double y = pos.getY() + 0.25;
        double z = pos.getZ() + 0.5 + facing.getOffsetZ() * 0.9;
        entity.refreshPositionAndAngles(x, y, z, entity.getYaw(), entity.getPitch());
        if (entity instanceof FrozenBeeEntity bee) {
            if (deliverHoney && bee.hasNectar()) {
                bee.setNectar(false);
                BlockState cur = world.getBlockState(pos);
                if (cur.contains(FrozenHiveBlock.HONEY_LEVEL) && cur.get(FrozenHiveBlock.HONEY_LEVEL) < 5) {
                    world.setBlockState(pos, cur.with(FrozenHiveBlock.HONEY_LEVEL, cur.get(FrozenHiveBlock.HONEY_LEVEL) + 1));
                }
            }
            bee.enterCooldown = 400;
            if (angryAt != null) {
                bee.setAngryAt(angryAt.getUuid());
                bee.chooseRandomAngerTime();
                bee.setTarget(angryAt);
            }
        }
        world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.spawnEntity(entity);
        return true;
    }

    private static Optional<Entity> EntityType_getEntity(NbtCompound nbt, World world) {
        return net.minecraft.entity.EntityType.getEntityFromNbt(nbt, world);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);
        occupants.clear();
        NbtList list = nbt.getList("Bees", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound c = list.getCompound(i);
            occupants.add(new Occupant(c.getCompound("EntityData"), c.getInt("TicksInHive"), c.getInt("MinTicks")));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.writeNbt(nbt, lookup);
        NbtList list = new NbtList();
        for (Occupant o : occupants) {
            NbtCompound c = new NbtCompound();
            c.put("EntityData", o.data);
            c.putInt("TicksInHive", o.ticks);
            c.putInt("MinTicks", o.minTicks);
            list.add(c);
        }
        nbt.put("Bees", list);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if (!world.isClient) FrozenHiveTracker.add(world, pos);
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        if (world != null && !world.isClient) FrozenHiveTracker.remove(world, pos);
    }
}
