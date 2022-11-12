package me.daqem.jobsplus.common.data.generation;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModDataGenerator {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        dataGenerator.addProvider(new BlockModels(dataGenerator, existingFileHelper));
        dataGenerator.addProvider(new ItemModels(dataGenerator, existingFileHelper));
    }
}
