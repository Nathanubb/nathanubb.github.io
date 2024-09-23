# Contacts
* [Website](https://rewardads.it)
* [Discord](https://discord.gg/wFxxxWuA5k)
* [Github](https://github.com/Nathanubb/rewardads)
* [Email](mailto:youremail@example.com)


# Library Minecraft

## Api
* Server={getName(), isValid()}
* Rewards={getList()}
* Buy={handle(RewardADsPlugin, playerName, idReward, nameReward, costRewards)}
* OnBuy={
          @EventHandler
          public void onBuy(OnBuy event) {}
  }
  
  event has {getPlugin(), getPlayer(), getNameReward(), getIdReward(), getCostReward(), getCode()}

  to parse RewardADs plugin, you can use:
  PluginManager pluginManager = Bukkit.getPluginManager();
  Plugin RewardADs = pluginManager.getPlugin("RewardADs");

## Dependencies
* PlaceholderAPI

## Placeholders APIs:
### Top Coins:
* rewardads_coinstop_1
* rewardads_coinstop_2
* rewardads_coinstop_3
* rewardads_coinstop_4
* rewardads_coinstop_5
* rewardads_coinstop_6
* rewardads_coinstop_7
* rewardads_coinstop_8
* rewardads_coinstop_9
* rewardads_coinstop_10

## Permissions:
* rewardads.admin
