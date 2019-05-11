package Utilities;

/**
 * Created by N550J on 5/18/2017.
 */
public class AAA {
    private static AAA ourInstance = new AAA();

    public static AAA getInstance() {
        return ourInstance;
    }

    private AAA() {
    }
}
