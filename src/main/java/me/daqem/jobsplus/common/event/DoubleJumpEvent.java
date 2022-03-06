package me.daqem.jobsplus.common.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class DoubleJumpEvent extends PlayerEvent {

    public DoubleJumpEvent(Player player) {
        super(player);
    }

    public static abstract class MultiJump extends DoubleJumpEvent {
        public MultiJump(Player player) {
            super(player);
        }

        public static class Post extends MultiJump {

            public Post(Player player) {
                super(player);
            }

        }
    }
}
