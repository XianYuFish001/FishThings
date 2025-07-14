package com.fish.fishthings.item.customs.superTool;

import com.fish.fishthings.FishThings;
import com.fish.fishthings.item.modItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@EventBusSubscriber(modid = FishThings.MODID)
public class superToolFunc {
    // 防抖机制 - 防止快速连续点击
    private static final long DEBOUNCE_INTERVAL = 200; // 200毫秒防抖间隔
    private static final Map<UUID, Long> lastActionTime = new HashMap<>();

    private static boolean isDebouncing(Player player) {
        UUID playerId = player.getUUID();
        long currentTime = System.currentTimeMillis();
        Long lastTime = lastActionTime.get(playerId);

        if (lastTime == null || currentTime - lastTime > DEBOUNCE_INTERVAL) {
            lastActionTime.put(playerId, currentTime);
            return false;
        }
        return true;
    }

    private static void sendStatusMessage(Player player) {
        Component superToolMode = switch (superTool.getMode(player)) {
            case superOre ->
                    Component.translatable("message.actionbar.item.fish_things.super_tool.superOre");
            case superTree ->
                    Component.translatable("message.actionbar.item.fish_things.super_tool.superTree");
        };

        player.displayClientMessage(
                !superTool.isToolEnabled(player) ?
                        Component.translatable("message.actionbar.item.fish_things.super_tool.disabled") : superToolMode,
                true
        );
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        if (item.getItem() != modItems.SUPER_TOOL.get()) return;
        if (superTool.isToolEnabled(player) && superTool.getMode(player) == superTool.ToolMode.superOre) {
            superOre.superOreAreaFunc(event.getLevel(), event.getPos(), player, item);
        }
        if (isDebouncing(player)) return;
        if (player.isCrouching()) {
            superTool.switchMode(player);
            sendStatusMessage(player);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        if (item.getItem() != modItems.SUPER_TOOL.get()) return;
        if (superTool.isToolEnabled(player)) {
            switch (superTool.getMode(player)) {
                case superOre -> superOre.superOreFunc(event.getLevel(), event.getPos(), player, item);
                case superTree -> superTree.superTreeFunc(event.getLevel(), event.getPos(), player, item);
            }
        }
        if (isDebouncing(player)) return;
        if (player.isCrouching()) {
            superTool.toggleToolEnabeld(player);
            sendStatusMessage(player);
        }
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        if (item.getItem() != modItems.SUPER_TOOL.get()) return;
        if (isDebouncing(player)) return;
        if (player.isCrouching()) {
            superTool.switchMode(player);
            sendStatusMessage(player);
        }
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().getItem() != modItems.SUPER_TOOL.get()) return;
        if (isDebouncing(player)) return;
        if (player.isCrouching()) {
            superTool.toggleToolEnabeld(player);
            sendStatusMessage(player);
        }
    }
}