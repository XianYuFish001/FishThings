package com.fish.fishthings.item.customs.superTool;

import com.fish.fishthings.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class superTree {
    private static final int MAX_LOG_COUNT = Config.MAX_LOG_COUNT.getAsInt();
    private static final int MAX_LEAF_COUNT = Config.MAX_LEAF_COUNT.getAsInt();
    // 26个方向（包括斜角）
    private static final BlockPos[] TWENTY_SIX_DIRECTIONS = new BlockPos[26];

    static {
        int index = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    TWENTY_SIX_DIRECTIONS[index++] = new BlockPos(x, y, z);
                }
            }
        }
    }

    private static final TagKey<Block> LOG_TAG = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("minecraft", "logs")
    );
    private static final TagKey<Block> LEAF_TAG = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("minecraft", "leaves")
    );

    public static void superTreeFunc(Level level, BlockPos startPos, Player player, ItemStack tool) {
        if (level.isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) level;

        BlockState startState = level.getBlockState(startPos);
        if (!startState.is(LOG_TAG)) {
            return;
        }

        TreeStructure tree = collectTreeBlocks(serverLevel, startPos, startState.getBlock());
        if (tree.leaves.isEmpty() || tree.logs.isEmpty()) {
            player.sendSystemMessage(Component.translatable("message.item.fish_things.super_tool.superTree.isnt_tree"));
            return;
        }

        for (BlockPos pos : tree.leaves) {
            breakBlock(serverLevel, pos, player, tool);
        }

        for (BlockPos pos : tree.logs) {
            breakBlock(serverLevel, pos, player, tool);
        }

        player.sendSystemMessage(Component.translatable("message.item.fish_things.super_tool.superTree.finished"));
    }

    private static void breakBlock(ServerLevel level, BlockPos pos, Player player, ItemStack tool) {
        BlockState state = level.getBlockState(pos);
        List<ItemStack> drops = Block.getDrops(state, level, pos, null, player, tool);

        for (ItemStack stack : drops) {
            ItemEntity itemEntity = new ItemEntity(
                    level,
                    player.getX(),
                    player.getY() + 0.5,
                    player.getZ(),
                    stack
            );
            level.addFreshEntity(itemEntity);
        }

        level.destroyBlock(pos, false);
    }

    private static TreeStructure collectTreeBlocks(ServerLevel level, BlockPos startPos, Block logType) {
        Set<BlockPos> logs = new HashSet<>();
        Set<BlockPos> leaves = new HashSet<>();
        Queue<BlockPos> logQueue = new LinkedList<>();
        Set<BlockPos> visitedLogs = new HashSet<>();
        Set<BlockPos> visitedLeaves = new HashSet<>();

        logQueue.add(startPos);
        visitedLogs.add(startPos);

        while (!logQueue.isEmpty() && logs.size() < MAX_LOG_COUNT) {
            BlockPos current = logQueue.poll();
            logs.add(current);

            for (BlockPos dir : TWENTY_SIX_DIRECTIONS) {
                BlockPos neighbor = current.offset(dir.getX(), dir.getY(), dir.getZ());
                if (visitedLogs.add(neighbor)) {
                    BlockState neighborState = level.getBlockState(neighbor);
                    if (neighborState.is(LOG_TAG) && neighborState.getBlock() == logType) {
                        logQueue.add(neighbor);
                    }
                }
            }
        }

        Queue<BlockPos> leafQueue = new LinkedList<>();
        Block leafType = null;

        // 步骤1：收集所有可能的树叶（26方向）
        for (BlockPos logPos : logs) {
            for (BlockPos dir : TWENTY_SIX_DIRECTIONS) {
                BlockPos leafPos = logPos.offset(dir.getX(), dir.getY(), dir.getZ());
                if (visitedLeaves.add(leafPos)) {
                    BlockState leafState = level.getBlockState(leafPos);
                    if (isValidLeaf(leafState)) {
                        if (leafType == null) leafType = leafState.getBlock();
                        if (leafState.getBlock() == leafType) {
                            leaves.add(leafPos);
                            leafQueue.add(leafPos);
                        }
                    }
                }
            }
        }

        // 步骤2：扩散收集树叶（26方向）
        while (!leafQueue.isEmpty() && leaves.size() < MAX_LEAF_COUNT) {
            BlockPos current = leafQueue.poll();
            for (BlockPos dir : TWENTY_SIX_DIRECTIONS) {
                BlockPos neighbor = current.offset(dir.getX(), dir.getY(), dir.getZ());
                if (visitedLeaves.add(neighbor)) {
                    BlockState neighborState = level.getBlockState(neighbor);
                    if (isValidLeaf(neighborState) &&
                            neighborState.getBlock() == leafType) {
                        leaves.add(neighbor);
                        leafQueue.add(neighbor);
                    }
                }
            }
        }

        // 步骤3：过滤掉与其他树原木直接相连的树叶
        Set<BlockPos> leavesToRemove = new HashSet<>();
        for (BlockPos leafPos : leaves) {
            // 检查6个正交方向（上下左右前后）
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = leafPos.relative(dir);
                BlockState neighborState = level.getBlockState(neighborPos);

                // 如果相邻方块是原木且不属于当前树
                if (neighborState.is(LOG_TAG) && !logs.contains(neighborPos)) {
                    leavesToRemove.add(leafPos);
                    break; // 找到一处连接即跳过
                }
            }
        }
        leaves.removeAll(leavesToRemove);

        return new TreeStructure(logs, leaves);
    }

    private static boolean isValidLeaf(BlockState state) {
        return state.is(LEAF_TAG) &&
                state.hasProperty(LeavesBlock.PERSISTENT) &&
                !state.getValue(LeavesBlock.PERSISTENT);
    }

    private record TreeStructure(Set<BlockPos> logs, Set<BlockPos> leaves) {}
}