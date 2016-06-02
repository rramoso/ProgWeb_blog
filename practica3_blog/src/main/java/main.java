/**
 * Created by ricardoramos on 5/28/16.
 */

import java.sql.*;
import java.util.ArrayList;
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
        Configuration configuration=new Configuration();
        Services services = new Services();
        configuration.setClassForTemplateLoading(main.class, "/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        get("/",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            ArrayList<Articulos> articles = services.getAllArticles();
            attributes.put("articles",articles);

            return new ModelAndView(attributes, "home.html");
        }, freeMarkerEngine);

        get("/articulo/:articulo",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            Articulos article = services.getArticle(Integer.parseInt(request.params("articulo")));

            attributes.put("article",article);

            return new ModelAndView(attributes, "article.html");
        }, freeMarkerEngine);

        post("/agregarComentario/:articulo",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            Session session = request.session(true);
            String comment = request.queryParams("comment");
            Usuario usuario = services.getUsuario(session.attribute("username"));
            services.createComment(usuario, comment,Integer.parseInt(request.params("articulo")));

            Articulos article = services.getArticle(Integer.parseInt(request.params("articulo")));

            attributes.put("article",article);
            return new ModelAndView(attributes, "article.html");
        }, freeMarkerEngine);
    }
}
