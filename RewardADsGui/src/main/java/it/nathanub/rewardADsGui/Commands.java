package it.nathanub.rewardADsGui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.nathanub.rewardads.Logic.Buy.Buy;
import it.nathanub.rewardads.Logic.Rewards.Rewards;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {
    private final Plugin RewardADs;
    private final Rewards rewards;
    private final Plugin plugin;

    public Commands(Plugin RewardADs, Plugin plugin) {
        this.RewardADs = RewardADs;
        rewards = new Rewards(RewardADs);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String s, @NonNull String[] args) {
        if(command.getName().equalsIgnoreCase("buy")) {
            if(args.length == 0) {
                if(sender instanceof Player player) {
                    player.openInventory(rewardGui(rewards.getList()));

                    return true;
                }
            }
        } else if(command.getName().equalsIgnoreCase("rewardadsgui")) {
            if(args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if(sender instanceof Player player) {
                    plugin.reloadConfig();
                    player.sendMessage("§bRewardADsGui§a reloaded successfully!");
                } else {
                    plugin.reloadConfig();
                    sender.sendMessage("§bRewardADsGui§a reloaded successfully!");
                }
            } else {
                if(sender instanceof Player player) {
                    player.sendMessage("This is a premade interface for §6RewardADs");

                    return true;
                } else {
                    sender.sendMessage("This is a premade interface for §6RewardADs");
                }
            }
        }

        return false;
    }

    public Inventory rewardGui(JsonArray jsonArray) {
        try {
            Inventory gui = Bukkit.createInventory(null, 54, "Rewards");

            for(int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                String name = jsonObject.get("name").getAsString();
                String cost = jsonObject.get("cost").getAsString();
                String id = jsonObject.get("id").getAsString();

                ItemStack item = new ItemStack(Material.CHEST);
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName("§e" + name);

                List<String> lore = new ArrayList<>();
                lore.add("§fCost: §6" + cost);
                meta.setLore(lore);

                NamespacedKey idKey = new NamespacedKey(plugin, "id");
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                dataContainer.set(idKey, PersistentDataType.STRING, id);

                NamespacedKey nameKey = new NamespacedKey(plugin, "name");
                NamespacedKey costKey = new NamespacedKey(plugin, "cost");

                dataContainer.set(nameKey, PersistentDataType.STRING, name);
                dataContainer.set(costKey, PersistentDataType.STRING, cost);

                item.setItemMeta(meta);
                gui.setItem(i, item);
            }

            return gui;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}