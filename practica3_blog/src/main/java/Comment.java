/**
** Created by ricardoramos on 6/1/16.
**/

import javax.persistence.*;
@Entity
@Table(name = "COMENTARIO")
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Column(name = "COMMENT", length = 500)
    private String comment;

    @ManyToOne
    private Usuario author;

    @ManyToOne
    private Articulos article;

    public Comment(){

    }
    public Comment(Integer id){
        this.setId(id);
    }

    public Comment(Integer id, Articulos article){
        this.setId(id);
        this.setArticle(article);
    }

    public Comment(String comment, Usuario author, Articulos article){

        this.setComment(comment);
        this.setAuthor(author);
        this.setArticle(article);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String commment) {
        this.comment = commment;
    }

    public Usuario getAuthor() {
        return author;
    }

    public void setAuthor(Usuario author) {
        this.author = author;
    }

    public Articulos getArticle() {
        return article;
    }

    public void setArticle(Articulos article) {
        this.article = article;
    }
}
