/**
 * Created by ricardoramos on 6/1/16.
 */
import java.sql.SQLException;
import java.sql.Statement;

public class Usuario {

    private String username;
    private String name;
    private String password;
    private boolean admin;
    private boolean author;

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

    public void createUser(Statement statement){
        try {
            statement.execute(String.format("INSERT INTO USUARIO(username,nombre,password,administrador) VALUES('%s','%s','%s',%s)",this.getUsername(),this.getName(),this.getPassword(),this.isAdmin()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

