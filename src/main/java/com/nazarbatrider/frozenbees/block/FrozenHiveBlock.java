package com.nazarbatrider.frozenbees.block;

import com.mojang.serialization.MapCodec;
import com.nazarbatrider.frozenbees.block.entity.FrozenHiveBlockEntity;
import com.nazarbatrider.frozenbees.entity.FrozenBeeEntity;
import com.nazarbatrider.frozenbees.registry.ModBlockEntities;
import com.nazarbatrider.frozenbees.registry.ModItems;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ItemActionResult;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/** Общий блок для морозного гнезда и морозного улья. */
public class FrozenHiveBlock extends BlockWithEntity {
    public static final MapCodec<FrozenHiveBlock> CODEC = createCodec(FrozenHiveBlock::new);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final IntProperty HONEY_LEVEL = IntProperty.of("honey_level", 0, 5);

    public FrozenHiveBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, net.minecraft.util.math.Direction.NORTH).with(HONEY_LEVEL, 0));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HONEY_LEVEL);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FrozenHiveBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlockEntities.FROZEN_HIVE, FrozenHiveBlockEntity::serverTick);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(HONEY_LEVEL) >= 5) {
            boolean used = false;
            if (stack.isOf(Items.SHEARS)) {
                world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0f, 1.0f);
                dropStack(world, pos, new ItemStack(ModItems.FROZEN_HONEYCOMB, 3));
                stack.damage(1, player, LivingEntity.getSlotForHand(hand));
                used = true;
            } else if (stack.isOf(Items.GLASS_BOTTLE)) {
                stack.decrement(1);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                ItemStack bottle = new ItemStack(ModItems.COLD_HONEY_BOTTLE);
                if (stack.isEmpty()) {
                    player.setStackInHand(hand, bottle);
                } else if (!player.getInventory().insertStack(bottle)) {
                    player.dropItem(bottle, false);
                }
                used = true;
            }
            if (used) {
                if (!world.isClient) {
                    angerNearbyBees(world, pos, player);
                    world.setBlockState(pos, state.with(HONEY_LEVEL, 0));
                }
                return ItemActionResult.success(world.isClient);
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof FrozenHiveBlockEntity be && be.hasBees()) {
                be.releaseAllAngry(world, pos, state, player);
            }
            if (!player.isCreative()) {
                angerNearbyBees(world, pos, player);
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    private static void angerNearbyBees(World world, BlockPos pos, PlayerEntity player) {
        if (CampfireBlock.isLitCampfireInRange(world, pos)) return; // дым успокаивает пчёл
        List<FrozenBeeEntity> bees = world.getNonSpectatingEntities(FrozenBeeEntity.class, new Box(pos).expand(8, 6, 8));
        for (FrozenBeeEntity bee : bees) {
            bee.setAngryAt(player.getUuid());
            bee.chooseRandomAngerTime();
            bee.setTarget(player);
        }
    }
}
