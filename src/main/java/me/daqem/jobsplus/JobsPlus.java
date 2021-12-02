package me.daqem.jobsplus;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(JobsPlus.MOD_ID)
public class JobsPlus {

    public static final String MOD_ID = "jobsplus";
    public static final Logger LOGGER = LogManager.getLogger();

    public JobsPlus() {
        DistExecutor.runForDist(
                () -> SideProxy.Client::new,
                () -> SideProxy.Server::new
        );
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
