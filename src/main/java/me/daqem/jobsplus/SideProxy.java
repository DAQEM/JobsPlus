package me.daqem.jobsplus;

import me.daqem.jobsplus.events.*;
import me.daqem.jobsplus.events.jobs.*;
import me.daqem.jobsplus.init.ModBlocks;
import me.daqem.jobsplus.init.ModEffects;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.init.ModPotions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

class SideProxy {

    SideProxy() {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.register(this);

        modEventBus.register(new EventRegisterCapabilities());
        modEventBus.register(new EventAttachCapabilities());
        modEventBus.register(new EventRegisterCommands());
        modEventBus.register(new EventServerChat());
        modEventBus.register(new EventNameFormat());
        modEventBus.register(new EventPlayerTick());
        modEventBus.register(new EventPlayerLoggedIn());

        modEventBus.register(new AlchemistEvents());
        modEventBus.register(new BuilderEvents());
        modEventBus.register(new DiggerEvents());
        modEventBus.register(new FarmerEvents());
        modEventBus.register(new FishermanEvents());
        modEventBus.register(new EnchanterEvents());
        modEventBus.register(new HunterEvents());
        modEventBus.register(new LumberjackEvents());
        modEventBus.register(new MinerEvents());
        modEventBus.register(new SmithEvents());

        ModItems.ITEMS.register(eventBus);
        ModBlocks.BLOCKS.register(eventBus);
        ModPotions.POTIONS.register(eventBus);
        ModEffects.EFFECTS.register(eventBus);

        modEventBus.addListener(EventClone::onDeath);
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModPotions::addPotionRecipes);
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
