package com.fish.fishthings;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MAX_LOG_COUNT = BUILDER
            .translation("config.fish_things.max_log_count")
            .defineInRange("maxLogCount", 128, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue MAX_LEAF_COUNT = BUILDER
            .translation("config.fish_things.max_leaf_count")
            .defineInRange("maxLeafCount", 512, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue MAX_ORE_COUNT = BUILDER
            .translation("config.fish_things.max_ore_count")
            .defineInRange("maxOreCount", 256, 1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();
}
