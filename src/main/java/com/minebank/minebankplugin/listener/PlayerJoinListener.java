package com.minebank.minebankplugin.listener;

import com.minebank.minebankplugin.client.MineBankApiClient;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Logger;

public class PlayerJoinListener implements Listener {

    private final MineBankApiClient apiClient;
    private final Logger logger;

    public PlayerJoinListener(MineBankApiClient apiClient, Logger logger) {
        this.apiClient = apiClient;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String nickName = player.getName();

        logger.info("Enviando jogador para API: " + nickName);

        apiClient.registerPlayer(uuid, nickName)
                .thenAccept(response -> {logger.info("SucessoAPI: " + nickName + response);})
                .exceptionally(ex -> {
                    logger.severe("Falha ao registrar jogador " + nickName + ex.getMessage());
                    player.sendMessage("[MineBank] O sistema está temporariamente indisponivel");
                    return null;
                });
    }

}
