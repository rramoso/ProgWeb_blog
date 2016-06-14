import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ricardoramos on 6/10/16.
 */
public class TagServices extends GestionDB<Tag>{

    private static TagServices instance;

    private TagServices(){
        super(Tag.class);
    }

    public static TagServices getInstance(){
        if(instance == null)
            instance = new TagServices();

        return instance;
    }


    public static Integer CreateTag(String tag){

        Tag tg = new Tag(tag);

        TagServices.getInstance().crear(tg);

        List<Tag> tags = TagServices.getInstance().findAll();

        for (Tag t:
                tags) {
            if(t.getTag().equals(tag))
                tg = t;
        }

        return tg.getId();
    }

    public static void TagsArticle(ArrayList<Tag> tags, Articulos article){

        List<Tag> tg = TagServices.getInstance().findAll();
        ArrayList<Tag> tg2 = new ArrayList<>();

        for (Tag tag:
                tags) {

            boolean newTag = true;

            for (Tag t:
                    tg) {
                if(tag.getTag().equals(t.getTag())) {
                    newTag = false;
                    tag.setId(t.getId());

                    break;
                }

            }

            if(newTag)
                tag.setId(CreateTag(tag.getTag()));

            tg2.add(tag);

        }
        for (Tag tag:
                tg2) {
            article.getTags().remove(tag);
        }

        for (Tag tag:
                tg2) {
            article.getTags().add(tag);
        }

        for(Tag t: article.getTags()){
            System.out.println("\n\n"+t.getId()+"  "+t.getTag()+"\n\n");
        }
        ArticulosServices.getInstancia().editar(article);

    }

}
