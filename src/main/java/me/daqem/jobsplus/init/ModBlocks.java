package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = createRegister(ForgeRegistries.BLOCKS);
    public static final DeferredRegister<Item> ITEMS = createRegister(ForgeRegistries.ITEMS);

//    public static final RegistryObject<Block> LEVEL_25_TROPHY = register("level_25_trophy", () ->
//            new Block(BlockBehaviour.Properties.of(Material.STONE)));
//    public static final RegistryObject<Block> LEVEL_50_TROPHY = register("level_50_trophy", () ->
//            new Block(BlockBehaviour.Properties.of(Material.STONE)));
//    public static final RegistryObject<Block> LEVEL_100_TROPHY = register("level_100_trophy", () ->
//            new Block(BlockBehaviour.Properties.of(Material.STONE)));

//    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
//        RegistryObject<T> ret = registerNoItem(name, block);
//        ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(JobsPlus.TAB)));
//        return ret;
//    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> createRegister(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, JobsPlus.MOD_ID);
    }
}
