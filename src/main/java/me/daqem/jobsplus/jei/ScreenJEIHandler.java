package me.daqem.jobsplus.jei;

import me.daqem.jobsplus.common.container.construction.ConstructionScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ScreenJEIHandler implements IGuiContainerHandler<ConstructionScreen> {

    /**
     * Prevents JEI from rendering Items over the GUI.
     */
    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(@NotNull ConstructionScreen containerScreen) {
        return List.of(new Rect2i(0, 0, containerScreen.width,
                containerScreen.height > 245 ? containerScreen.height / 2 + 24 : containerScreen.height / 2 + 12 + 18));
    }
}
