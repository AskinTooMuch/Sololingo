package Bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Subject implements Serializable{
    private int id;
    private String uId;
    private String name;

    public Subject() {
    }

    public Subject(int id, String uId, String name) {
        this.id = id;
        this.uId = uId;
        this.name = name;
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
}
