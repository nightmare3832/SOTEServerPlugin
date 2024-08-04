package sote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import cn.nukkit.Player;
import sote.achievements.Achievements;
import sote.clan.Clan;
import sote.home.Home;
import sote.lang.Language;
import sote.lobbyitem.LobbyItems;
import sote.login.LoginSystem;
import sote.murder.achievements.MurderAchievements;
import sote.murder.armor.Armors;
import sote.murder.hat.MurderHats;
import sote.murder.upgrade.MurderUpgrades;
import sote.murder.weapon.Weapons;
import sote.setting.Setting;
import sote.skywarssolo.kit.SkywarsSoloKits;
import sote.stat.Stat;

public class MySQL{

    public static String url = "jdbc:mysql://localhost/Account_v1";
    String user = "nightmare3832";
    String password = "0unSDzyeqdkUKbNF";

    public MySQL(){
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

    public static void createGeneralDB(Player player){
        int id = getNextId();
        String GeneralValue = ""+id+","
                + "'"+player.getName()+"',"
                + "'"+player.getName().toLowerCase()+"',"
                + "'"+LoginSystem.getLoginPassword(player)+"',"
                + "'"+LoginSystem.getLoginAddress(player)+"',"
                + ""+Stat.getExp(player)+","
                + ""+Stat.getLevel(player)+","
                + ""+Stat.getCoin(player)+","
                + ""+Stat.getMedal(player)+","
                + ""+Stat.getVip(player)+","
                + ""+Stat.getYoutube(player)+","
                + ""+Stat.getWarn(player)+","
                + "'"+Language.getLang(player)+"',"
                + "'"+Setting.getSettings(player)+"',"
                + "'"+LobbyItems.getLobbyItems(player)+"',"
                + "'"+LobbyItems.getSellectLobbyItem(player)+"',"
                + "'"+Clan.getClanDataString(player)+"',"
                + "'"+Home.getPlayerHomeData(player)+"'";
        String GeneralSql = "INSERT INTO `General`("+GeneralKey+") VALUES ("+GeneralValue+");";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(GeneralSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void createMurderDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String MurderValue = ""+id+","
                + ""+Stat.getMurderPlays(player)+","
                + ""+Stat.getMurderKarma(player)+","
                + ""+Stat.getMurderMisskills(player)+","
                + ""+Stat.getMurderInnocentsShot(player)+","
                + ""+Stat.getMurderEmeraldCollected(player)+","
                + ""+Stat.getMurderWeaponTraded(player)+","
                + "'"+Stat.getMurderHat(player)+"',"
                + "'"+MurderHats.getHats(player)+"',"
                + "'"+MurderAchievements.getAchievements(player)+"',"
                + "'"+MurderUpgrades.getUpgrades(player)+"',"
                + "'"+Armors.getArmors(player)+"',"
                + "'"+Armors.getSellectHelmetString(player)+"',"
                + "'"+Armors.getSellectLeggingsString(player)+"',"
                + "'"+Armors.getSellectBootsString(player)+"',"
                + "'"+Weapons.getWeapons(player)+"',"
                + "'"+Weapons.getSelectWeaponString(player)+"'";
        String MurderSql = "INSERT INTO `Game.Murder`("+MurderKey+") VALUES ("+MurderValue+");";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(MurderSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void createMurderMurderDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String MurderMurderValue = ""+id+","
                + ""+Stat.getMurderMurderWin(player)+","
                + ""+Stat.getMurderMurderLose(player)+","
                + "0,"
                + ""+Stat.getMurderMurderKills(player)+","
                + ""+Stat.getMurderMurderDeaths(player)+"";
        String MurderMurderSql = "INSERT INTO `Game.Murder_Murder`("+MurderMurderKey+") VALUES ("+MurderMurderValue+");";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(MurderMurderSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void createMurderBystanderDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String MurderBystanderValue = ""+id+","
                + ""+Stat.getMurderBystanderWin(player)+","
                + ""+Stat.getMurderBystanderLose(player)+","
                + "0,"
                + ""+Stat.getMurderBystanderKills(player)+","
                + ""+Stat.getMurderBystanderDeaths(player)+"";
        String MurderBystanderSql = "INSERT INTO `Game.Murder_Bystander`("+MurderBystanderKey+") VALUES ("+MurderBystanderValue+");";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(MurderBystanderSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void createSkywarsDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String SkywarsValue = "'"+id+"',"
                + ""+Stat.getSkywarsWin(player)+","
                + ""+Stat.getSkywarsLose(player)+","
                + ""+Stat.getSkywarsKills(player)+","
                + ""+Stat.getSkywarsDeaths(player)+","
                + ""+Stat.getSkywarsPlays(player)+","
                + "'"+SkywarsSoloKits.getKits(player)+"',"
                + "'"+SkywarsSoloKits.getSellectKitString(player)+"'";
        String SkywarsSql = "INSERT INTO `Game.SkyWars`("+SkywarsKey+") VALUES ("+SkywarsValue+");";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(SkywarsSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void createBuildbattleDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String BuildbattleValue = "'"+id+"',"
                + ""+Stat.getBuildbattleWin(player)+","
                + ""+Stat.getBuildbattleLose(player)+","
                + ""+Stat.getBuildbattlePlays(player)+"";
        String BuildbattleSql = "INSERT INTO `Game.BuildBattle`("+BuildbattleKey+") VALUES ("+BuildbattleValue+");";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(BuildbattleSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void createMiniwallsDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String MiniwallsValue = "'"+id+"',"
                + ""+Stat.getMiniwallsKills(player)+","
                + ""+Stat.getMiniwallsDeaths(player)+","
                + ""+Stat.getMiniwallsWin(player)+","
                + ""+Stat.getMiniwallsLose(player)+","
                + ""+Stat.getMiniwallsSoldierLevel(player)+","
                + ""+Stat.getMiniwallsArcherLevel(player)+","
                + ""+Stat.getMiniwallsBuilderLevel(player)+","
                + ""+Stat.getMiniwallsPlays(player)+"";
        String MiniwallsSql = "INSERT INTO `Game.MiniWalls`("+MiniwallsKey+") VALUES ("+MiniwallsValue+");";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(MiniwallsSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static int getNextId(){
        String s = "SELECT COUNT(*) FROM `General`";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString("COUNT(*)");
            if(r instanceof String)
            return Integer.parseInt(r) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 9999999;
    }

    public static String getMurderField(Player player, String d){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String s = "SELECT * FROM `Game.Murder` WHERE `Game.Murder`.`id` = "+id+"";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString(d);
            if(r instanceof String)
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMurderMurderField(Player player, String d){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String s = "SELECT * FROM `Game.Murder_Murder` WHERE `Game.Murder_Murder`.`id` = "+id+"";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString(d);
            if(r instanceof String)
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMurderBystanderField(Player player, String d){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String s = "SELECT * FROM `Game.Murder_Bystander` WHERE `Game.Murder_Bystander`.`id` = "+id+"";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString(d);
            if(r instanceof String)
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSkywarsField(Player player, String d){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String s = "SELECT * FROM `Game.SkyWars` WHERE `Game.SkyWars`.`id` = "+id+"";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString(d);
            if(r instanceof String)
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMiniwallsField(Player player, String d){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String s = "SELECT * FROM `Game.MiniWalls` WHERE `Game.MiniWalls`.`id` = "+id+"";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString(d);
            if(r instanceof String)
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getBuildbattleField(Player player, String d){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String s = "SELECT * FROM `Game.BuildBattle` WHERE `Game.BuildBattle`.`id` = "+id+"";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString(d);
            if(r instanceof String)
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getGeneralField(Player player, String d){
        String s = "SELECT * FROM `General` WHERE `General`.`lowname` = '"+player.getName().toLowerCase()+"'";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString(d);
            if(r instanceof String)
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getGeneralField(String player, String d){
        String s = "SELECT * FROM `General` WHERE `General`.`lowname` = '"+player.toLowerCase()+"'";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString(d);
            if(r instanceof String)
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getGeneralField(int id, String d){
        String s = "SELECT * FROM `General` WHERE `General`.`id` = "+id+"";
        try (PreparedStatement statement = con.prepareStatement(s)){
            ResultSet result = statement.executeQuery();
            result.next();
            String r = result.getString(d);
            if(r instanceof String)
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Boolean isGeneralCreated(Player player){
        String s = "SELECT COUNT(*) FROM `General` WHERE `General`.`lowname` = '"+player.getName().toLowerCase()+"'";
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

    public static Boolean isMurderCreated(Player player){
        String s = "SELECT COUNT(*) FROM `Game.Murder` WHERE `Game.Murder`.`id` = '"+getGeneralField(player, "id")+"'";
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

    public static Boolean isMurderMurderCreated(Player player){
        String s = "SELECT COUNT(*) FROM `Game.Murder_Murder` WHERE `Game.Murder_Murder`.`id` = '"+getGeneralField(player, "id")+"'";
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

    public static Boolean isMurderBystanderCreated(Player player){
        String s = "SELECT COUNT(*) FROM `Game.Murder_Bystander` WHERE `Game.Murder_Bystander`.`id` = '"+getGeneralField(player, "id")+"'";
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

    public static Boolean isSkywarsCreated(Player player){
        String s = "SELECT COUNT(*) FROM `Game.SkyWars` WHERE `Game.SkyWars`.`id` = '"+getGeneralField(player, "id")+"'";
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

    public static Boolean isBuildbattleCreated(Player player){
        String s = "SELECT COUNT(*) FROM `Game.BuildBattle` WHERE `Game.BuildBattle`.`id` = '"+getGeneralField(player, "id")+"'";
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

    public static Boolean isMiniwallsCreated(Player player){
        String s = "SELECT COUNT(*) FROM `Game.MiniWalls` WHERE `Game.MiniWalls`.`id` = '"+getGeneralField(player, "id")+"'";
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

    public static void sendGeneralDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String GeneralValue = ""+id+",,"
                + "'"+player.getName()+"',,"
                + "'"+player.getName().toLowerCase()+"',,"
                + "'"+LoginSystem.getLoginPassword(player)+"',,"
                + "'"+LoginSystem.getLoginAddress(player)+"',,"
                + ""+Stat.getExp(player)+",,"
                + ""+Stat.getLevel(player)+",,"
                + ""+Stat.getCoin(player)+",,"
                + ""+Stat.getMedal(player)+",,"
                + ""+Stat.getVip(player)+",,"
                + ""+Stat.getYoutube(player)+",,"
                + ""+Stat.getWarn(player)+",,"
                + "'"+Language.getLang(player)+"',,"
                + "'"+Setting.getSettings(player)+"',,"
                + "'"+LobbyItems.getLobbyItems(player)+"',,"
                + "'"+LobbyItems.getSellectLobbyItem(player)+"',,"
                + "'"+Clan.getClanDataString(player)+"',,"
                + "'"+Home.getPlayerHomeData(player)+"'";
        String[] GeneralKeys = GeneralKey.split(",");
        String[] GeneralValues = GeneralValue.split(",,");
        String GeneralBase = "";
        int count = 0;
        boolean isFirst = true;
        for(String k : GeneralKeys){
            if(!isFirst){
                GeneralBase += ",";
            }else{
                isFirst = false;
            }
            GeneralBase += k+" = "+GeneralValues[count];
            count++;
        }
        String GeneralSql = "UPDATE `General` SET "+GeneralBase+" WHERE `General`.`id` = "+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(GeneralSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void sendMurderDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String MurderValue = ""+id+",,"
                + ""+Stat.getMurderPlays(player)+",,"
                + ""+Stat.getMurderKarma(player)+",,"
                + ""+Stat.getMurderMisskills(player)+",,"
                + ""+Stat.getMurderInnocentsShot(player)+",,"
                + ""+Stat.getMurderEmeraldCollected(player)+",,"
                + ""+Stat.getMurderWeaponTraded(player)+",,"
                + "'"+Stat.getMurderHat(player)+"',,"
                + "'"+MurderHats.getHats(player)+"',,"
                + "'"+MurderAchievements.getAchievements(player)+"',,"
                + "'"+MurderUpgrades.getUpgrades(player)+"',,"
                + "'"+Armors.getArmors(player)+"',,"
                + "'"+Armors.getSellectHelmetString(player)+"',,"
                + "'"+Armors.getSellectLeggingsString(player)+"',,"
                + "'"+Armors.getSellectBootsString(player)+"',,"
                + "'"+Weapons.getWeapons(player)+"',,"
                + "'"+Weapons.getSelectWeaponString(player)+"'";
        String[] MurderKeys = MurderKey.split(",");
        String[] MurderValues = MurderValue.split(",,");
        String MurderBase = "";
        int count = 0;
        boolean isFirst = true;
        for(String k : MurderKeys){
            if(!isFirst){
                MurderBase += ",";
            }else{
                isFirst = false;
            }
            MurderBase += k+" = "+MurderValues[count];
            count++;
        }
        String MurderSql = "UPDATE `Game.Murder` SET "+MurderBase+" WHERE `Game.Murder`.`id` = "+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(MurderSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void sendMurderMurderDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String MurderMurderValue = ""+id+",,"
                + ""+Stat.getMurderMurderWin(player)+",,"
                + ""+Stat.getMurderMurderLose(player)+",,"
                + "0,,"
                + ""+Stat.getMurderMurderKills(player)+",,"
                + ""+Stat.getMurderMurderDeaths(player)+"";
        String[] MurderMurderKeys = MurderMurderKey.split(",");
        String[] MurderMurderValues = MurderMurderValue.split(",,");
        String MurderMurderBase = "";
        int count = 0;
        boolean isFirst = true;
        for(String k : MurderMurderKeys){
            if(!isFirst){
                MurderMurderBase += ",";
            }else{
                isFirst = false;
            }
            MurderMurderBase += k+" = "+MurderMurderValues[count];
            count++;
        }
        String MurderMurderSql = "UPDATE `Game.Murder_Murder` SET "+MurderMurderBase+" WHERE `Game.Murder_Murder`.`id` = "+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(MurderMurderSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void sendMurderBystanderDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String MurderBystanderValue = ""+id+",,"
                + ""+Stat.getMurderBystanderWin(player)+",,"
                + ""+Stat.getMurderBystanderLose(player)+",,"
                + "0,,"
                + ""+Stat.getMurderBystanderKills(player)+",,"
                + ""+Stat.getMurderBystanderDeaths(player)+"";
        String[] MurderBystanderKeys = MurderBystanderKey.split(",");
        String[] MurderBystanderValues = MurderBystanderValue.split(",,");
        String MurderBystanderBase = "";
        int count = 0;
        boolean isFirst = true;
        for(String k : MurderBystanderKeys){
            if(!isFirst){
                MurderBystanderBase += ",";
            }else{
                isFirst = false;
            }
            MurderBystanderBase += k+" = "+MurderBystanderValues[count];
            count++;
        }
        String MurderBystanderSql = "UPDATE `Game.Murder_Bystander` SET "+MurderBystanderBase+" WHERE `Game.Murder_Bystander`.`id` = "+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(MurderBystanderSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void sendSkywarsDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String SkywarsValue = "'"+id+"',,"
                + ""+Stat.getSkywarsWin(player)+",,"
                + ""+Stat.getSkywarsLose(player)+",,"
                + ""+Stat.getSkywarsKills(player)+",,"
                + ""+Stat.getSkywarsDeaths(player)+",,"
                + ""+Stat.getSkywarsPlays(player)+",,"
                + "'"+SkywarsSoloKits.getKits(player)+"',,"
                + "'"+SkywarsSoloKits.getSellectKitString(player)+"'";
        String[] SkywarsKeys = SkywarsKey.split(",");
        String[] SkywarsValues = SkywarsValue.split(",,");
        String SkywarsBase = "";
        int count = 0;
        boolean isFirst = true;
        for(String k : SkywarsKeys){
            if(!isFirst){
                SkywarsBase += ",";
            }else{
                isFirst = false;
            }
            SkywarsBase += k+" = "+SkywarsValues[count];
            count++;
        }
        String SkywarsSql = "UPDATE `Game.SkyWars` SET "+SkywarsBase+" WHERE `Game.SkyWars`.`id` = "+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(SkywarsSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void sendBuildbattleDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String BuildbattleValue = "'"+id+"',,"
                + ""+Stat.getBuildbattleWin(player)+",,"
                + ""+Stat.getBuildbattleLose(player)+",,"
                + ""+Stat.getBuildbattlePlays(player)+"";
        String[] BuildbattleKeys = BuildbattleKey.split(",");
        String[] BuildbattleValues = BuildbattleValue.split(",,");
        String BuildbattleBase = "";
        int count = 0;
        boolean isFirst = true;
        for(String k : BuildbattleKeys){
            if(!isFirst){
                BuildbattleBase += ",";
            }else{
                isFirst = false;
            }
            BuildbattleBase += k+" = "+BuildbattleValues[count];
            count++;
        }
        String BuildbattleSql = "UPDATE `Game.BuildBattle` SET "+BuildbattleBase+" WHERE `Game.BuildBattle`.`id` = "+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(BuildbattleSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void sendMiniwallsDB(Player player){
        int id = Integer.parseInt(getGeneralField(player, "id"));
        String MiniwallsValue = "'"+id+"',,"
                + ""+Stat.getMiniwallsKills(player)+",,"
                + ""+Stat.getMiniwallsDeaths(player)+",,"
                + ""+Stat.getMiniwallsWin(player)+",,"
                + ""+Stat.getMiniwallsLose(player)+",,"
                + ""+Stat.getMiniwallsSoldierLevel(player)+",,"
                + ""+Stat.getMiniwallsArcherLevel(player)+",,"
                + ""+Stat.getMiniwallsBuilderLevel(player)+",,"
                + ""+Stat.getMiniwallsPlays(player)+"";
        String[] MiniwallsKeys = MiniwallsKey.split(",");
        String[] MiniwallsValues = MiniwallsValue.split(",,");
        String MiniwallsBase = "";
        int count = 0;
        boolean isFirst = true;
        for(String k : MiniwallsKeys){
            if(!isFirst){
                MiniwallsBase += ",";
            }else{
                isFirst = false;
            }
            MiniwallsBase += k+" = "+MiniwallsValues[count];
            count++;
        }
        String MiniwallsSql = "UPDATE `Game.MiniWalls` SET "+MiniwallsBase+" WHERE `Game.MiniWalls`.`id` = "+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stm = con.createStatement();
                    int result = stm.executeUpdate(MiniwallsSql);
                } catch (SQLException e) {
                    System.out.println(e);
                }
             }
        }).start();
    }

    public static void sendAllData(Player player){
        if(!isGeneralCreated(player)) createGeneralDB(player);
        else sendGeneralDB(player);
        if(!isMurderCreated(player)) createMurderDB(player);
        else sendMurderDB(player);
        if(!isMurderMurderCreated(player)) createMurderMurderDB(player);
        else sendMurderMurderDB(player);
        if(!isMurderBystanderCreated(player)) createMurderBystanderDB(player);
        else sendMurderBystanderDB(player);
        if(!isSkywarsCreated(player)) createSkywarsDB(player);
        else sendSkywarsDB(player);
        if(!isMiniwallsCreated(player)) createMiniwallsDB(player);
        else sendMiniwallsDB(player);
        if(!isBuildbattleCreated(player)) createBuildbattleDB(player);
        else sendBuildbattleDB(player);
    }

    public static void setGeneralPlayerStats(Player player){
        String data = "";
        data = getGeneralField(player, "name");
        if(data.equals("")) data = "";
        data = getGeneralField(player, "password");
        if(data.equals("")) data = "";
        LoginSystem.setLoginPassword(player, data);
        data = getGeneralField(player, "ip");
        if(data.equals("")) data = "";
        LoginSystem.setLoginAddress(player, data);
        data = getGeneralField(player, "warn");
        if(data.equals("")) data = "0";
        Stat.setWarn(player, Integer.parseInt(data));
        data = getGeneralField(player, "language");
        //if(data.equals("")){
            data = "en_US";
        //}
        Language.setLang(player, data);
        data = getGeneralField(player, "select_lobbyitem");
        if(data.equals("")) data = "unknown";
        LobbyItems.setSellectLobbyItem(player, data);
        data = getGeneralField(player, "lobbyitems");
        if(data.equals("")){
            LobbyItems.addPlayer(player);
            data = LobbyItems.getLobbyItems(player);
        }
        LobbyItems.setLobbyItems(player, data);
        data = getGeneralField(player, "settings");
        if(data.equals("")){
            Setting.addPlayer(player);
            data = Setting.getSettings(player);
        }
        Setting.setSettings(player, data);
        data = getGeneralField(player, "clandata");
        if(data.equals("")){
            Clan.addPlayer(player);
            data = Clan.getClanDataString(player);
        }
        Clan.setClanDataString(player, data);
        data = getGeneralField(player, "homedata");
        if(data.equals("")){
            Home.addPlayer(player);
            data = Home.getPlayerHomeData(player);
        }
        Home.setPlayerHomeData(player, data);
        data = getGeneralField(player, "vip");
        if(data.equals("")) data = "0";
        Stat.setVip(player, Integer.parseInt(data));
        data = getGeneralField(player, "youtube");
        if(data.equals("")) data = "0";
        Stat.setYoutube(player, Integer.parseInt(data));
        data = getGeneralField(player, "coin");
        if(data.equals("")) data = "0";
        Stat.setCoin(player, Integer.parseInt(data));
        data = getGeneralField(player, "medal");
        if(data.equals("")) data = "0";
        Stat.setMedal(player, Integer.parseInt(data));
        data = getGeneralField(player, "level");
        if(data.equals("")) data = "1";
        Stat.setLevel(player, Integer.parseInt(data));
        data = getGeneralField(player, "exp");
        if(data.equals("")) data = "0";
        Stat.setExp(player, Integer.parseInt(data));
    }

    public static void setMurderPlayerStats(Player player){
        String data = "";
        data = getMurderField(player, "plays");
        if(data.equals("")) data = "0";
        Stat.setMurderPlays(player, Integer.parseInt(data));
        data = getMurderField(player, "karma");
        if(data.equals("")) data = "100";
        Stat.setMurderKarma(player, Integer.parseInt(data));
        data = getMurderField(player, "miss_kills");
        if(data.equals("")) data = "0";
        Stat.setMurderMisskills(player, Integer.parseInt(data));
        data = getMurderField(player, "innocents_shot");
        if(data.equals("")) data = "0";
        Stat.setMurderInnocentsShot(player, Integer.parseInt(data));
        data = getMurderField(player, "emerald_collected");
        if(data.equals("")) data = "0";
        Stat.setMurderEmeraldCollected(player, Integer.parseInt(data));
        data = getMurderField(player, "weapon_traded");
        if(data.equals("")) data = "0";
        Stat.setMurderWeaponTraded(player, Integer.parseInt(data));
        data = getMurderField(player, "select_hat");
        if(data.equals("")) data = "unknown";
        Stat.setMurderHat(player, data);
        data = getMurderField(player, "hats");
        if(data.equals("")){
            MurderHats.addPlayer(player);
            data = MurderHats.getHats(player);
        }
        MurderHats.setHats(player, data);
        data = getMurderField(player, "achievements");
        if(data.equals("")){
            MurderAchievements.addPlayer(player);
            data = MurderAchievements.getAchievements(player);
        }
        MurderAchievements.setAchievements(player, data);
        data = getMurderField(player, "upgrades");
        if(data.equals("")){
            MurderUpgrades.addPlayer(player);
            data = MurderUpgrades.getUpgrades(player);
        }
        MurderUpgrades.setUpgrades(player, data);
        data = getMurderField(player, "armors");
        if(data.equals("")){
            Armors.addPlayer(player);
            data = Armors.getArmors(player);
        }
        Armors.setArmors(player, data);
        data = getMurderField(player, "select_helmet");
        if(data.equals("")) data = "0:0";
        Armors.setSellectHelmet(player, data);
        data = getMurderField(player, "select_leggings");
        if(data.equals("")) data = "0:0";
        Armors.setSellectLeggings(player, data);
        data = getMurderField(player, "select_boots");
        if(data.equals("")) data = "0:0";
        Armors.setSellectBoots(player, data);
        data = getMurderField(player, "weapons");
        if(data.equals("")){
            Weapons.addPlayer(player);
            data = Weapons.getWeapons(player);
        }
        Weapons.setWeapons(player, data);
        data = getMurderField(player, "select_weapon");
        if(data.equals("")) data = "Wooden";
        Weapons.setSelectWeapon(player, data);
    }

    public static void setMurderMurderPlayerStats(Player player){
        String data = "";
        data = getMurderMurderField(player, "win");
        if(data.equals("")) data = "0";
        Stat.setMurderMurderWin(player, Integer.parseInt(data));
        data = getMurderMurderField(player, "lose");
        if(data.equals("")) data = "0";
        Stat.setMurderMurderLose(player, Integer.parseInt(data));
        data = getMurderMurderField(player, "deaths");
        if(data.equals("")) data = "0";
        Stat.setMurderMurderDeaths(player, Integer.parseInt(data));
        data = getMurderMurderField(player, "kills");
        if(data.equals("")) data = "0";
        Stat.setMurderMurderKills(player, Integer.parseInt(data));
    }

    public static void setMurderBystanderPlayerStats(Player player){
        String data = "";
        data = getMurderBystanderField(player, "win");
        if(data.equals("")) data = "0";
        Stat.setMurderBystanderWin(player, Integer.parseInt(data));
        data = getMurderBystanderField(player, "lose");
        if(data.equals("")) data = "0";
        Stat.setMurderBystanderLose(player, Integer.parseInt(data));
        data = getMurderBystanderField(player, "deaths");
        if(data.equals("")) data = "0";
        Stat.setMurderBystanderDeaths(player, Integer.parseInt(data));
        data = getMurderBystanderField(player, "kills");
        if(data.equals("")) data = "0";
        Stat.setMurderBystanderKills(player, Integer.parseInt(data));
    }

    public static void setSkywarsPlayerStats(Player player){
        String data = "";
        data = getSkywarsField(player, "win");
        if(data.equals("")) data = "0";
        Stat.setSkywarsWin(player, Integer.parseInt(data));
        data = getSkywarsField(player, "lose");
        if(data.equals("")) data = "0";
        Stat.setSkywarsLose(player, Integer.parseInt(data));
        data = getSkywarsField(player, "plays");
        if(data.equals("")) data = "0";
        Stat.setSkywarsPlays(player, Integer.parseInt(data));
        data = getSkywarsField(player, "kills");
        if(data.equals("")) data = "0";
        Stat.setSkywarsKills(player, Integer.parseInt(data));
        data = getSkywarsField(player, "deaths");
        if(data.equals("")) data = "0";
        Stat.setSkywarsDeaths(player, Integer.parseInt(data));
        data = getSkywarsField(player, "kits");
        if(data.equals("")){
            SkywarsSoloKits.addPlayer(player);
            data = SkywarsSoloKits.getKits(player);
        }
        SkywarsSoloKits.setKits(player, data);
        data = getSkywarsField(player, "select_kit");
        if(data.equals("")) data = "unknown";
        SkywarsSoloKits.setSellectKit(player, data);
    }

    public static void setMiniwallsPlayerStats(Player player){
        String data = "";
        data = getMiniwallsField(player, "win");
        if(data.equals("")) data = "0";
        Stat.setMiniwallsWin(player, Integer.parseInt(data));
        data = getMiniwallsField(player, "lose");
        if(data.equals("")) data = "0";
        Stat.setMiniwallsLose(player, Integer.parseInt(data));
        data = getMiniwallsField(player, "plays");
        if(data.equals("")) data = "0";
        Stat.setMiniwallsPlays(player, Integer.parseInt(data));
        data = getMiniwallsField(player, "kills");
        if(data.equals("")) data = "0";
        Stat.setMiniwallsKills(player, Integer.parseInt(data));
        data = getMiniwallsField(player, "deaths");
        if(data.equals("")) data = "0";
        Stat.setMiniwallsDeaths(player, Integer.parseInt(data));
        data = getMiniwallsField(player, "soldier_level");
        if(data.equals("")) data = "0";
        Stat.setMiniwallsSoldierLevel(player, Integer.parseInt(data));
        data = getMiniwallsField(player, "archer_level");
        if(data.equals("")) data = "0";
        Stat.setMiniwallsArcherLevel(player, Integer.parseInt(data));
        data = getMiniwallsField(player, "builder_level");
        if(data.equals("")) data = "0";
        Stat.setMiniwallsBuilderLevel(player, Integer.parseInt(data));
    }

    public static void setBuildbattlePlayerStats(Player player){
        String data = "";
        data = getBuildbattleField(player, "win");
        if(data.equals("")) data = "0";
        Stat.setBuildbattleWin(player, Integer.parseInt(data));
        data = getBuildbattleField(player, "lose");
        if(data.equals("")) data = "0";
        Stat.setBuildbattleLose(player, Integer.parseInt(data));
        data = getBuildbattleField(player, "plays");
        if(data.equals("")) data = "0";
        Stat.setBuildbattlePlays(player, Integer.parseInt(data));
    }

    public static void setAllPlayerStats(Player player){
        if(!MySQL.isGeneralCreated(player)){
            Language.addPlayer(player);
            Stat.addGeneralPlayer(player);
            Setting.addPlayer(player);
            Clan.addPlayer(player);
            Achievements.addPlayer(player);
            Home.addPlayer(player);
            LobbyItems.addPlayer(player);
        }else{
            Stat.stat.put(player.getName().toLowerCase(), new HashMap<String, String>());
            LoginSystem.accountDB.put(player.getName().toLowerCase(), new HashMap<String, String>());
            Home.homeData.put(player.getName().toLowerCase(), new HashMap<String, String>());
            setGeneralPlayerStats(player);
            MurderAchievements.achievementsData.put(player.getName().toLowerCase(), new HashMap<String, Integer>());
            Achievements.achievementsData.put(player.getName().toLowerCase(), new HashMap<String, String>());
            MurderHats.hatData.put(player.getName().toLowerCase(), new HashMap<String, Boolean>());
        }
        if(!MySQL.isMurderCreated(player)){
            Stat.addMurderPlayer(player);
            Weapons.addPlayer(player);
            Armors.addPlayer(player);
            MurderAchievements.addPlayer(player);
            MurderUpgrades.addPlayer(player);
            MurderHats.addPlayer(player);
        }else{
            MurderAchievements.achievementsData.put(player.getName().toLowerCase(), new HashMap<String, Integer>());
            MurderUpgrades.upgradeData.put(player.getName().toLowerCase(), new HashMap<String, Integer>());
            Achievements.achievementsData.put(player.getName().toLowerCase(), new HashMap<String, String>());
            MurderHats.hatData.put(player.getName().toLowerCase(), new HashMap<String, Boolean>());
            setMurderPlayerStats(player);
        }
        if(!MySQL.isMurderMurderCreated(player)){
            Stat.addMurderMurderPlayer(player);
        }else{
            setMurderMurderPlayerStats(player);
        }
        if(!MySQL.isMurderBystanderCreated(player)){
            Stat.addMurderBystanderPlayer(player);
        }else{
            setMurderBystanderPlayerStats(player);
        }
        if(!MySQL.isSkywarsCreated(player)){
            Stat.addSkywarsPlayer(player);
            SkywarsSoloKits.addPlayer(player);
            SkywarsSoloKits.setSellectKit(player, "unknown");
        }else{
            setSkywarsPlayerStats(player);
        }
        if(!MySQL.isMiniwallsCreated(player)){
            Stat.addMiniwallsPlayer(player);
        }else{
            setMiniwallsPlayerStats(player);
        }
        if(!MySQL.isBuildbattleCreated(player)){
            Stat.addBuildbattlePlayer(player);
        }else{
            setBuildbattlePlayerStats(player);
        }
    }

    public static void close(){
        /*try {
            con.close();
        } catch (SQLException e) {
            System.out.println("MySQLのクローズに失敗しました。");
        }*/
    }

    public static Connection con = null;

    public static String GeneralKey = "`id`,"
                    + "`name`,"
                    + "`lowname`,"
                    + "`password`,"
                    + "`ip`,"
                    + "`exp`,"
                    + "`level`,"
                    + "`coin`,"
                    + "`medal`,"
                    + "`vip`,"
                    + "`youtube`,"
                    + "`warn`,"
                    + "`language`,"
                    + "`settings`,"
                    + "`lobbyitems`,"
                    + "`select_lobbyitem`,"
                    + "`clandata`,"
                    + "`homedata`";

    public static String MurderKey = "`id`,"
                    + "`plays`,"
                    + "`karma`,"
                    + "`miss_kills`,"
                    + "`innocents_shot`,"
                    + "`emerald_collected`,"
                    + "`weapon_traded`,"
                    + "`select_hat`,"
                    + "`hats`,"
                    + "`achievements`,"
                    + "`upgrades`,"
                    + "`armors`,"
                    + "`select_helmet`,"
                    + "`select_leggings`,"
                    + "`select_boots`,"
                    + "`weapons`,"
                    + "`select_weapon`";

    public static String MurderMurderKey = "`id`,"
                    + "`win`,"
                    + "`lose`,"
                    + "`plays`,"
                    + "`kills`,"
                    + "`deaths`";

    public static String MurderBystanderKey = "`id`,"
                    + "`win`,"
                    + "`lose`,"
                    + "`plays`,"
                    + "`kills`,"
                    + "`deaths`";

    public static String SkywarsKey = "`id`,"
                    + "`win`,"
                    + "`lose`,"
                    + "`plays`,"
                    + "`kills`,"
                    + "`deaths`,"
                    + "`kits`,"
                    + "`select_kit`";

    public static String BuildbattleKey = "`id`,"
                    + "`win`,"
                    + "`lose`,"
                    + "`plays`";

    public static String MiniwallsKey = "`id`,"
                    + "`win`,"
                    + "`lose`,"
                    + "`plays`,"
                    + "`kills`,"
                    + "`deaths`,"
                    + "`soldier_level`,"
                    + "`archer_level`,"
                    + "`builder_level`";
}