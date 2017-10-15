package comp1110.ass2.SolutionDictionary;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hongbo on 16-10-3.
 * Generate all possible string of four character, which ranging from A to L
 */
public class All4Combination {

//    public static void output(String rt) {

//    }

    public static void main(String[] args) throws IOException {

        PrintWriter out = new PrintWriter("src/comp1110/ass2/SolutionDictionary/Combinations.txt");
        PrintWriter out1 = new PrintWriter("src/comp1110/ass2/SolutionDictionary/HBZ.txt");
        PrintWriter out2 = new PrintWriter("src/comp1110/ass2/SolutionDictionary/SJQ.txt");
        PrintWriter out3 = new PrintWriter("src/comp1110/ass2/SolutionDictionary/YFH.txt");

        int startNum = 4;
        int counter = 0;
        String rt;
        //char[] fullString = "ABCDEFGHIJKL".toCharArray();
        String fullString = "ABCDEFGHIJKL";

        for ( int i1 = 0; i1< fullString.length()+1-startNum; i1++ ) {
            String rt1 = "";
            rt1 = rt1 + fullString.charAt(i1);
            for ( int i2 = i1 + 1; i2<fullString.length()+2-startNum; i2++ ) {
                String rt2 = rt1 + fullString.charAt(i2);
                for ( int i3 = i2 + 1; i3<fullString.length()+3-startNum;i3++) {
                    String rt3 = rt2 + fullString.charAt(i3);
                    for ( int i4 = i3 + 1; i4 < fullString.length()+4-startNum;i4++) {
                        String rt4 = rt3 + fullString.charAt(i4);
                        //System.out.println(rt4);
                        counter++;
                        out.println(rt4);
                        if ( counter < 166 ) {
                            out1.println(rt4);
                        } else if ( counter < 331) {
                            out2.println(rt4);
                        } else {
                            out3.println(rt4);
                        }
                    }
                }
            }
        }
        out.close();
        out1.close();
        out2.close();
        out3.close();
    }



}
