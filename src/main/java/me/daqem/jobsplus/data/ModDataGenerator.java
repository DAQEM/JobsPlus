package me.daqem.jobsplus.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModDataGenerator {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();

        dataGenerator.addProvider(true, new ModRecipeProvider(dataGenerator));
        dataGenerator.addProvider(true, new ItemModels(dataGenerator, event.getExistingFileHelper()));
    }
}
