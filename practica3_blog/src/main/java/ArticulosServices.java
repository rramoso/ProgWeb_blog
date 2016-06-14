import java.util.List;

/**
 * Created by ricardoramos on 6/9/16.
 */
public class ArticulosServices extends GestionDB<Articulos> {

    private static ArticulosServices instancia;

    private ArticulosServices() {
        super(Articulos.class);
    }

    public static ArticulosServices getInstancia(){
        if(instancia==null){
            instancia = new ArticulosServices();
        }
        return instancia;
    }
    public static Integer CreateArticle(String title, String body, Usuario author){

        Articulos article = new Articulos(title, body, author);

        ArticulosServices.getInstancia().crear(article);

        List<Articulos> articles = ArticulosServices.getInstancia().findAll();

        for (Articulos art:
                articles) {
            if(art.getContenido().equals(body))
                article = art;
        }

        return article.getId();
    }

}
