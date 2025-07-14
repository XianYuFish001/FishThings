package com.fish.fishthings.item.customs.superTool;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class superTool extends PickaxeItem {

    public superTool() {
        super(Tiers.NETHERITE, new Item.Properties()
                .durability(1145)
                .rarity(Rarity.RARE)
                .fireResistant()
        );
    }

    private static final Map<UUID, ToolMode> toolMode = new HashMap<>();
    private static final Map<UUID, Boolean> isToolEnabled = new HashMap<>();
    private static final ToolMode DEFAULT_MODE = ToolMode.superTree;
    private static final Boolean DEFAULT_ENABLED = false;

    public static ToolMode getMode(Player player) {
        return toolMode.getOrDefault(player.getUUID(), DEFAULT_MODE);
    }

    public static void setMode(Player player, ToolMode mode) {
        toolMode.put(player.getUUID(), mode);
    }

    public static void switchMode(Player player) {
        setMode(player, getMode(player) == ToolMode.superTree ? ToolMode.superOre : ToolMode.superTree);
    }

    public static boolean isToolEnabled(Player player) {
        return isToolEnabled.getOrDefault(player.getUUID(), DEFAULT_ENABLED);
    }

    public static void setToolEnabled(Player player, boolean isEnabled) {
        isToolEnabled.put(player.getUUID(), isEnabled);
    }

    public static void toggleToolEnabeld(Player player) {
        setToolEnabled(player, !isToolEnabled(player));
    }

    public enum ToolMode {
        superTree,
        superOre
    }
}