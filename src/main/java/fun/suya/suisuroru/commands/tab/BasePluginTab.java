package fun.suya.suisuroru.commands.tab;

import fun.suya.suisuroru.config.ConfigKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/10/15 03:01
 * function: Provides tab completion for the baseplugin command
 */
public class BasePluginTab implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("report");
            completions.add("config");
            completions.add("query-report");
        } else if (args.length == 2) {
            completions.add("reload");
            completions.add("query");
            completions.add("set");
        } else if (args.length == 3) {
            if (args[1].equals("query") || args[1].equals("set")) {
                completions.addAll(ConfigKeys.configKeysList.keySet());
            }
        }
        return completions;
    }
}