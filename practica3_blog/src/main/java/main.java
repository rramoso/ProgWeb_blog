/**
 * Created by ricardoramos on 5/28/16.
 */

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
import spark.Session;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;


public class main {

    public static void main(String[] arg) throws Exception{

        Class.forName("org.h2.Driver");
        Connection connection =  DriverManager.getConnection("jdbc:h2:~/test","sa","");
        Statement statement = connection.createStatement();
        staticFiles.location("/");
        Configuration configuration=new Configuration();

        Services services = new Services();
        configuration.setClassForTemplateLoading(main.class, "/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setAdmin(true);
        admin.setName("Fulano");
        admin.setPassword("admin01");

        admin.createUser(statement);

        get("/",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            ArrayList<Articulos> articles = new ArrayList<>();
            Session s = request.session(true);

            String query = "SELECT * FROM Articulo";

            ResultSet articleQuery = statement.executeQuery(query);
            while (articleQuery.next()) {

                Articulos art = new Articulos();
                Usuario user = services.getUsuario(articleQuery.getString("autor"),connection.createStatement());
                int articleId = articleQuery.getInt("id");

                art.setId(articleId);
                art.setTitulo(articleQuery.getString("titulo"));
                art.setAutor(user);
                art.setContenido(articleQuery.getString("cuerpo"));
                art.setDate(articleQuery.getString("fecha"));
                articles.add(art);
            }
            if(s.attribute("username") == null){

                attributes.put("username","null");
            }
            else {

                attributes.put("username",s.attribute("username"));
            }
            attributes.put("articles",articles);

            return new ModelAndView(attributes, "index.html");
        }, freeMarkerEngine);

        post("/loging",(request, response) -> {

            String username = request.queryParams("username");
            String password = request.queryParams("password");

            Usuario user = services.getUsuario(username,statement);
            System.out.print("USUARIO DESDE LOGINING "+ user.getName());
            System.out.print(password + " "+ user.getPassword());
            int pass = password.compareTo(user.getPassword());
            if (user.getUsername() != null && password.equals(user.getPassword())){
                Session session = request.session(true);
                session.attribute("username",username);

                System.out.print("aqui");

                response.redirect("/");
                return "";
            }
            response.redirect("/login");
            return "";
        });

        get("/login",(request, response) -> {

            return new ModelAndView(null, "signup.html");
        }, freeMarkerEngine);

        get("/signup",(request, response) -> {

            return new ModelAndView(null, "signup.html");
        }, freeMarkerEngine);

        get("/logout",(request, response) -> {
            Session session = request.session(true);
            session.invalidate();

            response.redirect("/");
            return "";
        });

        post("/registeting",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            System.out.print("aqui");
            String username = request.queryParams("username_r");
            String password = request.queryParams("password_r");
            String name = request.queryParams("name");

            Usuario user = services.getUsuario(username,statement);

            System.out.println(user);
            if (user.getUsername() == null){
                user.setUsername(username);
                user.setPassword(password);
                user.setName(name);
                user.createUser(connection.createStatement());

                Session session = request.session(true);
                session.attribute("username",username);
                response.redirect("/");
                return "";
            }
            Session session = request.session(true);
            session.attribute("username",username);

            response.redirect("/signup");
            return "";
        });

        before("/eliminar/:article",(request, response) -> {

            Session session = request.session(true);
            String username = session.attribute("username");

            if(username == null){
                response.redirect("/login");
            }

            Usuario usuario= services.getUsuario(username,connection.createStatement());

            Articulos article = services.getArticle(Integer.parseInt(request.params("article")),statement);

            if(usuario.getUsername() != article.getAutor().getUsername() && !usuario.isAdmin())
            {
                halt(401, "No estas autorizado para eliminar este articulo.");
            }
        });

        get("/eliminar/:article",(request, response) -> {
            int id = Integer.parseInt(request.params("article"));

            Articulos article = services.getArticle(id,statement);

            article.deleteArticle(statement);

            response.redirect("/");
            return "";
        });

