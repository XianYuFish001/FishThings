package com.fish.fishthings.register;

import com.fish.fishthings.FishThings;
import com.fish.fishthings.network.PacketMouseAction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = FishThings.MODID)
public class ModMessages {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(FishThings.MODID);

        registrar.playBidirectional(
            PacketMouseAction.TYPE,
            PacketMouseAction.STREAM_CODEC,
            new DirectionalPayloadHandler<>(
                    PacketMouseAction::handle,
                    PacketMouseAction::handle
            )
        );
    }
}
