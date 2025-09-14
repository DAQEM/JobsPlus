package com.daqem.jobsplus.client.gui.jobs.components.conditions;

import com.daqem.arc.api.condition.ICondition;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;

public class DefaultConditionComponent extends AbstractConditionComponent {

    public DefaultConditionComponent(ICondition condition) {
        MultiLineTextComponent nameComponent = new MultiLineTextComponent(10, 0, getWidth() - 10, condition.getName(), 0xFF1E1410);
        MultiLineTextComponent descriptionComponent = new MultiLineTextComponent(10, nameComponent.getHeight(), getWidth() - 10, condition.getDescription(), 0xFFD8BF96);

        this.addComponent(nameComponent);
        this.addComponent(descriptionComponent);

        this.setHeight(nameComponent.getHeight() + descriptionComponent.getHeight());
    }
}
