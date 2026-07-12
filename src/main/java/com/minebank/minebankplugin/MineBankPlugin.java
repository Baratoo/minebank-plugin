package com.minebank.minebankplugin;

import com.minebank.minebankplugin.client.MineBankApiClient;
import com.minebank.minebankplugin.command.LojaCommand;
import com.minebank.minebankplugin.command.SaldoCommand;
import com.minebank.minebankplugin.listener.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MineBankPlugin extends JavaPlugin{

    private MineBankApiClient apliClient;

    @Override
    public void onEnable() {

        this.apliClient = new MineBankApiClient();

        //Registra eventos
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(apliClient, getLogger()), this);

        //Registra comandos com nome salvo no yml
        getCommand("saldo").setExecutor(new SaldoCommand(apliClient));
        getCommand("loja").setExecutor(new LojaCommand(this, apliClient));

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
