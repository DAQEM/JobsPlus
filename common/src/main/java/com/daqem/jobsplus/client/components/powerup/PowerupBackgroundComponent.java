package com.daqem.jobsplus.client.components.powerup;


import com.daqem.jobsplus.integration.arc.holder.holders.job.JobManager;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import com.daqem.uilib.client.gui.component.texture.RepeatingTextureComponent;
import com.daqem.uilib.client.gui.texture.Texture;

public class PowerupBackgroundComponent extends RepeatingTextureComponent {

    public PowerupBackgroundComponent(int x, int y, int width, int height, Powerup powerup) {
        super(new Texture(JobManager.getInstance().getJobs().get(powerup.getPowerupInstance().getJobLocation()).getPowerupBackground(), 0, 0, 32, 32, 32), x, y, width, height);
    }
}
