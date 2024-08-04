package sote.serverlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import cn.nukkit.Server;

public class ServerListAPI {

    public static String token = "Q3gvR1pxaFhlbWxJWFN0Y1ZsVmlJaW5HSk5zSXFFOWFhcU00SUdoVFFmY3FXU1A5VTNvYnZ3dG9xYjJtSGhSV3BUYlBpR2s1WTFXbTFmN0ljRXdVTTZsRS9RSVpla0tNNTNUU2RqM05FTEV0aXBNUmloY0JCZkhVdGc5bWprUmhaOFNSb2NqR1BkOG5wVmV4ajhTVEE2VStmOW5VcHdwRllDQStEcVB3U1JpTVJIZ3JGT2YvZEU4bUJyZkxqS0FhVS9rNXZGSUVLSlJGNTNDRms4b2tvUG1MS2x6MmxjbkI3bzMyelY0azFIVTZkWVNwZ3MzOC81K000THRFSmRxVnNtVmloUk4zWVRHbGU4QXJPeUtSbW9xQ2ltbnJkdVFqWWJLTzNENnRremY3bHZpTkFldHRWTjhESjQrak92TXhTdlYxeVRKQiswVEduZUlsY0NIUC96dmtHL0JsZmh6YnNjNTAvMHo3NUgvQXAvRmR5UFBCMEtiTndkVlNITFA0V2xFNUFxRUIyRXN1K1lMNEt3UnpvM2hkalR2ZW43N1pWQTRhejR4MFlZMXJvejlCclo4M2lIbW94eXpQeXBIeFpkbkZBaWpkOWlacytpZ3FDN3F6UGsxOHJuUHlzOFUxeGtQTGlKTU5PeGM9";

    public static String accessToken = null;

    public static void login(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("max", String.valueOf(Server.getInstance().getMaxPlayers()));
        map.put("now", String.valueOf(Server.getInstance().getOnlinePlayers().size()));
        map.put("type", "start");
        map.put("server_token", token);
        sendData("update", castQuery(map));

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("state", "1");
        map2.put("access_token", accessToken);
        sendData("push", castQuery(map2));
    }

    public static void logout(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", "stop");
        map.put("access_token", accessToken);
        sendData("update", castQuery(map));
    }

    public static void updatePlayers(String type){
        Map<String, String> map = new HashMap<String, String>();
        map.put("max", String.valueOf(Server.getInstance().getMaxPlayers()));
        map.put("now", String.valueOf(Server.getInstance().getOnlinePlayers().size()));
        map.put("type", type);
        map.put("access_token", accessToken);
        sendData("update", castQuery(map));
    }

    public static void updateTime(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("max", String.valueOf(Server.getInstance().getMaxPlayers()));
        map.put("type", "time");
        map.put("access_token", accessToken);
        sendData("update", castQuery(map));
    }

    public static StringJoiner castQuery(Map<String, String> map){
        StringJoiner query = new StringJoiner("&");
        for (Map.Entry<String, String> parameter : map.entrySet()) {
            query.add(parameter.getKey() + "=" + parameter.getValue());
        }
        return query;
    }

    public static void sendData(String endpoint, StringJoiner data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = "http://api.pmmp.jp.net/"+endpoint;
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("Plugin", "hXBsxY_P7_".toCharArray());
                    }
                });
                try {
                    URL url = new URL(urlString);
                    URLConnection uc = url.openConnection();
                    uc.setDoOutput(true);//POST可能にする
                    OutputStream os = uc.getOutputStream();
                    PrintStream ps = new PrintStream(os);
                    ps.print(data);//データをPOSTする
                    System.setProperty("sun.net.client.defaultReadTimeout", "3000");
                    ps.close();
                    InputStream is = uc.getInputStream();//POSTした結果を取得
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String s;
                    while ((s = reader.readLine()) != null) {
                        String[] fruit = s.split("'", 0);
                        accessToken = fruit[1];
                    }
                    reader.close();
                } catch (MalformedURLException e) {
                    System.err.println("Invalid URL format: " + urlString);
                    System.exit(-1);
                } catch (IOException e) {
                    System.err.println("Can't connect to " + urlString);
                    System.exit(-1);
                }
            }
        }).start();
    }

}
