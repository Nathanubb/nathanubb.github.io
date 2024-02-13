package it.nathanub.RewardADs.Tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class DB {
	private static DatabaseManager databaseManager;
	
	public DB() {
		String url = "jdbc:mysql://185.229.236.154:3306/rewardads?useSSL=true";
        String username = "root";
        String password = "password";

        databaseManager = new DatabaseManager(url, username, password);
	}
	
	public void setCoins(Player player, int coins) {
		Map<String, Object> response = queryDB("UPDATE coins SET coins_player = " + coins + " WHERE code_server = '" + new GetConfig().getCode() + "' AND name_player = '" + player.getName() + "';");
	    Connection connection = (Connection) response.get("connection");
	    Statement statement = (Statement) response.get("statement");
	    
	    try {
			statement.close();
		    connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getCoins(Player player) {
		Map<String, Object> response = queryDB("SELECT * FROM coins WHERE code_server = '" + new GetConfig().getCode() + "' AND name_player = '" + player.getName() + "';");
		Connection connection = (Connection) response.get("connection");
	    Statement statement = (Statement) response.get("statement");
	    ResultSet resultSet = (ResultSet) response.get("resultSet");
		
		try {
			while(resultSet.next()) {
				return resultSet.getInt("coins_player");
			}
			
			resultSet.close();
			statement.close();
		    connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void setEarnPerAD() {
		Map<String, Object> response = queryDB("UPDATE servers SET earnPerAD_server = " + new GetConfig().getEarnPerAD() + " WHERE code_server = '" + new GetConfig().getCode() + "';");
	    Connection connection = (Connection) response.get("connection");
	    Statement statement = (Statement) response.get("statement");
	    
	    try {
			statement.close();
		    connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getServerName() {
		Map<String, Object> response = queryDB("SELECT * FROM servers WHERE code_server = '" + new GetConfig().getCode() + "';");
	    Connection connection = (Connection) response.get("connection");
	    Statement statement = (Statement) response.get("statement");
	    ResultSet resultSet = (ResultSet) response.get("resultSet");
	    String nameServer = null;
	    
	    try {
	    	while(resultSet.next()) {
				nameServer = resultSet.getString("name_server");
			}
	    	
			resultSet.close();
			statement.close();
		    connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    if(nameServer == null) new Error("configuration.yml", "code is not valid!");
	    
	    return nameServer;
	}
	
	private Map<String, Object> queryDB(String query) {
        Map<String, Object> result = new HashMap<>();
        Connection connection = null;
        Statement selectStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = databaseManager.getConnection();
            selectStatement = connection.createStatement();
            
            if(selectStatement.execute(query)) {
                resultSet = selectStatement.getResultSet();
            } else {
                selectStatement.getUpdateCount();
            }
            
            result.put("connection", connection);
            result.put("statement", selectStatement);
            result.put("resultSet", resultSet);
            
        } catch (SQLException e) {
            e.printStackTrace();
            new Error("Internal Error", "failed to connect to our database!");
        }
        
        return result;
    }
}
