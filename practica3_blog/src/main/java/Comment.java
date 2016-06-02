/**
** Created by ricardoramos on 6/1/16.
**/
public class Comment {

    private int id;
    private String comment;
    private String author;
    private Articulos article;


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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Articulos getArticle() {
        return article;
    }

    public void setArticle(Articulos article) {
        this.article = article;
    }
}
