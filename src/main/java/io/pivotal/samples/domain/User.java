package io.pivotal.samples.domain;

/**
 * Created by pivotal on 07/01/2016.
 */
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value="users")
public class User {

    @PrimaryKey(value="username")
    @Column("username") private String username;
    @Column("fname") private String fname;
    @Column("lname") private String lname;


    public String getUsername() {
        return username;
    }

    public void setUsername(String uname) {
        this.username = uname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}