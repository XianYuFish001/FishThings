package com.fish.fishthings.item.customs.superTool;

import com.fish.fishthings.FishThings;
import com.fish.fishthings.modRegisters.ModItems;
import net.minecraft.core.component.DataComponents;
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

    // Debouncing
    private static final long DEBOUNCE_INTERVAL = 200;
    private static final Map<UUID, Long> lastActionTime = new HashMap<>();
    private static boolean isDebouncing(Player player) {
        UUID playerId = player.getUUID();
        Long currentTime = System.currentTimeMillis();
        Long lastTime = lastActionTime.get(playerId);

        if (lastTime == null || currentTime - lastTime > DEBOUNCE_INTERVAL) {
            lastActionTime.put(playerId, currentTime);
            return false;
        }
        return true;
    }

    // Message to Player
    private static void sendStatusMessage(Player player) {
        Component superToolMode = switch (superTool.getMode(player.getMainHandItem())) {
            case superOre ->
                    Component.translatable("message.actionbar.item.fish_things.super_tool.superOre");
            case superTree ->
                    Component.translatable("message.actionbar.item.fish_things.super_tool.superTree");
        };

        player.displayClientMessage(
                !superTool.isToolEnabled(player.getMainHandItem()) ?
                        Component.translatable("message.actionbar.item.fish_things.super_tool.disabled") : superToolMode,
                true
        );
    }

    // ClickEvents and fxxking clickEmpty!!!
    public static void leftClickOpt(Player player) {
    }

    public static void rightClickOpt(Player player) {
        if (player.getMainHandItem().getItem() != ModItems.SUPER_TOOL.get()) return;
        if (isDebouncing(player)) return;
        if (player.isCrouching()) {
            superTool.toggleToolEnabled(player.getMainHandItem());
            sendStatusMessage(player);
        }
        player.getMainHandItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE,
                superTool.getState(player.getMainHandItem()).isEnabled());
    }

    public static void scrollUp(Player player) {
        if (player.getMainHandItem().getItem() != ModItems.SUPER_TOOL.get()) return;
        if (!superTool.isToolEnabled(player.getMainHandItem())) return;
        if (isDebouncing(player)) return;
        if (player.isCrouching()) {
            superTool.switchMode(player.getMainHandItem(), false);
            sendStatusMessage(player);
        }
    }

    public static void scrollDown(Player player) {
        if (player.getMainHandItem().getItem() != ModItems.SUPER_TOOL.get()) return;
        if (!superTool.isToolEnabled(player.getMainHandItem())) return;
        if (isDebouncing(player)) return;
        if (player.isCrouching()) {
            superTool.switchMode(player.getMainHandItem(), true);
            sendStatusMessage(player);
        }
    }

    @SubscribeEvent
    public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        if (item.getItem() != ModItems.SUPER_TOOL.get()) return;
        if (superTool.isToolEnabled(player.getMainHandItem()) &&
                superTool.getMode(player.getMainHandItem()) == superToolState.ToolMode.superOre) {
            superOre.superOreAreaFunc(event.getLevel(), event.getPos(), player, item);
        }
//        if (isDebouncing(player)) return;
//        if (player.isCrouching()) {
//            superTool.switchMode(player.getMainHandItem());
//            sendStatusMessage(player);
//        }
    }

    @SubscribeEvent
    public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        if (item.getItem() != ModItems.SUPER_TOOL.get()) return;
        if (superTool.isToolEnabled(player.getMainHandItem())) {
            switch (superTool.getMode(player.getMainHandItem())) {
                case superOre -> superOre.superOreFunc(event.getLevel(), event.getPos(), player, item);
                case superTree -> superTree.superTreeFunc(event.getLevel(), event.getPos(), player, item);
            }
        }
        if (isDebouncing(player)) return;
        if (player.isCrouching()) {
            superTool.toggleToolEnabled(player.getMainHandItem());
            sendStatusMessage(player);
        }
        player.getMainHandItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE,
                superTool.getState(player.getMainHandItem()).isEnabled());
    }
//
//
//  // Fxxking client event!!!
//    @SubscribeEvent
//    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
//        Player player = event.getEntity();
//        ItemStack item = event.getItemStack();
//        if (item.getItem() != modItems.SUPER_TOOL.get()) return;
//        if (isDebouncing(player)) return;
//        if (player.isCrouching()) {
//            superTool.switchMode(player.getMainHandItem());
//            sendStatusMessage(player);
//        }
//    }
//
//    @SubscribeEvent
//    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
//        Player player = event.getEntity();
//        if (player.getMainHandItem().getItem() != modItems.SUPER_TOOL.get()) return;
//        if (isDebouncing(player)) return;
//        if (player.isCrouching()) {
//            superTool.toggleToolEnabled(player.getMainHandItem());
//            sendStatusMessage(player);
//        }
//        player.getMainHandItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE,
//                superTool.getState(player.getMainHandItem()).isEnabled());
//    }
}