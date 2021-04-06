package main.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_operator")
    private int isOperator;

    @Column(name = "is_administrator")
    private int isAdministrator;

    @Column(name = "reg_time")
    private Date regTime;

    private String name;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Request> requests;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsOperator() {
        return isOperator;
    }

    public void setIsOperator(int isOperator) {
        this.isOperator = isOperator;
    }

    public int getIsAdministrator() {
        return isAdministrator;
    }

    public void setIsAdministrator(int isAdministrator) {
        this.isAdministrator = isAdministrator;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Request> getPosts() {
        return requests;
    }

    public void setPosts(List<Request> requests) {
        this.requests = requests;
    }


}
