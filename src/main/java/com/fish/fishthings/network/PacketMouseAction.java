package com.fish.fishthings.network;

import com.fish.fishthings.FishThings;
import com.fish.fishthings.item.customs.superTool.superToolFunc;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record PacketMouseAction(ActionType action) implements CustomPacketPayload {
    public enum ActionType {
        CLICK_LEFT, CLICK_RIGHT, SCROLL_UP, SCROLL_DOWN
    }

    public static final CustomPacketPayload.Type<PacketMouseAction> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FishThings.MODID, "mouse_action"));

    public static final StreamCodec<FriendlyByteBuf, PacketMouseAction> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(ActionType.class),
            PacketMouseAction::action,
            PacketMouseAction::new
    );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final PacketMouseAction packet, final IPayloadContext context){
        if (context.player().level().isClientSide) return;
        context.enqueueWork(()->{
            Player player = context.player();
            if (!player.isAlive() || player.level().isClientSide) return;
            switch (packet.action()) {
                case SCROLL_UP -> superToolFunc.scrollUp(player);
                case SCROLL_DOWN -> superToolFunc.scrollDown(player);
                case CLICK_LEFT -> superToolFunc.leftClickOpt(player);
                case CLICK_RIGHT -> superToolFunc.rightClickOpt(player);
            }
        });
    }
}