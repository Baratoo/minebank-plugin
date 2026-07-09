package com.minebank.minebankplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class MineBankPlugin extends JavaPlugin{

    @Override
    public void onEnable() {
        //Roda sozinho assim que o servidor inicia
        getLogger().info("Plugin iniciado...");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin desligando...");
    }
}
