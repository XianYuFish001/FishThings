package com.fish.fishthings.item.customs.superTool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public record superToolState(ToolMode toolMode, boolean isEnabled) {
    // 默认状态
    public static final superToolState DEFAULT = new superToolState(ToolMode.superTree, false);

    // 编解码器
    public static final Codec<superToolState> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ToolMode.CODEC.fieldOf("mode").forGetter(superToolState::toolMode),
                    Codec.BOOL.fieldOf("isEnabled").forGetter(superToolState::isEnabled)
            ).apply(instance, superToolState::new)
    );

    public static final StreamCodec<FriendlyByteBuf, superToolState> STREAM_CODEC = StreamCodec.composite(
            ToolMode.STREAM_CODEC, superToolState::toolMode,
            ByteBufCodecs.BOOL, superToolState::isEnabled,
            superToolState::new
    );

    public enum ToolMode implements StringRepresentable {
        superTree("superTree"),
        superOre("superOre");

        public static final Codec<ToolMode> CODEC = StringRepresentable.fromEnum(ToolMode::values);
        public static final StreamCodec<ByteBuf, ToolMode> STREAM_CODEC =
                ByteBufCodecs.fromCodec(CODEC);

        private final String name;

        ToolMode(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}