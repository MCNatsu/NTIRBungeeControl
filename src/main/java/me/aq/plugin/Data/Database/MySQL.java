package me.aq.plugin.Data.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private String host = "localhost";
    private String port = "3306";
    private String database = "ntirplayerdata";
    private String chatdatabase = "ntirchatdata";
    private String user = "NatsuServer";
    private String password = "TaiWanIsVeryGood";


    private Connection connection;
    private Connection chatConnection;

    public boolean isConnected(){

        return (connection == null ? false : true);

    }

    public boolean isChatConnected(){

        return (chatConnection == null ? false : true);

    }

    public  void connect() throws ClassNotFoundException, SQLException {
        if(!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":"+ port + "/"
                    + database + "?useSSL=false&autoReconnect=true&failOverReadOnly=false", user, password);
        }

        if(!isChatConnected()) {
            chatConnection = DriverManager.getConnection("jdbc:mysql://" + host + ":"+ port + "/"
                    + chatdatabase + "?useSSL=false&autoReconnect=true&failOverReadOnly=false", user, password);
        }
    }

    public  void disconnect(){
        if(isConnected())
            try {
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        if(isChatConnected())
            try {
                chatConnection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
    }
    public  Connection getConnection(){
        return connection;
    }
    public  Connection getChatConnection(){
        return chatConnection;
    }
}
