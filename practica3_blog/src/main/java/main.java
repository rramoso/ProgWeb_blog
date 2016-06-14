/**
 * Created by ricardoramos on 5/28/16.
 */

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static spark.Spark.*;
import spark.Session;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;


public class main {

    public static void main(String[] arg) throws Exception{

        staticFiles.location("/");
        Configuration configuration=new Configuration();

        configuration.setClassForTemplateLoading(main.class, "/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        Usuario admin = new Usuario("admin","Fulano","admin01",true,true);

        if(UserServices.getInstance().findAll().size() == 0){

            UserServices.getInstance().crear(admin);
        }

        before("/",(request, response) -> {

            response.redirect("/page/1");
        });

        get("/page/:page",(request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            int page;
            if(request.params("page").equals("")){
                page = 1;
            }
            else{
                page = Integer.parseInt(request.params("page"));
            }

            List<Articulos> articles = ArticulosServices.getInstancia().findAll();
            List<Articulos> result = new ArrayList<Articulos>();
            int count = 0;

            for(Articulos art : articles){
                count++;
                System.out.println("\n\nCount: "+ count);
                System.out.println(page*5-5);
                System.out.println(page*5);

                if(count > page*5-5 && count <= page*5){
                    result.add(art);
                    System.out.println("\tSODA STEREO\n\n\n");
                }
            }
            Session s = request.session(true);
            String username = s.attribute("username");
            if(username == null){
                username = "null";
            }

            attributes.put("prev_page",page-1);
            attributes.put("page",page);
            attributes.put("next_page",page+1);
            attributes.put("username",username);
            attributes.put("articles",result);

            return new ModelAndView(attributes, "index.html");
        }, freeMarkerEngine);





        post("/loging",(request, response) -> {

            String username = request.queryParams("username");
            String password = request.queryParams("password");

            Usuario user = UserServices.getInstance().find(username);

            if (user.getUsername() != null && password.equals(user.getPassword())){
                Session session = request.session(true);
                session.attribute("username",username);

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

            String username = request.queryParams("username_r");
            String name = request.queryParams("name");
            String pass = request.queryParams("password_r");

            Usuario user = new Usuario(username,name,pass);
            UserServices.getInstance().crear(user);

            if (user.getUsername() == null){

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
            String id = request.params("article");
            if(username == null){
                response.redirect("/login");
            }

            Usuario usuario= UserServices.getInstance().find(username);

            Articulos article = ArticulosServices.getInstancia().find(id);

            if(usuario.getUsername() != article.getAutor().getUsername() && !usuario.isAdmin())
            {
                halt(401, "No estas autorizado para eliminar este articulo.");
            }
        });

        get("/eliminar/:article",(request, response) -> {
            int id = Integer.parseInt(request.params("article"));

            Articulos article = ArticulosServices.getInstancia().find(id);

            ArticulosServices.getInstancia().eliminar(article);

            response.redirect("/");
            return "";
        });

        get("/articulo/:articulo",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            int id = Integer.parseInt(request.params("articulo"));
            Articulos article = ArticulosServices.getInstancia().find(id);

            if(article.getId() == 0){
                halt(404,"Oops, te jodite xD!");

            }

            Set<Tag> tags = article.getTags();
            Set<Comment> comments = article.getComments();

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
            int id = Integer.parseInt(request.params("articulo"));
            if (user == null){
                response.redirect("/login");

            }


            Usuario usuario= UserServices.getInstance().find(user);

            System.out.println("Tamo aqui");
            Articulos article = ArticulosServices.getInstancia().find(id);
            System.out.println("\n\n"+usuario.getUsername()+"\t"+article.getAutor().getUsername()+"\n\n");
            System.out.println("\n\n"+!usuario.getUsername().equals(article.getAutor().getUsername())+"\n\n");

            if(!usuario.getUsername().equals(article.getAutor().getUsername()) && !usuario.isAdmin())
            {
                halt(401, usuario.getUsername()+", no estas autorizado para editar este archivo");
            }
        });

        get("/editar/:articulo",(request, response) -> {


            Map<String, Object> attributes = new HashMap<>();

            int id = Integer.parseInt(request.params("articulo"));
            Articulos article = ArticulosServices.getInstancia().find(id);
            String tags = "";
            Set<Comment> comments = article.getComments();

            String author = article.getAutor().getName();
            if (author == null){
                author = "no name";
            }
            for(Tag tag : article.getTags()){
                tags +=tag.getTag()+",";
            }
            attributes.put("tags",tags);
            attributes.put("articleComments",comments);
            attributes.put("article",article);

            attributes.put("author",author);
            attributes.put("edit",1);
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

            String usuario = request.session(true).attribute("username");
            Usuario user = UserServices.getInstance().find(usuario);

            int id = Integer.parseInt(request.params("articulo"));

            Articulos article = ArticulosServices.getInstancia().find(id);

            String title = request.queryParams("title");
            String content = request.queryParams("content");

            article.setTitulo(title);
            article.setContenido(content);

            String[] tags = request.queryParams("tags").split(",");

            ArrayList<Tag> articleTags = new ArrayList<Tag>();
            System.out.println("\n\nTAGS");
            for (String s:tags) {
                System.out.println("s");
                articleTags.add(new Tag(s));
            }


            TagServices.TagsArticle(articleTags,article);
            ArticulosServices.getInstancia().editar(article);

            response.redirect(String.format("/articulo/%s",id));
            return "";
        });

        post("/crearArticulo/",(request, response) -> {


            Map<String, Object> attributes = new HashMap<>();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String username = request.session(true).attribute("username");
            String title = request.queryParams("title");
            String content = request.queryParams("content");
            Usuario user = UserServices.getInstance().find(username);
            String[] tags = request.queryParams("tags").split(",");
            System.out.println(tags);
            ArrayList<Tag> articleTags = new ArrayList<Tag>();

            System.out.println("\n\nTAGS");
            for (String s:tags) {
                if (!s.equals("")){
                    articleTags.add(new Tag(s));
                }
            }


            int id = ArticulosServices.CreateArticle(title,content,user);
            System.out.print("\n\n\tSIZE: "+articleTags.size()+"\n\n");
            if(articleTags.size() > 0) {
                TagServices.TagsArticle(articleTags, ArticulosServices.getInstancia().find(id));
            }
            System.out.print("\n\n\tWHAT2\n\n");

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
        before("/like/:articulo",(request, response) -> {
            String user = request.session(true).attribute("username");
            if (user != null){
                return;
            }
            response.redirect("/login");
        });before("/dislike/:articulo",(request, response) -> {
            String user = request.session(true).attribute("username");
            if (user != null){
                return;
            }
            response.redirect("/login");
        });

        post("/agregarComentario/:articulo",(request, response) -> {
            System.out.print("\n\nDESDE EL COMENTARIO:\n\n");
            Session session = request.session(true);

            int id = Integer.parseInt(request.params("articulo"));
            String comment = request.queryParams("comment");
            String user = session.attribute("username");
            Usuario author = UserServices.getInstance().find(user);

            Articulos article = ArticulosServices.getInstancia().find(id);
            System.out.println("\n\tAfter article\n\n");

            System.out.println("\n\nCOMENTARIO\n\n"+comment+" "+author.getUsername()+" "+id+" \n\n");
            Comment com = new Comment(comment, author, article);

            CommentServices.getInstance().crear(com);

            response.redirect(String.format("/articulo/%s",id));
            return "";
        });

        get("/like/:articulo",(request, response) -> {
            Session session = request.session(true);

            int id = Integer.parseInt(request.params("articulo"));

            Articulos article = ArticulosServices.getInstancia().find(id);

            int likes = article.getLikes();
            article.setLikes(likes+1);
            ArticulosServices.getInstancia().editar(article);

            response.redirect(String.format("/articulo/%s",id));
            return "";
        });

        get("/articulos/etiqueta/:etiqueta",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();

            List<Articulos> articles = ArticulosServices.getInstancia().findAll();
            ArrayList<Articulos> articlesToShow =  new ArrayList<Articulos>();

            int id = Integer.parseInt(request.params("etiqueta"));

            System.out.println("\n\ntodo bien\n\n");

            for (Articulos art:articles) {
                for (Tag t:art.getTags()) {
                    if (t.getId() == id) {
                        articlesToShow.add(art);
                    }
                }
            }

            attributes.put("articles",articlesToShow);

            return new ModelAndView(attributes, "tag_articles.html");
        }, freeMarkerEngine);

        get("/dislike/:articulo",(request, response) -> {
            Session session = request.session(true);

            int id = Integer.parseInt(request.params("articulo"));

            Articulos article = ArticulosServices.getInstancia().find(id);
            System.out.println("Hope it works");
            int likes = article.getDislikes();
            article.setDislikes(likes+1);
            ArticulosServices.getInstancia().editar(article);

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
