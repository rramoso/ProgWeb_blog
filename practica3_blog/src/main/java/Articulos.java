import javax.xml.soap.Text;
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

    public void createArticle(Usuario blogger, String title, String content){


    }
    public void editArticle(Usuario blogger, String title, String content){

    }

    public void deleteArticle(Usuario blogger){

    }

    public void addComment(Comment comment){

    }

    public String previewText(){

        return this.contenido.substring(0,69);
    }

}
