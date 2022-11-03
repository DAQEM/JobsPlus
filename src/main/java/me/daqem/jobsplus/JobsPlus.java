package me.daqem.jobsplus;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(JobsPlus.MOD_ID)
public class JobsPlus {

    public static final String MOD_ID = "jobsplus";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab TAB = new CreativeModeTab("jobsplustab") {
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(Items.IRON_HOE);
        }
    };

    public JobsPlus() {
        DistExecutor.safeRunForDist(
                () -> SideProxy.Client::new,
                () -> SideProxy.Server::new
        );
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static MutableComponent translatable(String str) {
        return Component.translatable("jobsplus." + str);
    }

    public static MutableComponent translatable(String str, Object... objects) {
        return Component.translatable("jobsplus." + str, objects);
    }
}
