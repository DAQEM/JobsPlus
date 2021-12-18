package me.daqem.jobsplus.utils.enums;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;

public enum Backpack {
    SMALL("Small", Rarity.COMMON, 9, 1, 9, "small_backpack_gui.png", 176, 132, 7, 49, ModItems.SMALL_BACKPACK),
    MEDIUM("Medium", Rarity.UNCOMMON, 18, 2, 9, "medium_backpack_gui.png", 176, 150, 7, 67, ModItems.MEDIUM_BACKPACK),
    LARGE("Large", Rarity.RARE, 36, 4, 9, "large_backpack_gui.png", 176, 186, 7, 103, ModItems.LARGE_BACKPACK),
    HUGE("Huge", Rarity.EPIC, 54, 6, 9, "huge_backpack_gui.png", 176, 222, 7, 139, ModItems.HUGE_BACKPACK),
    ENDER("Ender", Rarity.RARE, 27, 3, 9, "", 176, 222, 7, 85, ModItems.ENDER_BACKPACK);

    public final Rarity rarity;
    public final int slots;

    public final ResourceLocation texture;
    public final int xSize;
    public final int ySize;
    //offset from left edge of texture, to left edge of first player inventory slot.
    public final int slotXOffset;
    //offset from left edge of texture, to left edge of first player inventory slot.
    public final int slotYOffset;
    public final int slotRows;
    public final int slotCols;
    public final String name;
    public final RegistryObject<Item> item;

    Backpack(String name, Rarity rarity, int slots, int rows, int cols, String location, int xSize, int ySize, int slotXOffset, int slotYOffset, RegistryObject<Item> itemIn) {
        this.name = name;
        this.rarity = rarity;
        this.slots = slots;
        this.slotRows = rows;
        this.slotCols = cols;
        this.texture = new ResourceLocation(JobsPlus.MOD_ID, "textures/gui/container/" + location);
        this.xSize = xSize;
        this.ySize = ySize;
        this.slotXOffset = slotXOffset;
        this.slotYOffset = slotYOffset;
        this.item = itemIn;
    }
}
