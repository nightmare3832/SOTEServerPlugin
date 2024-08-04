package sote;

public class PlayerData{

    public String name;
    public int ID;

    public PlayerData(int id, String name){
        this.ID = id;
        this.name = name;
    }

    public int getID(){
        return this.ID;
    }

    public String getName(){
        return this.name;
    }

}
