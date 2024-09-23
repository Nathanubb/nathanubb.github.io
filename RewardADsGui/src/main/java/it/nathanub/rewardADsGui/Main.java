package it.nathanub.rewardADsGui;

import it.nathanub.rewardads.Logic.Rewards.Rewards;
import it.nathanub.rewardads.Tools.Server.Server;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {
    PluginManager pluginManager = Bukkit.getPluginManager();
    Plugin RewardADs = pluginManager.getPlugin("RewardADs");

    private final Rewards rewards;
    private final Server server;

    {
        assert RewardADs != null;
        server = new Server(RewardADs);
        rewards = new Rewards(RewardADs);
    }

    public static void main(String[] args) {}

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(this, RewardADs), this);
        Objects.requireNonNull(this.getCommand("buy")).setExecutor(new Commands(RewardADs, this));
        saveDefaultConfig();

        System.out.println(server.isValid());
        System.out.println(rewards.getList());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
