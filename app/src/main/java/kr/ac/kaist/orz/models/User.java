package kr.ac.kaist.orz.models;

public class User {
    private int id;
    private String username;
    private String email;
    private int type;
    private int schoolID;
    private String schoolName;
    private String name;

    public User(int id, int type, int schoolID) {
        this.id = id;
        this.type = type;
        this.schoolID = schoolID;
    }

    public int getID() { return id; }
    public int getSchoolID() { return schoolID; }
    public String getUserName() { return username; }
    public String getName() { return name; }
    public void setName(String newName) { name = newName; }
    public String getUserEmail() { return email; }
    public void setUserEmail(String newEmail) { email = newEmail; }
    public int getUserType() { return type; }
}
