package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.block.ConstructionTableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JobsPlus.MOD_ID);

    public static final RegistryObject<Block> CONSTRUCTION_TABLE = BLOCKS.register("construction_table", () -> new ConstructionTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));

}
