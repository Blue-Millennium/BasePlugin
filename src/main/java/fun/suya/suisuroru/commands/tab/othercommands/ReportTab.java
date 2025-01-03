package fun.suya.suisuroru.commands.tab.othercommands;

import fun.suya.suisuroru.module.impl.OnlinePlayerListGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/10/15 01:41
 * function: Provides tab completion for the report command
 */
public class ReportTab implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // 返回所有在线玩家的名字
            completions.addAll(OnlinePlayerListGet.GetOnlinePlayerList());
        } else if (args.length >= 2) {
            // 不返回任何补全结果
            return completions; // 返回空列表
        }
        return completions;
    }
}
