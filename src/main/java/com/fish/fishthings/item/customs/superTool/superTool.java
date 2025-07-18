package com.fish.fishthings.item.customs.superTool;

import com.fish.fishthings.register.ModComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;

public class superTool extends PickaxeItem {

    public superTool() {
        super(Tiers.NETHERITE, new Item.Properties()
                .durability(1145)
                .rarity(Rarity.RARE)
                .fireResistant()
                .component(ModComponents.SUPER_TOOL_STATE.get(), superToolState.DEFAULT)
                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, superToolState.DEFAULT.isEnabled())
        );
    }

    public static superToolState getState(ItemStack item) {
        return item.getOrDefault(ModComponents.SUPER_TOOL_STATE.get(), superToolState.DEFAULT);
    }

    public static void setState(ItemStack item, superToolState state) {
        item.set(ModComponents.SUPER_TOOL_STATE.get(), state);
    }

    public static superToolState.ToolMode getMode(ItemStack item) {
        return getState(item).toolMode();
    }

    public static void setMode(ItemStack item, superToolState.ToolMode toolMode) {
        setState(item, new superToolState(toolMode, getState(item).isEnabled()));
    }

    public static void switchMode(ItemStack item, Boolean isDown) {
        superToolState.ToolMode[] toolModes = superToolState.ToolMode.values();
        if (isDown) {
            setMode(item, toolModes[(getMode(item).ordinal() + 1 <= 1 ? getMode(item).ordinal() + 1 : 0)]);
        } else {
            setMode(item, toolModes[(getMode(item).ordinal() - 1 >= 0 ? getMode(item).ordinal() - 1 : 1)]);
        }
    }

    public static boolean isToolEnabled(ItemStack item) {
        return getState(item).isEnabled();
    }

    public static void setToolEnabled(ItemStack item, boolean enabled) {
        setState(item, new superToolState(getState(item).toolMode(), enabled));
    }

    public static void toggleToolEnabled(ItemStack item) {
        setToolEnabled(item, !getState(item).isEnabled());
    }
}