        get("/articulo/:articulo",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            int id = Integer.parseInt(request.params("articulo"));
            Articulos article = services.getArticle(id,statement);
            if(article.getId() == 0){
                halt(404,"Oops, te jodite xD!");

            }

            ArrayList<Tag> tags = services.getArticleTags(id,statement,connection.createStatement());
            ArrayList<Comment> comments = services.getArticleComments(id,statement,connection.createStatement());

            String author = article.getAutor().getName();
            if (author==null){
                author = "no name";
            }

            attributes.put("articleTags",tags);
            attributes.put("articleComments",comments);
            attributes.put("article",article);

            attributes.put("author",author);
            return new ModelAndView(attributes, "whole_article.html");
        }, freeMarkerEngine);

        before("/editar/:articulo",(request, response) -> {
            Session s = request.session(true);
            String  user = s.attribute("username");

            if (user == null){
                response.redirect("/login");

            }

            Usuario usuario= services.getUsuario(user,connection.createStatement());
            Articulos article = services.getArticle(Integer.parseInt(request.params("articulo")),statement);

            if(usuario.getUsername() != article.getAutor().getUsername() && !usuario.isAdmin())
            {
                halt(401, usuario.getUsername()+", no estas autorizado para editar este archivo");
            }

        });

        get("/editar/:articulo",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            int id = Integer.parseInt(request.params("articulo"));
            Articulos article = services.getArticle(id,statement);
            attributes.put("edit",1);
            attributes.put("article",article);
            String tags = "";
            ArrayList<Tag> tagsa = services.getArticleTags(id,connection.createStatement(),connection.createStatement());
            ;
            for (Tag t: tagsa){
                tags += t.getTag();
                tags +=",";
            }
            attributes.put("tags",tags);
            return new ModelAndView(attributes, "entry.html");
        }, freeMarkerEngine);

        before("/crear/articulo",(request, response) -> {
            String user = request.session(true).attribute("username");
            if (user != null){
                return ;
            }
            response.redirect("/login");
        });

        get("/crear/articulo",(request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("edit",0);
            return new ModelAndView(attributes, "entry.html");
        }, freeMarkerEngine);

        post("/editando/:articulo",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            Articulos article = new Articulos();
            Usuario user = services.getUsuario(request.session(true).attribute("username"),connection.createStatement());
            int id = Integer.parseInt(request.params("articulo"));
            String[] tags = request.queryParams("tags").split(",");

            ArrayList<Tag> articleTags = new ArrayList<Tag>();
            article.setId(id);
            article.setTitulo(request.queryParams("title"));
            article.setContenido(request.queryParams("content"));
            article.setAutor(user);
            for (String s:tags) {
                articleTags.add(services.createTag(s,statement));
            }
            article.setTags(articleTags);
            article.editArticle(connection.createStatement());
            response.redirect(String.format("/articulo/%s",id));
            return "";
        });


        post("/crearArticulo/",(request, response) -> {


            Map<String, Object> attributes = new HashMap<>();
            Articulos article = new Articulos();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Usuario user = services.getUsuario(request.session(true).attribute("username"),connection.createStatement());
            String[] tags = request.queryParams("tags").split(",");
            ArrayList<Tag> articleTags = new ArrayList<Tag>();
            article.setTitulo(request.queryParams("title"));
            article.setContenido(request.queryParams("content"));
            article.setAutor(user);
            article.setDate(sdf.format(cal.getTime()));
            for (String s:tags) {
                articleTags.add(services.createTag(s,statement));
            }
            article.setTags(articleTags);
            article.createArticle(statement);

            response.redirect("/");
            return "";

        });
        before("/agregarComentario/:articulo",(request, response) -> {
            String user = request.session(true).attribute("username");
            if (user != null){
                return;
            }
            response.redirect("/login");
        });

        post("/agregarComentario/:articulo",(request, response) -> {
            Session session = request.session(true);

            String id = request.params("articulo");
            String comment = request.queryParams("comment");
            String user = session.attribute("username");
            String query = String.format("INSERT INTO COMENTARIO(comentario,autor,articulo) VALUES('%s','%s',%s)",comment,user,id);

            statement.execute(query);


            response.redirect(String.format("/articulo/%s",id));
            return "";
        });

        post("/articulo/:articulo",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            String usuario = request.session(true).attribute("username").toString();

            int id = Integer.parseInt(request.params("articulo"));
            String comment = request.params("comment");

            response.redirect("/articulo/%s",id);
            return "";
        });
    }
}
