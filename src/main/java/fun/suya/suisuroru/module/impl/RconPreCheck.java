package fun.suya.suisuroru.module.impl;

import fun.suya.suisuroru.config.Config;
import fun.xd.suka.Main;
import fun.xd.suka.module.Module;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static fun.suya.suisuroru.data.Report.ReportCharmProcess.reportCharmProcess;
import static fun.suya.suisuroru.rcon.RconCommandExecute.executeRconCommand;
import static fun.xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/9/28 16:06
 * function: Check message if need rcon function
 */
public class RconPreCheck extends Module implements Listener {
    private List<Long> EnabledGroups = new ArrayList<>();

    public RconPreCheck() {
        super("DirectConsoleCommandCheck");
    }

    @Override
    public void onEnable() {
        String enabledGroupStr = Config.RconEnabledGroups;
        if (enabledGroupStr == null || enabledGroupStr.isEmpty()) {
            LOGGER.warning("[RCONCommandCheck] RCON commands will be disabled due to empty or null RCONEnabledGroups");
            Config.RconEnabled = false;
            Main.INSTANCE.configManager.save();
            return;
        }

        String[] groupIds = enabledGroupStr.split(";");
        for (String groupId : groupIds) {
            try {
                long id = Long.parseLong(groupId.trim());
                EnabledGroups.add(id);
            } catch (NumberFormatException e) {
                LOGGER.warning("[RCONCommandCheck] Invalid group ID: " + groupId);
            }
        }

        if (EnabledGroups.isEmpty()) {
            LOGGER.warning("[RCONCommandCheck] RCON commands will be disabled");
            Config.RconEnabled = false;
            Main.INSTANCE.configManager.save();
            return;
        }

        Main.INSTANCE.eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
            if (!Config.RconEnabled || !EnabledGroups.contains(event.getGroup().getId())) {
                return;
            }

            Message message = event.getMessage();
            String content = message.contentToString();

            if (content.startsWith(Config.ExecuteCommandPrefix)) {
                int prefixLength = Config.ExecuteCommandPrefix.length();
                String command = content.substring(prefixLength);
                boolean isOperator = event.getSender().getPermission().equals(net.mamoe.mirai.contact.MemberPermission.ADMINISTRATOR)
                        || event.getSender().getPermission().equals(net.mamoe.mirai.contact.MemberPermission.OWNER);
                if (Config.RconEnforceOperator) {
                    if (!isOperator) {
                        event.getGroup().sendMessage(new MessageChainBuilder()
                                .append(new PlainText("您没有权限执行此操作。"))
                                .build());
                        return;
                    }
                }
                String[] result = executeRconCommand(command);
                handleConsoleResult(result, event);
            }
        });
    }

    private void handleConsoleResult(String[] result, GroupMessageEvent event) {
        try {
            if (result != null && !result[0].isEmpty()) {
                MessageChainBuilder message = new MessageChainBuilder();
                message.append(new PlainText(Config.servername + "Console command result: \n"))
                        .append(result[0]);
                String PREFIX = "[BasePlugin Report]\n已查询到以下数据，下面的数据将按照以下顺序排列\n";
                if (!result[1].isEmpty() && result[1].startsWith(PREFIX)) {
                    reportCharmProcess(result[1].substring(PREFIX.length()));
                    message.append(PREFIX)
                            .append(event.getGroup().uploadImage(ExternalResource.create(new File(".\\BasePlugin\\CharmProcess\\latest.png"))));
                } else {
                    message.append(result[1]);
                }
                event.getGroup().sendMessage(message.build());
            } else {
                event.getGroup().sendMessage(new MessageChainBuilder()
                        .append(new PlainText(Config.servername + "No result from console command."))
                        .build());
            }
        } catch (Exception e) {
            event.getGroup().sendMessage(new MessageChainBuilder()
                    .append(new PlainText(Config.servername + "\n[ERROR] " + e.getMessage()))
                    .build());
        }
    }
}
