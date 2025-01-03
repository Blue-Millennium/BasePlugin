package fun.suya.suisuroru.commands;

import fun.suya.suisuroru.commands.execute.CommandManager;
import fun.suya.suisuroru.commands.execute.othercommands.ConfigRoot;
import fun.suya.suisuroru.commands.execute.othercommands.DataRoot;
import fun.suya.suisuroru.commands.execute.othercommands.Help;
import fun.suya.suisuroru.commands.execute.othercommands.ReportQuery;
import fun.suya.suisuroru.commands.execute.othercommands.config.Reload;
import fun.suya.suisuroru.commands.execute.vanilla.Ban;
import fun.suya.suisuroru.commands.execute.vanilla.Kill;
import fun.suya.suisuroru.commands.execute.vanilla.Pardon;
import fun.suya.suisuroru.commands.tab.othercommands.BasePluginTab;
import fun.suya.suisuroru.commands.tab.othercommands.ConfigTab;
import fun.suya.suisuroru.commands.tab.othercommands.DataTab;
import fun.suya.suisuroru.commands.tab.othercommands.ReportTab;
import fun.suya.suisuroru.commands.tab.vanilla.BanTab;
import fun.suya.suisuroru.commands.tab.vanilla.KillTab;
import fun.suya.suisuroru.commands.tab.vanilla.PardonTab;
import fun.suya.suisuroru.config.Config;
import fun.xd.suka.command.ReportCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegister {
    public static void registerCommand(JavaPlugin plugin) {
        if (Config.VanillaCommandsRewritten) {
            // vanilla functions
            plugin.getCommand("ban").setExecutor(new Ban());
            plugin.getCommand("ban").setTabCompleter(new BanTab());
            plugin.getCommand("pardon").setExecutor(new Pardon());
            plugin.getCommand("pardon").setTabCompleter(new PardonTab());
            plugin.getCommand("kill").setExecutor(new Kill());
            plugin.getCommand("kill").setTabCompleter(new KillTab());
        }
        // new functions
        plugin.getCommand("basepluginhelp").setExecutor(new Help());
        plugin.getCommand("report").setExecutor(new ReportCommand());
        plugin.getCommand("report").setTabCompleter(new ReportTab());
        plugin.getCommand("bpconfig").setExecutor(new ConfigRoot());
        plugin.getCommand("bpconfig").setTabCompleter(new ConfigTab());
        plugin.getCommand("bpreload").setExecutor(new Reload());
        plugin.getCommand("baseplugin").setExecutor(new CommandManager());
        plugin.getCommand("baseplugin").setTabCompleter(new BasePluginTab());
        plugin.getCommand("query-report").setExecutor(new ReportQuery());
        plugin.getCommand("bpdata").setExecutor(new DataRoot());
        plugin.getCommand("bpdata").setTabCompleter(new DataTab());
    }
}
