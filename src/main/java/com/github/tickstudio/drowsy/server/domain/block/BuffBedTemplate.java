package com.github.tickstudio.drowsy.server.domain.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// buff床模板类,预定义了1*2大小的床模板,子类需要复写getShape\effects\codec方法
public abstract class BuffBedTemplate extends HorizontalDirectionalBlock implements BuffBed {

    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    public BuffBedTemplate(Properties properties){
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, BedPart.FOOT)
                .setValue(OCCUPIED,false)
        );
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING, OCCUPIED);
    }


    // 右键交互逻辑
    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        }

        if (state.getValue(PART) != BedPart.HEAD) {
            pos = pos.relative(state.getValue(FACING));   // 从 foot 跳到 head
            state = level.getBlockState(pos);
            if (!state.is(this)) {                        // head 不存在？
                return InteractionResult.CONSUME;          // 什么都不做
            }
        }

        else if (state.getValue(OCCUPIED)) {

            player.displayClientMessage(Component.translatable("block.drowsy.bed.occupied"), true);

            return InteractionResult.SUCCESS;
        }

        // 睡觉
        player.startSleepInBed(pos).ifLeft(
                l->{
                    if (l.getMessage()!=null)
                        player.displayClientMessage(l.getMessage(),true);
                }
        );
        return InteractionResult.SUCCESS;

    }




    // 放置时的状态
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        // context.getHorizontalDirection() = 玩家面朝的方向（不取反）
        Direction direction = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();
        BlockPos headPos = pos.relative(direction);
        Level level = context.getLevel();

        // 检查 head 位置是否可放置（可替换且在世界边界内）
        if (level.getBlockState(headPos).canBeReplaced(context)
                && level.getWorldBorder().isWithinBounds(headPos)) {
            return this.defaultBlockState()
                    .setValue(FACING, direction)       // 朝玩家面对方向
                    .setValue(PART, BedPart.FOOT);
        }
        return null;  // 放不下则阻止放置
    }

    // 放置头部方块
    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                            @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            BlockPos headPos = pos.relative(facing);
            level.setBlock(headPos, state.setValue(PART, BedPart.HEAD),
                    Block.UPDATE_ALL);
        }
    }

    protected static Direction getNeighbourDirection(BedPart part, Direction facing) {
        return part == BedPart.FOOT ? facing : facing.getOpposite();
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, @NotNull Direction neighborDir,
                                              @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                              @NotNull BlockPos currentPos, @NotNull BlockPos neighborPos) {

        // 只在"配对方向"上的邻居变化时才检查
        if (neighborDir == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            // 邻居还是同款方块且 part 不同 → 一切正常
            if (neighborState.is(this) && neighborState.getValue(PART) != state.getValue(PART)) {
                return state;
            }
            // 邻居不在了或不是配对的半 → 自己也消失
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, neighborDir, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // 1. 只在服务端 + 创造模式下才执行这个补丁
        if (!level.isClientSide && player.isCreative()) {

            level.setBlock(pos,Blocks.AIR.defaultBlockState(),35); // 35 是更新标志，表示：更新自己 + 更新邻居 + 通知客户端
            level.levelEvent(player, 2001, pos, Block.getId(state)); // 2001 是方块破坏事件，客户端收到后会播放破坏粒子和音效

        }
        // 最后调用父类，让正常的破坏流程继续
        return super.playerWillDestroy(level, pos, state, player);

    }
}
