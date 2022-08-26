package me.daqem.jobsplus;

import com.mojang.blaze3d.platform.InputConstants;
import me.daqem.jobsplus.client.renderer.entity.ModFishingHookRenderer;
import me.daqem.jobsplus.common.container.backpack.BackpackScreen;
import me.daqem.jobsplus.common.container.construction.ConstructionScreen;
import me.daqem.jobsplus.data.ModDataGenerator;
import me.daqem.jobsplus.events.*;
import me.daqem.jobsplus.events.item.CurseBreakEvents;
import me.daqem.jobsplus.events.item.FarmersHoeEvents;
import me.daqem.jobsplus.events.jobs.*;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.init.*;
import me.daqem.jobsplus.utils.HeadData;
import me.daqem.jobsplus.utils.JobItemEntry;
import me.daqem.jobsplus.utils.JobItemEntryHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class SideProxy {

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
        modEventBus.register(new EventServerStart());

        modEventBus.register(new FarmersHoeEvents());
        modEventBus.register(new CurseBreakEvents());

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

        modEventBus.register(new DoubleJumpEvents());

        ModItems.ITEMS.register(eventBus);
        ModBlocks.BLOCKS.register(eventBus);
        ModPotions.POTIONS.register(eventBus);
        ModEffects.EFFECTS.register(eventBus);
        ModRecipes.RECIPES.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModMenuTypes.MENU_TYPES.register(eventBus);

        modEventBus.addListener(EventClone::onDeath);
        eventBus.addListener(ModDataGenerator::gatherData);

        ModPacketHandler.init();
        HeadData.loadHeadData();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModPotions::addPotionRecipes);
    }

    public static class Server extends SideProxy {
        Server() {

        }

    }

    public static class Client extends SideProxy {

        public static final KeyMapping OPEN_GUI_KEYBIND = new KeyMapping("keys.jobsplus.open_gui", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, "keys.jobsplus.category");
        public static final KeyMapping VEIN_MINER_KEYBIND = new KeyMapping("keys.jobsplus.vein_miner", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "keys.jobsplus.category");
        public static final KeyMapping DOUBLE_JUMP_KEYBIND = new KeyMapping("keys.jobsplus.double_jump", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, "keys.jobsplus.category");

        public static ArrayList<JobItemEntry> entries = new ArrayList<>();

        Client() {
            IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
            MinecraftForge.EVENT_BUS.register(new EventKeyInput());
            eventBus.addListener(this::clientStuff);
            eventBus.addListener(this::registerKeyMappings);
            eventBus.addListener(ModItemProperties::clientSetupHandler);
        }

        public static ArrayList<JobItemEntry> getEntries() {
            return entries;
        }

        public static void setEntries(CompoundTag tag) {
            Client.entries = JobItemEntryHelper.fromTag(tag);
        }

        private void clientStuff(final FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.BACKPACK.get(), BackpackScreen::new);
            MenuScreens.register(ModMenuTypes.CONSTRUCTION.get(), ConstructionScreen::new);

            EntityRenderers.register(ModEntities.FISHING_BOBBER.get(), ModFishingHookRenderer::new);
            EntityRenderers.register(ModEntities.EXPERIENCE_BOTTLE.get(), ThrownItemRenderer::new);
        }

        private void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(OPEN_GUI_KEYBIND);
            event.register(VEIN_MINER_KEYBIND);
            event.register(DOUBLE_JUMP_KEYBIND);
        }
    }
}
