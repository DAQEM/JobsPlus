package com.daqem.jobsplus.client.gui.jobs.components.conditions;

import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.data.condition.NotCondition;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;

public class NotConditionComponent extends AbstractConditionComponent {

    public NotConditionComponent(NotCondition notCondition, ICondition condition) {
        MultiLineTextComponent notText = new MultiLineTextComponent(10, 0, getWidth() - 10, notCondition.getName().copy().append(" ").append(condition.getName()), 0xFF1E1410);
        this.addComponent(notText);
        this.setHeight(notText.getHeight());
    }
}
