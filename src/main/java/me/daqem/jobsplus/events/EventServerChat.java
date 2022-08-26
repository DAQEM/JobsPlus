package me.daqem.jobsplus.events;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventServerChat {

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
//        if (ModList.get().isLoaded("ftbranks")) return;

        //Isn't working becuase of the new 1.19 Message Signatures.

//        Component component = event.getComponent();
//        if (event.getPlayer() != null) {
//            Player player = event.getPlayer();
//            if (Config.FORMAT_CHAT.get()) {
//                if (JobGetters.getDisplay(player) == 0) {
//                    event.setComponent(Component.keybind(ChatColor.gray() + player.getScoreboardName() +
//                            ChatColor.darkGray() + " > " + ChatColor.white() + event.getMessage()));
//                } else {
//                    for (Jobs job : Jobs.values()) {
//                        if (job.get() == JobGetters.getDisplay(player) - 1) {
//                            StringBuilder stringBuilder = new StringBuilder();
//                            stringBuilder.append(ChatColor.boldWhite()).append("Levels:").append(ChatColor.reset());
//                            for (Jobs job1 : Jobs.values()) {
//                                if (JobGetters.getJobLevel(player, job1) > 0) {
//                                    stringBuilder.append("\n").append(ChatHandler.ColorizedJobName(job1)).append(ChatColor.boldDarkGray()).append("> ").append(ChatColor.white()).append(JobGetters.getJobLevel(player, job1));
//                                }
//                            }
//                            Component newComponent = Component.keybind("")
//                                    .append(Component.keybind(ChatHandler.ColorizedJobName(job))
//                                            .withStyle(component.getStyle()
//                                                    .withHoverEvent(
//                                                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.keybind(stringBuilder.toString())))))
//                                    .append(ChatColor.gray())
//                                    .append(Component.keybind(player.getScoreboardName())
//                                            .withStyle(component.getStyle()
//                                                    .withHoverEvent(
//                                                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.keybind(ChatColor.boldGreen() + player.getScoreboardName() + "\nCoins: " + ChatColor.white() + JobGetters.getCoins(player))))
//                                                    .withClickEvent(
//                                                            new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + player.getScoreboardName() + " "))))
//                                    .append(ChatColor.darkGray() + " > " + ChatColor.white());
//
//                            for (int i = 3; i < component.copy().toFlatList(component.getStyle()).size(); ++i) {
//                                newComponent = newComponent.copy().append(component.copy().toFlatList(component.getStyle()).get(i));
//                            }
//                            event.setComponent(newComponent);
//                            return;
//                        }
//                    }
//                }
//            }
//        }
    }
}
