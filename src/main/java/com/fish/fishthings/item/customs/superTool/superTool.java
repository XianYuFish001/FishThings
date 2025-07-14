package com.fish.fishthings.item.customs.superTool;

import com.fish.fishthings.item.modComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;

public class superTool extends PickaxeItem {

    public superTool() {
        super(Tiers.NETHERITE, new Item.Properties()
                .durability(1145)
                .rarity(Rarity.RARE)
                .fireResistant()
                .component(modComponents.SUPER_TOOL_STATE.get(), superToolState.DEFAULT)
                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, superToolState.DEFAULT.isEnabled())
        );
    }

    public static superToolState getState(ItemStack item) {
        return item.getOrDefault(modComponents.SUPER_TOOL_STATE.get(), superToolState.DEFAULT);
    }

    public static void setState(ItemStack item, superToolState state) {
        item.set(modComponents.SUPER_TOOL_STATE.get(), state);
    }

    public static superToolState.ToolMode getMode(ItemStack item) {
        return getState(item).toolMode();
    }

    public static void setMode(ItemStack item, superToolState.ToolMode toolMode) {
        setState(item, new superToolState(toolMode, getState(item).isEnabled()));
    }

    public static void switchMode(ItemStack item) {
        setMode(item, getState(item).toolMode() ==
                superToolState.ToolMode.superTree ? superToolState.ToolMode.superOre : superToolState.ToolMode.superTree);
    }

    public static boolean isToolEnabled(ItemStack item) {
        return getState(item).isEnabled();
    }

    public static void setToolEnabled(ItemStack item, boolean enabled) {
        setState(item, new superToolState(getState(item).toolMode(), enabled));
    }

    public static void toggleToolEnabeld(ItemStack item) {
        setToolEnabled(item, !getState(item).isEnabled());
    }
}