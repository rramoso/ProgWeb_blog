import javax.xml.soap.Text;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by ricardoramos on 6/1/16.
 */
public class Articulos {

    private int id;
    private Usuario autor;
    private String titulo;
    private String contenido;
    private String date;
    private ArrayList<Tag> tags;
    private ArrayList<Comment> comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }



    public void createArticle(Statement statement){

        Usuario username = this.getAutor();
        String query = String.format("INSERT INTO ARTICULO(titulo,cuerpo,autor,fecha) VALUES('%s','%s','%s','%s')",this.getTitulo(),this.getContenido(),username.getUsername(),this.getDate());
        try {
            statement.execute(query);

            ResultSet rs = statement.executeQuery(String.format("SELECT ID FROM ARTICULO WHERE TITULO='%s'",this.getTitulo()));
            while(rs.next()){
                this.setId(rs.getInt("id"));
            }

            for (Tag tag: this.getTags()) {
                String queryTag = String.format("INSERT INTO ARTICULO_ETIQUETA(articulo,etiqueta) VALUES(%s,%s)",this.getId(),tag.getId());
                statement.execute(queryTag);
            }
            if(this.getComments() != null){
                for (Comment comment:this.getComments()) {
                    String queryComment = String.format("INSERT INTO COMENTARIO(autor,articulo,comment) VALUES(%s,%s,%s)",this.getAutor().getUsername(),this.getId(),comment.getId());
                    statement.execute(queryComment);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void editArticle(Statement statement){
        Usuario username = this.getAutor();
        String query = String.format("UPDATE ARTICULO SET titulo ='%s' ,cuerpo= '%s' WHERE id= %s",this.getTitulo(),this.getContenido(),this.getId());

        try {
            statement.execute(query);

            for (Tag tag: this.getTags()) {
                String queryTag = String.format("INSERT INTO ARTICULO_ETIQUETA(articulo,etiqueta) VALUES(%s,%s)",this.getId(),tag.getId());
                statement.execute(queryTag);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteArticle(Statement statement){

        try {
            statement.execute(String.format("DELETE FROM COMENTARIO WHERE articulo = %s",this.getId()));
            statement.execute(String.format("DELETE FROM ARTICULO_ETIQUETA WHERE ARTICULO = %s",this.getId()));
            statement.execute(String.format("DELETE FROM ARTICULO WHERE ID = %s",this.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String previewText(){
        if(this.contenido.length() < 70){
            return this.getContenido();
        }
        return this.getContenido().substring(0,69);
    }

}
