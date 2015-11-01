package Helpers;

import java.util.Random;

/**
 * Created by Flo on 31/10/15.
 */

public class RandomNumberGen {
    private static RandomNumberGen instance = null;
    Random random = new Random();

    protected RandomNumberGen() {

    }
    public static RandomNumberGen getInstance() {
        if(instance == null) {
            instance = new RandomNumberGen();
        }
        return instance;
    }

    public int randomInt()
    {
        return random.nextInt(Integer.MAX_VALUE);
    }
}
