package singleton;

import modelo.User;

/**
 * Created by nucleus on 08/03/17.
 */
public class Singleton {

    private static Singleton uniqueInstance;

    public User currentUser;

    private Singleton() {
    }

    public static synchronized Singleton getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new Singleton();

        return uniqueInstance;
    }
}
