/**
 * Created by ricardoramos on 6/10/16.
 */

public class UserServices extends GestionDB<Usuario>{

    private static UserServices instance;

    private UserServices(){
        super(Usuario.class);
    }

    public static UserServices getInstance(){
        if(instance == null)
            instance = new UserServices();

        return instance;
    }

}
