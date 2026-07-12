package com.minebank.minebankplugin.command;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.minebank.minebankplugin.MineBankPlugin;
import com.minebank.minebankplugin.client.MineBankApiClient;
import com.minebank.minebankplugin.dto.MarketItem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

public class LojaCommand implements CommandExecutor {
    private final MineBankPlugin  plugin;
    private final MineBankApiClient apiClient;

    public LojaCommand(MineBankPlugin plugin, MineBankApiClient apiClient) {
        this.plugin = plugin;
        this.apiClient = apiClient;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage("Apenas jogadores podem usar esse comando!");
            return true;
        }

        apiClient.getMarket().thenAccept(responseJson -> {
            try {
                //converte objetos Java para JSON e vice-versa.
                Gson gson = new Gson();
                Type listType = new TypeToken<List<MarketItem>>(){}.getType();
                //Lista de itens
                List<MarketItem> marketItems = gson.fromJson(responseJson, listType);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    abrirMenuPaginado(player, marketItems, sender);
                });
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Erro ao buscar itens do Mercado");
                e.printStackTrace();
            }
        }).exceptionally(ex -> {
            player.sendMessage(ChatColor.RED + "Servidor está offline");
            return null;
        });
        return true;
    }

    private void abrirMenuPaginado(Player player, List<MarketItem> marketItems, CommandSender sender) {

        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.BLUE + "Mercado Central"))
                .rows(6)
                .pageSize(45)
                .create();

        for (MarketItem marketItem : marketItems) {
            //Tenta achar o item dentro do minecraft
            Material material = Material.getMaterial(marketItem.getminecraftMaterial());
            if (material == null || material.isAir() || !material.isItem()) {
                sender.sendMessage( ChatColor.RED + "Pulou item: " + marketItem.getminecraftMaterial());
                continue; // Pula blocos inválidos
            }

            String precoFormatado = String.format("%.2f", marketItem.getCurrentPrice());

            GuiItem guiItem = ItemBuilder.from(material)
                    .name(Component.text(ChatColor.GREEN + marketItem.getminecraftMaterial()))
                    .lore(
                        Component.text(ChatColor.GRAY + "Estoque disponível: §f" + marketItem.getStock()),
                        Component.text(""),
                        Component.text(ChatColor.WHITE + "Preço unitário: §e$" + precoFormatado),
                        Component.text(""),
                        Component.text(ChatColor.GREEN + "[Botão Esquerdo] para §aComprar §7(Em breve)"),
                        Component.text(ChatColor.GRAY + "[Botão Direito] §7para §cVender §7(Em breve)")
                    )
                    .asGuiItem(event -> {
                        event.setCancelled(true);
                    });
                gui.addItem(guiItem);
        }

        GuiItem btnAnterior = ItemBuilder.from(Material.ARROW).name(Component.text(ChatColor.YELLOW + "Página Anterior"))
                .asGuiItem(event -> {
                    event.setCancelled(true);
                    gui.previous();
                });

        GuiItem btnProximo = ItemBuilder.from(Material.ARROW).name(Component.text(ChatColor.YELLOW + "Próxima Página"))
                .asGuiItem(event -> {
                    event.setCancelled(true);
                    gui.next();
                });

        gui.setItem(6, 1,  btnAnterior);
        gui.setItem(6, 9,  btnProximo);

        gui.open(player);
    }
}
