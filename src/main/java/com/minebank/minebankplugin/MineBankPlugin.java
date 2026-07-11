package com.minebank.minebankplugin;

import com.minebank.minebankplugin.client.MineBankApiClient;
import com.minebank.minebankplugin.listener.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MineBankPlugin extends JavaPlugin{

    private MineBankApiClient apliClient;

    @Override
    public void onEnable() {

        this.apliClient = new MineBankApiClient();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(apliClient, getLogger()), this);

        //Roda sozinho assim que o servidor inicia
        getLogger().info("Plugin iniciado...");
        getLogger().info("Eventos registrados e Cliente HTTP pronto.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin desligando...");
    }

    //Outras funcoes sempre buscam daqui
    public MineBankApiClient getApliClient() {
        return apliClient;
    }
}
