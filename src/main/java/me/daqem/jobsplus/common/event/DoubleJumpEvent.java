package me.daqem.jobsplus.common.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class DoubleJumpEvent extends PlayerEvent {

    public DoubleJumpEvent(Player player) {
        super(player);
    }

    public static class SetCoyoteTime extends DoubleJumpEvent {

        private int coyoteTime;

        public SetCoyoteTime(Player player, int initialValue) {
            super(player);
            this.coyoteTime = initialValue;
        }

        public void addCoyoteTime(int ticks) {
            this.coyoteTime += ticks;
        }

        public int getCoyoteTime() {
            return coyoteTime;
        }

        public void setCoyoteTime(int ticks) {
            this.coyoteTime = ticks;
        }

        public void maximizeCoyoteTime(int ticks) {
            this.coyoteTime = Math.max(this.coyoteTime, ticks);
        }
    }

    public static abstract class CoyoteTimeJump extends DoubleJumpEvent {
        public CoyoteTimeJump(Player player) {
            super(player);
        }

        @Cancelable
        public static class Pre extends SpecialJump {
            private boolean jump;

            public Pre(Player player) {
                super(player);
            }

            @Override
            public boolean isCancelable() {
                return true;
            }
        }

        public static class Post extends SpecialJump {
            private boolean isHandled;

            public Post(Player player) {
                super(player);
            }

            /**
             * Whether or not the even has already been handled cosmetically,
             * meaning primarily applying cosmetic effects like particles and sounds
             *
             * @return whether the event has been handled
             */
            public boolean isHandled() {
                return this.isHandled;
            }

            /**
             * Indicate that the event has been handled cosmetically
             */
            public void setHandled() {
                this.isHandled = true;
            }
        }
    }

    public static class SpecialJump extends DoubleJumpEvent {
        private boolean jump;
        private Consumer<Player> callback;

        public SpecialJump(Player player) {
            super(player);
        }

        public void setJump(boolean jump) {
            this.jump = jump;
        }

        public boolean canJump() {
            return this.jump;
        }

        public boolean hasCallback() {
            return this.callback != null;
        }

        @Nullable
        public Consumer<Player> getCallback() {
            return this.callback;
        }

        public void setCallback(Consumer<Player> callback) {
            this.callback = callback;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    @Cancelable
    public static class SetMultiJumpCount extends DoubleJumpEvent {
        private int jumps;

        public SetMultiJumpCount(Player player) {
            super(player);
        }

        public int addJumps(int count) {
            return jumps += count;
        }

        public int getJumps() {
            return jumps;
        }

        public void setJumps(int count) {
            this.jumps = count;
        }

        public void maximizeJumps(int count) {
            this.jumps = Math.max(this.jumps, count);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static abstract class MultiJump extends DoubleJumpEvent {
        public MultiJump(Player player) {
            super(player);
        }

        @Cancelable
        public static class Pre extends MultiJump {
            public Pre(Player player) {
                super(player);
            }

            @Override
            public boolean isCancelable() {
                return true;
            }
        }

        public static class Post extends MultiJump {
            private boolean isHandled;

            public Post(Player player) {
                super(player);
            }

            /**
             * Whether or not the even has already been handled cosmetically,
             * meaning primarily applying cosmetic effects like particles and sounds
             *
             * @return whether the event has been handled
             */
            public boolean isHandled() {
                return this.isHandled;
            }

            /**
             * Indicate that the event has been handled cosmetically
             */
            public void setHandled() {
                this.isHandled = true;
            }
        }
    }

    /**
     * Fired whenever a players midair jump count would reset, usually when the player is on the ground.
     */
    public static class Reset extends DoubleJumpEvent {
        public Reset(Player player) {
            super(player);
        }
    }
}
