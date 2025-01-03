package fun.xd.suka.module;

import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.module.impl.RconPreCheck;
import fun.xd.suka.Main;
import fun.xd.suka.module.impl.QQCheck;
import fun.xd.suka.module.impl.Reporter;
import fun.xd.suka.module.impl.SyncChat;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class ModuleManager {
    public ArrayList<Module> modules = new ArrayList<>();

    public void load() {
        if (Config.QQRobotEnabled) {
            modules.add(new SyncChat());
            modules.add(new QQCheck());
            modules.add(new RconPreCheck());
        }
        modules.add(new Reporter());

        modules.forEach(Module::onLoad);
    }

    public void onEnable() {
        if (Config.QQRobotEnabled) {
            modules.forEach(module -> Bukkit.getPluginManager().registerEvents(module, Main.INSTANCE));
            modules.forEach(Module::onEnable);
        }
    }

    public void onDisable() {
        modules.forEach(Module::onDisable);
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.moduleName.equals(name)) {
                return module;
            }
        }
        return null;
    }
}
