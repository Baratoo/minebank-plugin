package com.minebank.minebankplugin.command;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minebank.minebankplugin.client.MineBankApiClient;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SaldoCommand implements CommandExecutor {

    private final MineBankApiClient apiClient;

    public SaldoCommand(MineBankApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)){
            sender.sendMessage("Apenas jogadores podem usar esse comando!");
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "Consultando saldo...");

        apiClient.getSaldo(player.getUniqueId().toString())
                .thenAccept(responseJson -> {
                    try {
                        JsonObject json = JsonParser.parseString(responseJson).getAsJsonObject();
                        double saldo = json.get("balance").getAsDouble();
                        player.sendMessage(ChatColor.GREEN + "Saldo atual: §f$" + String.format("%.2f", saldo));
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Erro ao buscar carteira");
                        e.printStackTrace();
                    }
                }).exceptionally(ex -> {
                    player.sendMessage(ChatColor.RED + "Sistema fora do ar");
                    return null;
                });
        return true;
    }
}
