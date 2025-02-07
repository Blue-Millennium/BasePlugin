package fun.blue_millennium.module.impl;

import fun.blue_millennium.Main;
import fun.blue_millennium.config.Config;
import fun.blue_millennium.module.Module;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static fun.blue_millennium.Main.LOGGER;
import static fun.blue_millennium.message.ImageProcess.sendImageUrl;

public class SyncChat extends Module implements Listener {
    private Group syncGroup = null;

    public SyncChat() {
        super("SyncChat");
    }

    @Override
    public void onEnable() {
        syncGroup = Main.INSTANCE.BOT.getGroup(Config.SyncChatGroup);

        if (!Config.BotModeOfficial) {
            if (syncGroup == null) {
                LOGGER.warning("Failed to get sync group");
                Config.SyncChatEnabled = false;
                Main.INSTANCE.configManager.save();
                return;
            }
        }

        Main.INSTANCE.eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
            if (!Config.BotModeOfficial & (!Config.SyncChatEnabled || event.getGroup() != syncGroup)) {
                return;
            }
            MessageChainBuilder builder = new MessageChainBuilder();
            for (Message message : event.getMessage()) {
                if (message instanceof PlainText || message instanceof At || message instanceof AtAll) {
                    builder.add(message);
                } else if (message instanceof Image image) {
                    sendImageUrl(image, event);
                }
            }

            if (!builder.isEmpty()) {
                if (Config.BotModeOfficial & builder.build().contentToString().replace("/", "").replace(" ", "").startsWith(Config.SyncChatStartWord)) {
                    String message = Config.SayQQMessage.replace("%NAME%", event.getSenderName()).replace(Config.SyncChatStartWord, "").replace("%MESSAGE%", builder.build().contentToString());
                    Main.INSTANCE.getServer().broadcastMessage(message);
                    event.getGroup().sendMessage("已成功发送消息至服务器，以下为发送至服务器的原始数据：\n" + message);
                } else if (!Config.BotModeOfficial) {
                    Main.INSTANCE.getServer().broadcastMessage(Config.SayQQMessage.replace("%NAME%", event.getSenderName()).replace("%MESSAGE%", builder.build().contentToString()));
                }
                if (builder.build().contentToString().replace("/", "").replace(" ", "").startsWith(Config.QQCheckStartWord)) {
                    QQCheck.GroupCheck(event, builder);
                }
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Config.SyncChatEnabled & !Config.SyncChatEnabledQ2SOnly & !Config.BotModeOfficial) {
            syncGroup.sendMessage(Config.JoinServerMessage.replace("%NAME%", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (Config.SyncChatEnabled & !Config.SyncChatEnabledQ2SOnly & !Config.BotModeOfficial) {
            syncGroup.sendMessage(Config.LeaveServerMessage.replace("%NAME%", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (Config.SyncChatEnabled & !Config.SyncChatEnabledQ2SOnly & !Config.BotModeOfficial) {
            syncGroup.sendMessage(Config.SayServerMessage.replace("%NAME%", event.getPlayer().getName()).replace("%MESSAGE%", event.getMessage()));
        }
    }
}
