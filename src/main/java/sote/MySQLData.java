package sote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import cn.nukkit.Player;
import sote.ban.Ban;
import sote.home.Home;
import sote.party.Party;

public class MySQLData{

    public static String url = "jdbc:mysql://localhost/soteserver";
    String user = "nightmare3832";
    String password = "0unSDzyeqdkUKbNF";

    public static final String CLAN_DATA = "clan";
    public static final String HOME_DATA = "home";
    public static final String BAN_DATA = "bandata";
    public static final String REALNAME_DATA = "realname";

    public MySQLData(){
       try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
        } catch (SQLException e) {
            System.out.println("DBに接続できませんでした。\n"+e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public static void syncAllData(){
        syncBanData();
        syncHomeData();
    }

    public static long getBanId(){
        long l = System.currentTimeMillis();
        return l;
    }

    public static void addBan(LinkedTreeMap<String, String> map){
        String sql = "INSERT INTO `Ban`("
                   + "`name`, "
                   + "`id`,"
                   + "`ip`,"
                   + "`time`,"
                   + "`reason`"
                   + ") VALUES ("
                   + "'"+map.get("name")+"',"
                   + "'"+map.get("id")+"',"
                   + "'"+map.get("ip")+"',"
                   + "'"+map.get("time")+"',"
                   + "'"+map.get("reason")+"');";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(sql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void removeBan(String name){
        String sql = "DELETE FROM `Ban` WHERE `Ban`.`name` = '"+name+"'";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(sql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static Boolean isBanned(String name){
        String s = "SELECT COUNT(*) FROM `Ban` WHERE `Ban`.`name` = '"+name+"'";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            int r = result.getInt("COUNT(*)");
            if(r >= 1) return true;
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void syncBanData(){
        String s = "SELECT * FROM `Ban`";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (PreparedStatement statement = con.prepareStatement(s)){
                    ResultSet result = statement.executeQuery();
                    while(result.next()){
                        Ban.setBanData(result);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
             }
        }).start();
    }

    public static ResultSet getBanData(String name){
        String s = "SELECT * FROM `Ban` WHERE `Ban`.`name` = '"+name+"'";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addHome(HashMap<String, String> map){
        String sql = "INSERT INTO `Home`("
                   + "`owner`, "
                   + "`number`,"
                   + "`color`,"
                   + "`id`"
                   + ") VALUES ("
                   + "'"+map.get("owner")+"',"
                   + ""+map.get("number")+","
                   + ""+map.get("color")+","
                   + "'"+map.get("id")+"');";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(sql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void removeHome(String name){
        String sql = "DELETE FROM `Home` WHERE `Home`.`owner` = '"+name+"'";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(sql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void syncHomeData(){
        String s = "SELECT * FROM `Home`";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (PreparedStatement statement = con.prepareStatement(s)){
                    ResultSet result = statement.executeQuery();
                    while(result.next()){
                        Home.setHomeData(result);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
             }
        }).start();
    }

    public static Boolean isCreatedHomeId(String id){
        String s = "SELECT COUNT(*) FROM `Home` WHERE `Home`.`id` = '"+id+"'";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            int r = result.getInt("COUNT(*)");
            if(r >= 1) return true;
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean isCreatedHomeNumber(int id){
        String s = "SELECT COUNT(*) FROM `Home` WHERE `Home`.`number` = "+id+"";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            int r = result.getInt("COUNT(*)");
            if(r >= 1) return true;
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet getHomeData(int id){
        String s = "SELECT * FROM `Home` WHERE `Home`.`id` = '"+id+"'";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getNextHomeId(){
        int number = 0;
        for(int i = 100;i <= Integer.MAX_VALUE;i++){
            if(!isCreatedHomeNumber(i)){
                number = i;
                break;
            }
        }
        if(number == 0) return 0;
        return number;
    }

    public static void sendTemporaryDataForGame(Player player, String GameName, int GameId){
        String sql = "INSERT INTO `TemporaryData`("
                   + "`id`,"
                   + "`GameName`,"
                   + "`GameID`,"
                   + "`PartyData,`"
                   + "`HomeID`"
                   + ") VALUES ("
                   + "'"+MySQL.getGeneralField(player, "id")+"',"
                   + "'"+GameName+"',"
                   + "'"+GameId+"',"
                   + "'"+new GsonBuilder().setPrettyPrinting().create().toJson(Party.partyData.get(player.getName().toLowerCase()))+"',"
                   + "'"+""+"');";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(sql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void close(){
        /*try {
            con.close();
        } catch (SQLException e) {
            System.out.println("MySQLのクローズに失敗しました。");
        }*/
    }

    public static Connection con = null;
}