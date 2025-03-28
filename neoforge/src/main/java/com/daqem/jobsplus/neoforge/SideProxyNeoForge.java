package com.daqem.jobsplus.neoforge;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.JobsPlusClient;
import com.daqem.jobsplus.command.arguments.EnumArgument;
import com.daqem.jobsplus.command.arguments.JobArgument;
import com.daqem.jobsplus.command.arguments.PowerupArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SideProxyNeoForge {

    protected final IEventBus modEventBus;
    protected final IEventBus eventBus;

    SideProxyNeoForge(IEventBus modEventBus) {
        this.eventBus = NeoForge.EVENT_BUS;
        this.modEventBus = modEventBus;

        registerCommandArgumentTypes();
    }

    private void registerCommandArgumentTypes() {
        DeferredRegister<ArgumentTypeInfo<?, ?>> argTypeRegistry = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, JobsPlus.MOD_ID);
        argTypeRegistry.register("job", () -> ArgumentTypeInfos.registerByClass(JobArgument.class, SingletonArgumentInfo.contextFree(JobArgument::job)));
        argTypeRegistry.register("powerup", () -> ArgumentTypeInfos.registerByClass(PowerupArgument.class, SingletonArgumentInfo.contextFree(PowerupArgument::powerup)));
        //noinspection rawtypes,unchecked
        argTypeRegistry.register("enum", () -> ArgumentTypeInfos.registerByClass(EnumArgument.class, new EnumArgument.Info()));
        argTypeRegistry.register(modEventBus);
    }

    public static class Server extends SideProxyNeoForge {
        Server(IEventBus modEventBus) {
            super(modEventBus);
        }

    }

    public static class Client extends SideProxyNeoForge {

        Client(IEventBus modEventBus) {
            super(modEventBus);
            JobsPlusClient.init();

            registerEvents();
        }

        private void registerEvents() {
            this.modEventBus.addListener(this::registerKeyBindings);
        }

        private void registerKeyBindings(RegisterKeyMappingsEvent event) {
            event.register(JobsPlusClient.OPEN_MENU);
        }
    }
}
