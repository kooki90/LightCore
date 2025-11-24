package me.lime.lightCore;

import me.lime.lightCore.api.StartupMessage;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightCore extends JavaPlugin {

    @Override
    public void onEnable() {
        // Automatic - gets version, authors from plugin.yml
        StartupMessage.printWithAscii("LightCore", "&#FB7208");
    }

    @Override
    public void onDisable() {

    }
}
