package it.nathanub.rewardADsGui;

import it.nathanub.rewardads.Logic.Buy.Buy;
import it.nathanub.rewardads.Logic.Buy.OnBuy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class Events implements Listener {
    private final Buy buy = new Buy();
    private final Plugin RewardADs;
    private final Plugin plugin;

    public Events(Plugin plugin, Plugin RewardADs) {
        this.RewardADs = RewardADs;
        this.plugin = plugin;
    }

    @EventHandler
    public void onBuy(OnBuy event) {
        Player player = event.getPlayer();
        String nameReward = event.getNameReward();
        String idReward = event.getIdReward();
        String costReward = event.getCostReward();

        String commandReward = plugin.getConfig().getString("rewards." + idReward + ".command");

        if(commandReward != null) {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), commandReward
                    .replace("%player%", player.getName())
                    .replace("%name%", nameReward)
                    .replace("%cost%", costReward)
                    .replace("%id%", idReward));
        } else {
            player.sendMessage("No action found for: " + idReward);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Rewards")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if(clickedItem != null && clickedItem.hasItemMeta()) {
                ItemMeta meta = clickedItem.getItemMeta();
                assert meta != null;
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                NamespacedKey nameKey = new NamespacedKey(plugin, "name");
                NamespacedKey costKey = new NamespacedKey(plugin, "cost");
                NamespacedKey idKey = new NamespacedKey(plugin, "id");

                String name = dataContainer.get(nameKey, PersistentDataType.STRING);
                String cost = dataContainer.get(costKey, PersistentDataType.STRING);
                String id = dataContainer.get(idKey, PersistentDataType.STRING);

                buy.handle(RewardADs, player.getName(), id, name, cost);
            }
        }
    }
}
