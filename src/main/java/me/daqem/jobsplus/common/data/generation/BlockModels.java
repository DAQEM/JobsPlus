package me.daqem.jobsplus.common.data.generation;

import me.daqem.jobsplus.JobsPlus;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModels extends BlockModelProvider {

    public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, JobsPlus.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        cube("construction_table",
                mcLoc("block/spruce_planks"),
                JobsPlus.getId("block/construction_table_top"),
                JobsPlus.getId("block/construction_table_left"),
                JobsPlus.getId("block/construction_table_left"),
                JobsPlus.getId("block/construction_table_right"),
                JobsPlus.getId("block/construction_table_right"))
                .texture("particle", JobsPlus.getId("block/construction_table_left"));
    }
}
