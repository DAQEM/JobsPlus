package me.daqem.jobsplus.data;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, JobsPlus.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleItem(ModItems.SMALL_BACKPACK.get());
        singleItem(ModItems.MEDIUM_BACKPACK.get());
        singleItem(ModItems.LARGE_BACKPACK.get());
        singleItem(ModItems.HUGE_BACKPACK.get());
        singleItem(ModItems.ENDER_BACKPACK.get());
    }

    private void singleItem(Item item) {
        String name = Objects.requireNonNull(item.getRegistryName()).getPath();
        singleTexture(name, mcLoc("item/handheld"), "layer0", modLoc("item/" + name));

    }
}
