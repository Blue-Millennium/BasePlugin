package fun.blue_millennium.commands.execute.othercommands.sub.config;

import fun.blue_millennium.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.blue_millennium.Main.LOGGER;
import static fun.blue_millennium.config.ConfigManager.getConfigFieldNames;
import static fun.blue_millennium.util.CommandOperatorCheck.checkNotOperator;

/**
 * @author Suisuroru
 * Date: 2024/10/15 02:06
 * function: Set new config
 */
public class Set implements CommandExecutor {
    ConfigManager configManager = new ConfigManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (checkNotOperator(sender)) {
            return true;
        }

        if (args.length == 1) {
            if (command.getName().equals("cm")) {
                sender.sendMessage("§c/cm config set [修改参数] [修改值]");
            } else if (command.getName().equals("chamomile")) {
                sender.sendMessage("§c/chamomile config set [修改参数] [修改值]");
            }
            String allConfigNames = String.join("|", getConfigFieldNames());
            sender.sendMessage("§a所有配置项名称: " + allConfigNames);
            return true;
        }

        String configName = args[0];
        String value = args[1];

        // 检查配置项是否存在
        if (!getConfigFieldNames().contains(configName)) {
            sender.sendMessage("§c配置项 " + configName + " 不存在，请检查拼写");
            return true;
        }

        try {
            configManager.setConfigValue(configName, value);
            sender.sendMessage("§a配置项 " + configName + " 已成功设置为 " + value);
        } catch (Exception e) {
            sender.sendMessage("§c修改配置项 " + configName + " 的值时出错，请检查配置文件或联系开发人员。");
            LOGGER.warning(e.getMessage());
            return true;
        }

        return true;
    }

}
