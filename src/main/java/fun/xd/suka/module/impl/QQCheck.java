package fun.xd.suka.module.impl;

import fun.suya.suisuroru.config.Config;
import fun.xd.suka.Main;
import fun.xd.suka.data.Data;
import fun.xd.suka.data.PlayerData;
import fun.xd.suka.module.Module;
import fun.xd.suka.util.TimeUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QQCheck extends Module implements Listener {
    private static final HashMap<PlayerData, Integer> playerCodeMap = new HashMap<>();

    public QQCheck() {
        super("QQCheck");
    }

    public static void GroupCheck(GroupMessageEvent event, MessageChainBuilder builder) {
        if (Config.qqCheckEnabled) {
            int code = -1;
            try {
                code = Integer.parseInt(builder.build().contentToString().replace(Config.QQCheckStartWord, ""));
            } catch (Exception exception) {
                return;
            }
            event.getGroup().sendMessage(DataCheck(event, code));
        }
    }

    private static MessageChain DataCheck(MessageEvent event, int code) {
        MessageChain checkMessage = null;
        for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
            if (entry.getValue().equals(code)) {
                playerCodeMap.remove(entry.getKey());

                // 保存数据
                Data data = new Data();
                data.playerData = entry.getKey();
                data.qqNumber = event.getSender().getId();
                data.linkedTime = System.currentTimeMillis();
                Main.INSTANCE.dataManager.DATA_LIST.add(data);
                Main.INSTANCE.dataManager.save();

                // 构建确认消息
                checkMessage = new MessageChainBuilder()
                        .append("Your account was linked!").append("\n")
                        .append("Player Name: ").append(entry.getKey().playerName).append("\n")
                        .append("Linked QQ: ").append(String.valueOf(event.getSender().getId())).append("\n")
                        .append("Linked Time: ").append(TimeUtil.getNowTime())
                        .build();
            }
        }
        return checkMessage;
    }

    @Override
    public void onEnable() {
        Main.INSTANCE.eventChannel.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);

        Main.INSTANCE.eventChannel.subscribeAlways(FriendMessageEvent.class, event -> {
            String message = event.getMessage().contentToString();

            if (Config.qqCheckEnabled) {
                int code = -1;
                try {
                    code = Integer.parseInt(message);
                } catch (Exception exception) {
                    return;
                }
                event.getSender().sendMessage(DataCheck(event, code));
            }
        });
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) {
            return;
        }

        if (Config.qqCheckEnabled) {
            Data data = Main.INSTANCE.dataManager.getPlayerData(event.getUniqueId());
            // 首次登录
            if (data == null) {
                int code;

                // 生成不重复的验证码
                do {
                    for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
                        if (entry.getKey().playerUuid.equals(event.getUniqueId())) {
                            playerCodeMap.remove(entry.getKey());
                        }
                    }
                    code = Math.abs(new Random().nextInt(100000));
                } while (playerCodeMap.containsValue(code));

                // 加入等待列表
                PlayerData playerData = new PlayerData();
                playerData.playerName = event.getName();
                playerData.playerUuid = event.getUniqueId();
                playerCodeMap.put(playerData, code);

                // 拒绝加入服务器
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Config.disTitle.replace("%CODE%", String.valueOf(code)));
                return;
            }

            // 设置首次登陆数据
            if (data.firstJoin < 0) {
                data.firstJoin = System.currentTimeMillis();
            }
            if (data.firstJoinIp == null) {
                data.firstJoinIp = event.getAddress().getHostAddress();
            }

            data.lastJoin = System.currentTimeMillis();
            data.lastJoinIp = event.getAddress().getHostAddress();

            Main.INSTANCE.dataManager.setPlayerData(event.getUniqueId(), data);
        }
    }
}
