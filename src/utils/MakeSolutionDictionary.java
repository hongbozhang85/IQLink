package utils;

import comp1110.ass2.LinkGame;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by hongbo on 16-10-2.
 * No longer used!
 */
public class MakeSolutionDictionary {

    public static void main(String[] args) throws IOException {

        final int DIC_SIZE = 2;
        String piecesInStart = "";
        String placement = "";
        String newPiece = "";
        String totalOrientation = "ABCDEFGHIJKL";
        String totalOrigin = "ABCDEFGHIJKLMNOPQRSTUVWX";
        String totalPiece = "ABCDEFGHIJKL";

        //final String dicURL = "SolutionDictionary/";
        String dicURL = "src/comp1110/ass2/SolutionDictionary/";
        String filename = "diff01";
        //PrintWriter dictFile = new PrintWriter("src/comp1110/ass2/SolutionDictionary/diff01.txt");
        //URL url = MakeSolutionDictionary.class.getResource(dicURL+filename+".txt");
        //PrintWriter dictFile = new PrintWriter(url.toString());
        PrintWriter dictFile = new PrintWriter(dicURL+filename+".txt");

        Random ran = new Random();

        for ( int diff = 1; diff<2; diff++ ) { // diff level 1 ~ 8
            int count = 0;
            while ( count < DIC_SIZE ) { // finish when finds DIC_SIZE starting placement
                //piecesInStart = getRandomPieces(diff);
                placement = "";
                newPiece = "";
                boolean isValid = false;
                for ( int i = 1; i < 13 - diff; i ++) { // the i-th piece placement in staring placement
                    while ( (!isValid) || ((!placement.equals(""))&&(!newPiece.equals(""))) ) {
                        char ori = totalOrigin.charAt(ran.nextInt(totalOrigin.length()));
                        char orient = totalOrientation.charAt(ran.nextInt(totalOrientation.length()));
                        char pie = totalPiece.charAt(ran.nextInt(totalPiece.length()));
                        newPiece = "" + ori + pie + orient;
                        isValid = LinkGame.isPlacementValid(placement+newPiece);
                        if ( isValid ) {
                            totalPiece = totalPiece.replace(pie+"","");
                            placement = placement + newPiece;
                            System.out.println(placement);
                        } else {

                        }
                    }
                }
                count++;
                System.out.println("one possible starting piece: " + placement);
            }
        }

        dictFile.println("Hello!");
        dictFile.close();
    }

    private static String getRandomPieces(int diff) {
        String rt = "";
        String total = "ABCDEFGHIJKL";
        Random ran = new Random();
        for ( int i = 0; i< 12-diff; i++ ) {
            //char rollPiece = (char) ('A' + ran.nextInt(12-i));
            char rollPiece = total.charAt(ran.nextInt(12-i));
            total = total.replace(rollPiece+"","");
            rt = rt + rollPiece;
        }
        return rt;
    }

}
