package com.fish.fishthings.register;

import com.fish.fishthings.FishThings;
import com.fish.fishthings.item.customs.superTool.superToolState;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModComponents {
    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, FishThings.MODID);

    // 注册工具状态组件
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<superToolState>> SUPER_TOOL_STATE =
            COMPONENTS.registerComponentType(
                    "super_tool_state",
                    builder -> builder
                            .persistent(superToolState.CODEC)
                            .networkSynchronized(superToolState.STREAM_CODEC)
            );
}