package database.DBSet;


import model.Post;
import model.User;

public class Context {
    static Context context;
    public DBSet<User> Users;
    public DBSet<Post> Posts;

    public static Context get() {
        if (context == null) {
            context = new Context();
        }
        return context;
    }

    private Context(){
        Users = new UserDBSet();
        Posts = new PostDBSet();
    }

    public void resetContext(){
        context = null;
    }
}