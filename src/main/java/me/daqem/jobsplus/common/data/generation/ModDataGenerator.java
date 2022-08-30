package me.daqem.jobsplus.common.data.generation;

import me.daqem.jobsplus.common.data.generation.BlockModels;
import me.daqem.jobsplus.common.data.generation.ItemModels;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModDataGenerator {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        dataGenerator.addProvider(true, new BlockModels(dataGenerator, existingFileHelper));
        dataGenerator.addProvider(true, new ItemModels(dataGenerator, existingFileHelper));
    }
}
