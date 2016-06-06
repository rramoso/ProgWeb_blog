
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardoramos on 6/1/16.
 */
public class Services {


    public ArrayList<Articulos> getAllArticles(Statement statement) throws SQLException, ClassNotFoundException {
        ArrayList<Articulos> articles = new ArrayList<>();
        ResultSet articleQuery;
        Class.forName("org.h2.Driver");
        Connection connection =  DriverManager.getConnection("jdbc:h2:~/test","sa","");


        String query = "SELECT * FROM Articulo ORDER BY fecha DESC";

            articleQuery = statement.executeQuery(query);
            while (articleQuery.next()) {
                System.out.println("como asi");
                Articulos art = new Articulos();
                Usuario user = getUsuario(articleQuery.getString("autor"),connection.createStatement());
                int articleId = articleQuery.getInt("id");

                art.setId(articleId);
                art.setTitulo(articleQuery.getString("titulo"));
                art.setAutor(user);
                art.setContenido(articleQuery.getString("cuerpo"));
                art.setDate(articleQuery.getString("fecha"));
                articles.add(art);
            }

        return articles;
    }
    public Articulos getArticle(int id,Statement statement) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection connection =  DriverManager.getConnection("jdbc:h2:~/test","sa","");

        Articulos article = new Articulos();
        try {
            ResultSet articlesQuery = statement.executeQuery(String.format("SELECT * FROM Articulo WHERE ID = %s",id));

            while(articlesQuery.next()){
                Usuario user = getUsuario(articlesQuery.getString("autor"),connection.createStatement());

                article.setId(articlesQuery.getInt("id"));
                article.setTitulo(articlesQuery.getString("titulo"));
                article.setContenido(articlesQuery.getString("cuerpo"));
                article.setAutor(user);
                article.setDate(articlesQuery.getString("fecha"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    public Services() {
    }

    public ArrayList<Tag> getArticleTags(int articleId, Statement statement, Statement statement2){
        ArrayList<Tag> tags = new ArrayList<>();
        String query = String.format("SELECT * FROM ARTICULO_ETIQUETA WHERE articulo = %s",articleId);
        ResultSet tagsQuery;

        try {
            tagsQuery = statement.executeQuery(query);
            while(tagsQuery.next()){
                ResultSet tag = statement2.executeQuery(String.format("SELECT * FROM ETIQUETA WHERE id = %s",tagsQuery.getString("Etiqueta")));
                ArrayList<Integer> ids = new ArrayList<>();
                while(tag.next()){

                    Tag commentTag = new Tag();
                    commentTag.setId(tag.getInt("id"));

                    commentTag.setTag(tag.getString("etiqueta"));

                    if (!ids.contains(commentTag.getId())){
                        ids.add(tag.getInt("id"));
                        tags.add(commentTag);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tags;

    }

    public ArrayList<Comment> getArticleComments(int articleId,Statement statement,Statement statement2) throws ClassNotFoundException {

        ArrayList<Comment> comments = new ArrayList<>();
        String query = String.format("SELECT * FROM COMENTARIO WHERE articulo = %s",articleId);

        try {
            ResultSet commentsQuery = statement.executeQuery(query);

            while(commentsQuery.next()){
                Comment cmt = new Comment();
                cmt.setId(commentsQuery.getInt("id"));
                cmt.setArticle(getArticle(articleId,statement2));
                cmt.setAuthor(commentsQuery.getString("autor"));
                cmt.setComment(commentsQuery.getString("comentario"));

                comments.add(cmt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    public void createComment(String user, String comment,String id,Statement statement){

        System.out.print("tamos aqui");
        String query = String.format("INSERT INTO COMENTARIO(comentario,autor,articulo) VALUES('%s','%s',%s)",comment,user,id);
        try {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Tag createTag(String s, Statement statement){

        Tag tag = new Tag();

        try {
            ResultSet tagQuery = statement.executeQuery(String.format("SELECT * FROM ETIQUETA WHERE etiqueta='%s'",s));
            if(!tagQuery.next()){
                String query = String.format("INSERT INTO ETIQUETA(etiqueta) VALUES('%s')",s);
                statement.execute(query);
            }
            ResultSet rs = statement.executeQuery(String.format("SELECT id FROM etiqueta WHERE etiqueta = '%s'",s));
            while(rs.next()){
                tag.setId(rs.getInt("id"));
            }

            tag.setTag(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.print("Tag id: ");
        System.out.println(tag.getId());
        return tag;
    }

    public Usuario getUsuario(String username, Statement statement){
        Usuario user = new Usuario();

        try {
            ResultSet users = statement.executeQuery(String.format("SELECT * FROM USUARIO WHERE USERNAME = '%s'",username));

            while(users.next()){

                user.setUsername(users.getString("username"));
                user.setName(users.getString("nombre"));
                user.setPassword(users.getString("password"));
                user.setAdmin(users.getBoolean("administrador"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
