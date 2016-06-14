
import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.ArrayList;

/**
 * Created by ricardoramos on 6/1/16.
 */
@Entity
@Table(name = "ARTICULO")
public class Articulos {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @ManyToOne
    private Usuario autor;

    @Column(name = "TITULO", length = 50)
    private String titulo;

    @Column(name = "CUERPO", length = 80000, unique = true)
    private String contenido;

    @Column(name = "FECHA")
    private Date date;

    @Column(name = "LIKES")
    private int likes;

    @Column(name = "DISLIKES")
    private int dislikes;

    @ManyToMany(fetch = FetchType.EAGER)
    @Column(name = "ETIQUETAS")
    private Set<Tag> tags;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    @Column(name = "COMENTARIOS")
    private Set<Comment> comments;

    public Articulos(Integer id, String title, String body, Usuario author, Set<Comment> Comments, Set<Tag> Tags){

        this.setId(id);
        this.setTitulo(title);
        this.setContenido(body);
        this.setAutor(author);

        this.setComments(Comments);
        this.setTags(Tags);

        this.setLikes(0);
        this.setDislikes(0);

        java.util.Date utilDate = new java.util.Date();
        java.sql.Date date = new java.sql.Date(utilDate.getTime());

        this.setDate(date);

    }
    public Articulos(){

    }

    public String previewText(){
        if(this.contenido.length() < 70){
            return this.getContenido();
        }
        return this.getContenido().substring(0,69);
    }

    public Articulos(String title, String body, Usuario author) {
        this.setTitulo(title);
        this.setContenido(body);
        this.setAutor(author);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        this.setDate(date);
    }


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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }


    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}
