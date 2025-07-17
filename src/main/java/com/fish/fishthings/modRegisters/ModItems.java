package com.fish.fishthings.modRegisters;

import com.fish.fishthings.FishThings;
import com.fish.fishthings.item.customs.superTool.superTool;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, FishThings.MODID);

    public static final Supplier<Item> SUPER_TOOL = ITEMS.register("super_tool", superTool::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
