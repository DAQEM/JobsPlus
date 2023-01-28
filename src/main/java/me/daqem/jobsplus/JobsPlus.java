package me.daqem.jobsplus;

import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG, "jobsplus-common.toml");
        DistExecutor.safeRunForDist(
                () -> SideProxy.Client::new,
                () -> SideProxy.Server::new
        );
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static MutableComponent translatable(String str) {
        return new TranslatableComponent("jobsplus." + str);
    }

    public static MutableComponent translatable(String str, Object... objects) {
        return new TranslatableComponent("jobsplus." + str, objects);
    }

    public static MutableComponent literal(String str) {
        return new KeybindComponent(str);
    }
}
