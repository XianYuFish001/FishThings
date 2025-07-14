package com.fish.fishthings.item;

import com.fish.fishthings.FishThings;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class creativeTab {
    public static final DeferredRegister<CreativeModeTab> CTAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FishThings.MODID);

    public static final String TAB = "ctab.fish_things.maintab";

    public static final Supplier<CreativeModeTab> MAINTAB = CTAB.register("main_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(TAB))
            .icon(() -> modItems.SUPER_TOOL.get().getDefaultInstance())
            .displayItems((p, o) -> {
                o.accept(modItems.SUPER_TOOL.get());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CTAB.register(eventBus);
    }
}
