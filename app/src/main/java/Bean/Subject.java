package Bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Subject implements Serializable{
    private int id;
    private String uId;
    private String name;
    private int curProgress;

    public Subject() {
    }

    public Subject(int id, String uId, String name, int curProgress) {
        this.id = id;
        this.uId = uId;
        this.name = name;
        this.curProgress = curProgress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurProgress() {
        return curProgress;
    }

    public void setCurProgress(int curProgress) {
        this.curProgress = curProgress;
    }
}
