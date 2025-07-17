package com.fish.fishthings.handlers;

import com.fish.fishthings.FishThings;
import com.fish.fishthings.item.customs.superTool.superTool;
import com.fish.fishthings.modRegisters.ModItems;
import com.fish.fishthings.network.PacketMouseAction;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = FishThings.MODID)
public class handlerKeyCheck {
    @SubscribeEvent
    public static void leftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        PacketDistributor.sendToServer(new PacketMouseAction(PacketMouseAction.ActionType.CLICK_LEFT));
    }
    @SubscribeEvent
    public static void rightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        PacketDistributor.sendToServer(new PacketMouseAction(PacketMouseAction.ActionType.CLICK_RIGHT));
    }
    @SubscribeEvent
    public static void mouseScrollingEvent(InputEvent.MouseScrollingEvent event) {
        Minecraft game = Minecraft.getInstance();
        if (game.screen != null || game.isPaused() || game.player == null) return;
        double delta = event.getScrollDeltaY();
        if (delta > 0) {
            PacketDistributor.sendToServer(new PacketMouseAction(PacketMouseAction.ActionType.SCROLL_UP));
            if (game.player.getMainHandItem().getItem() == ModItems.SUPER_TOOL.get() &&
                    game.player.isCrouching() && superTool.isToolEnabled(game.player.getMainHandItem()))
                event.setCanceled(true);
        } else if (delta < 0) {
            PacketDistributor.sendToServer(new PacketMouseAction(PacketMouseAction.ActionType.SCROLL_DOWN));
            if (game.player.getMainHandItem().getItem() == ModItems.SUPER_TOOL.get() &&
                    game.player.isCrouching() && superTool.isToolEnabled(game.player.getMainHandItem()))
                event.setCanceled(true);
        }
    }
}
