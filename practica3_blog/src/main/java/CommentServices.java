/**
 * Created by ricardoramos on 6/10/16.
 */


public class CommentServices extends GestionDB<Comment>{

    private static CommentServices instance;

    private CommentServices(){
        super(Comment.class);
    }

    public static CommentServices getInstance(){
        if(instance == null)
            instance = new CommentServices();

        return instance;
    }

}
