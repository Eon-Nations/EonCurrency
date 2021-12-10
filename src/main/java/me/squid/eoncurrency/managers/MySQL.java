package me.squid.eoncurrency.managers;

import me.squid.eoncurrency.Eoncurrency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    Eoncurrency plugin;
    String host, database, user, password;
    Connection connection;

    public MySQL(Eoncurrency plugin) {
        this.plugin = plugin;
        host = plugin.getConfig().getString("host");
        database = plugin.getConfig().getString("database");
        user = plugin.getConfig().getString("username");
        password = plugin.getConfig().getString("password");
    }

    public boolean isConnected() {
        return (connection != null);
    }

    public void connectToDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?useSSL=false", user, password);
        if (isConnected()) plugin.getLogger().info("Database has succesfully connected");
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
