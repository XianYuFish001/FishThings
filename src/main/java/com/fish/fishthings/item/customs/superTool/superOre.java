package com.fish.fishthings.item.customs.superTool;

import com.fish.fishthings.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.util.*;

public class superOre {
    private static final int MAX_CONNECTED = Config.MAX_ORE_COUNT.getAsInt();
    private static final int CHUNK_SIZE = 16;
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

    public static void superOreFunc(Level level, BlockPos startPos, Player player, ItemStack tool) {
        if (level.isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) level;

        BlockState startState = level.getBlockState(startPos);
        if (!startState.is(Tags.Blocks.ORES)) {
            return;
        }

        Set<BlockPos> ores = collectConnectedOres(serverLevel, startPos, startState.getBlock());
        if (ores.isEmpty()) {
            return;
        }

        for (BlockPos pos : ores) {
            breakOre(serverLevel, pos, player, tool);
        }

        player.sendSystemMessage(Component.translatable("message.item.fish_things.super_tool.superOre.finished", ores.size()));
    }

    public static void superOreAreaFunc(Level level, BlockPos startPos, Player player, ItemStack tool) {
        if (level.isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) level;

        BlockState startState = level.getBlockState(startPos);
        if (!startState.is(Tags.Blocks.ORES)) return;

        int chunkX = (startPos.getX() >> 4) << 4;
        int chunkY = (startPos.getY() >> 4) << 4;
        int chunkZ = (startPos.getZ() >> 4) << 4;

        List<BlockPos> ores = new ArrayList<>();
        for (int y = 0; y < CHUNK_SIZE; y++) {
            for (int x = 0; x < CHUNK_SIZE; x++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    BlockPos pos = new BlockPos(
                            chunkX + x,
                            chunkY + y,
                            chunkZ + z
                    );
                    BlockState state = level.getBlockState(pos);
                    if (!state.isAir() && state.is(Tags.Blocks.ORES)) {
                        ores.add(pos);
                    }
                }
            }
        }

        if (ores.isEmpty()) return;

        for (BlockPos pos : ores) {
            breakOre(serverLevel, pos, player, tool);
        }

        player.sendSystemMessage(Component.translatable("message.item.fish_things.super_tool.superOre.finished", ores.size()));
    }

    private static void breakOre(ServerLevel level, BlockPos pos, Player player, ItemStack tool) {
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

    private static Set<BlockPos> collectConnectedOres(ServerLevel level, BlockPos startPos, Block oreType) {
        Set<BlockPos> connected = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty() && connected.size() < MAX_CONNECTED) {
            BlockPos current = queue.poll();
            connected.add(current);

            for (BlockPos dir : TWENTY_SIX_DIRECTIONS) {
                BlockPos neighbor = current.offset(dir.getX(), dir.getY(), dir.getZ());
                if (visited.add(neighbor)) {
                    BlockState neighborState = level.getBlockState(neighbor);
                    if (!neighborState.isAir() &&
                            neighborState.is(Tags.Blocks.ORES) &&
                            neighborState.getBlock() == oreType) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        return connected;
    }
}