package me.daqem.jobsplus;

import me.daqem.jobsplus.events.*;
import me.daqem.jobsplus.events.jobs.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

class SideProxy {

    SideProxy() {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;
        modEventBus.register(new EventRegisterCapabilities());
        modEventBus.register(new EventAttachCapabilities());
        modEventBus.register(new EventRegisterCommands());
        modEventBus.register(new EventServerChat());
        modEventBus.register(new EventNameFormat());
        modEventBus.register(new EventPlayerTick());

        modEventBus.register(new AlchemistEvents());
        modEventBus.register(new BuilderEvents());
        modEventBus.register(new ButcherEvents());
        modEventBus.register(new CraftsmanEvents());
        modEventBus.register(new DiggerEvents());
        modEventBus.register(new FarmerEvents());
        modEventBus.register(new FishermanEvents());
        modEventBus.register(new EnchanterEvents());
        modEventBus.register(new HunterEvents());
        modEventBus.register(new LumberjackEvents());
        modEventBus.register(new MinerEvents());
        modEventBus.register(new SmithEvents());


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
