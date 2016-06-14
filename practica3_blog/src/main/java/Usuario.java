/**
 * Created by ricardoramos on 6/1/16.
 */
import javax.persistence.*;
import java.sql.SQLException;
import java.sql.Statement;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @Column(name = "USERNAME", length = 50)
    private String username;

    @Column(name = "NOMBRE", length = 100)
    private String name;

    @Column(name = "PASSWORD", length = 20)
    private String password;

    @Column(name = "ADMIN")
    private boolean admin;

    @Column(name = "AUTOR")
    private boolean author;

    public Usuario(){}



    public Usuario(String username, String name, String password, boolean admin, boolean author){

        this.setUsername(username);
        this.setName(name);
        this.setPassword(password);
        this.setAdmin(admin);
        this.setAuthor(author);
    }

    public Usuario(String username, String name, String pass) {
        this.setUsername(username);
        this.setPassword(pass);
        this.setName(name);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAuthor() {
        return author;
    }

    public void setAuthor(boolean author) {
        this.author = author;
    }


}

