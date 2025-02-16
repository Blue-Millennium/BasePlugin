package fun.blue_millennium.commands.tab.othercommands.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static fun.blue_millennium.config.ConfigManager.getConfigFieldNames;

/**
 * @author Suisuroru
 * Date: 2024/10/18 22:11
 * function: Provides tab completion for the bpconfig command
 */
public class Config implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("reload");
            completions.add("query");
            completions.add("set");
        } else if (args.length == 2) {
            if (args[0].equals("query") || args[0].equals("set")) {
                completions.addAll(getConfigFieldNames());
            }
        }
        return completions;
    }
}
