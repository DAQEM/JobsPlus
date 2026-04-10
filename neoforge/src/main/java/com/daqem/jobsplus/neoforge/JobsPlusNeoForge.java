package com.daqem.jobsplus.neoforge;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.command.arguments.EnumArgument;
import com.daqem.jobsplus.command.arguments.JobArgument;
import com.daqem.jobsplus.command.arguments.PowerupArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(JobsPlus.MOD_ID)
public class JobsPlusNeoForge {

    public JobsPlusNeoForge(IEventBus modEventBus) {
        JobsPlus.init();
        registerCommandArgumentTypes(modEventBus);
    }

    private void registerCommandArgumentTypes(IEventBus modEventBus) {
        DeferredRegister<ArgumentTypeInfo<?, ?>> argTypeRegistry = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, JobsPlus.MOD_ID);
        argTypeRegistry.register("job", () -> ArgumentTypeInfos.registerByClass(JobArgument.class, SingletonArgumentInfo.contextFree(JobArgument::job)));
        argTypeRegistry.register("powerup", () -> ArgumentTypeInfos.registerByClass(PowerupArgument.class, SingletonArgumentInfo.contextFree(PowerupArgument::powerup)));
        //noinspection rawtypes,unchecked
        argTypeRegistry.register("enum", () -> ArgumentTypeInfos.registerByClass(EnumArgument.class, new EnumArgument.Info()));
        argTypeRegistry.register(modEventBus);
    }
}
