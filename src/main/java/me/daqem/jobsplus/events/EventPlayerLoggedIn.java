package me.daqem.jobsplus.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventPlayerLoggedIn {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().refreshDisplayName();
        if (!event.getPlayer().level.isClientSide) {
            try {
                for (ServerPlayer player : Objects.requireNonNull(event.getPlayer().getServer()).getPlayerList().getPlayers()) {
                    player.refreshDisplayName();
                    player.refreshTabListName();
                }
            } catch (Exception ignored) {
            }
        }
    }
}
