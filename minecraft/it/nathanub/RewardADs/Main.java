package it.nathanub.RewardADs;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * Written with love by @Nathanub ;)
 */

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import it.nathanub.RewardADs.Logic.Announcer;
import it.nathanub.RewardADs.Logic.Events;
import it.nathanub.RewardADs.Logic.PApi;
import it.nathanub.RewardADs.Tools.Api;
import it.nathanub.RewardADs.Tools.InitializeInformations;
import it.nathanub.RewardADs.Tools.GetConfig;
import it.nathanub.RewardADs.Tools.Error;

public class Main extends JavaPlugin {
	private static Main instance;
	private static Api api = new Api();

	@Override
	public void onEnable() {
		instance = this;
		
		getLogger().info("Plugin RewardADs enabled!");
		getServer().getPluginManager().registerEvents(new Events(this), this);
		
		new InitializeInformations();
		new PApi().register();

		load();
	}
	
	public static void load() {
		new InitializeInformations();
		new PApi().register();
		cancelAllTasks();
		
		api.getServer().thenAccept(object -> {
			String nameServer = (String) object.get("name_server");
			String codeServer = (String) object.get("code_server");
			
			if(nameServer != null) {
				if(new GetConfig().isAnnouncerEnabled()) new Announcer().runTaskTimerAsynchronously(Main.getInstance(), 10, 20 * new GetConfig().getAnnouncesDelay());
				
				try {
		            InetAddress localHost = InetAddress.getLocalHost();
		            String ipAddress = localHost.getHostAddress();
		            
		            api.collectIp(codeServer, ipAddress).thenAccept(reply -> {})
						.exceptionally(e -> {
				            e.printStackTrace();
				            return null;
				        });
		        } catch (UnknownHostException e) {
		            Bukkit.getLogger().severe("Failed to get IP address: " + e.getMessage());
		        }
				
				api.setEarnPerAD();
			} else {
				new Error("configurations.yml", "not configurated code");
			}
		}).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
	}
	
	public static Main getInstance() {
        return instance;
    }
	
	public static void cancelAllTasks() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        for(BukkitTask task : scheduler.getPendingTasks()) {
            task.cancel();
        }
    }
}