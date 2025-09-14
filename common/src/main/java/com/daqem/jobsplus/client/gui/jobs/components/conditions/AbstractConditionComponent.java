package com.daqem.jobsplus.client.gui.jobs.components.conditions;

import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import net.minecraft.network.chat.Component;

public class AbstractConditionComponent extends EmptyComponent {

    public AbstractConditionComponent() {
        super(0, 0, 99, 0);

        TextComponent dotText = new TextComponent(0, 0, Component.literal(" • "), 0xFF1E1410);
        this.addComponent(dotText);
    }
}
