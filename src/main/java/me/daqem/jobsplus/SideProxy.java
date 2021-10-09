package me.daqem.jobsplus;

import me.daqem.jobsplus.events.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

class SideProxy {

    SideProxy() {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;
        modEventBus.register(new EventRegisterCapabilities());
        modEventBus.register(new EventAttachCapabilities());
        modEventBus.register(new EventRegisterCommands());

        modEventBus.addListener(EventClone::onDeath);
    }

    static class Server extends SideProxy {
        Server() {

        }
    }

    static class Client extends SideProxy {
        Client() {

        }
    }
}
