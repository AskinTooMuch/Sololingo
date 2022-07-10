package Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private String email;
    private String name;
    private String dob;

    public User() {
    }

    public User(String email, String name, String dob) {
        this.email = email;
        this.name = name;
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Date getStringDOB() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/yyyy");
        Date result = null;
        try {
            result = simpleDateFormat.parse(this.dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getUserPath() {
        return this.getEmail().replace('.', '_')
                .replace('#', '_')
                .replace('$', '_')
                .replace('[', '_')
                .replace(']', '_');
    }
}
