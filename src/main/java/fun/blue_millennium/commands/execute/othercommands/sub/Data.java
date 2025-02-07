package fun.blue_millennium.commands.execute.othercommands.sub;

import fun.blue_millennium.commands.execute.othercommands.sub.data.Bind;
import fun.blue_millennium.commands.execute.othercommands.sub.data.Query;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static fun.blue_millennium.util.CommandOperatorCheck.checkNotOperator;

public class Data implements CommandExecutor {
    Query query = new Query();
    Bind bind = new Bind();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender.isOp()) {
                sender.sendMessage("§e在下方的指令中，您可以使用cm来代替输入chamomile");
                sender.sendMessage("§e查询绑定数据:使用/chamomile data query <依据>");
                sender.sendMessage("§e绑定数据:使用/chamomile data bind <游戏ID> <QQ号>");
            } else if (checkNotOperator(sender)) {
                return true;
            }
        }
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0].toLowerCase()) {
            case "query": {
                query.onCommand(sender, command, label, subArgs);
                break;
            }
            case "bind": {
                bind.onCommand(sender, command, label, subArgs);
                break;
            }
            default: {
                sender.sendMessage("Unknown command. Usage: /chamomile data query [args...]");
                break;
            }
        }
        return true;
    }
}
