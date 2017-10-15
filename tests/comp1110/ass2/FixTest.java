package comp1110.ass2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shijie Qin on 2016/9/20.
 */
public class FixTest {
    @Test
    public void testall(){
        String[] input = {"A","B","C","D","E","F","G","H","I","J","K"};
        String[] output = {"A","B","C","D","E","F","A","B","C","D","E"};
        PiecePlacement pieceplacement = new PiecePlacement("AAA");

        for (int i = 0; i < input.length; i++){
            assertTrue("fix( "+ input[i] +") " + "Except: " + output[i] + ", but got: " + pieceplacement.fix(input[i]) ,pieceplacement.fix(input[i]).equals(output[i]));
        }
    }
}
