package comp1110.ass2.SolutionDictionary;

import com.sun.xml.internal.bind.v2.TODO;
import comp1110.ass2.LinkGame;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by u5553084 on 4/10/16.
 * By Shijie
 * to find solutions with 4 starting pieces.
 * we first use one piece placement to find all valid placements of four piece with the given one piece placement,
 * then we find solution by calling getSolutions using these 4-piece placement.
 */
public class Solution extends LinkGame {
    public static HashMap check(String pieces) {
        String origin = "ABCDEFGHIJKLMNOPQRSTUVWX";
        String orientation = "ABCDEFGHIJKL";
        String orientation_A = "ABCDEF";
        HashMap<String, String> output = new HashMap();


        String a = pieces.charAt(0)+"";
        String b = pieces.charAt(1)+"";
        String c = pieces.charAt(2)+"";
        String d = pieces.charAt(3)+"";

        ArrayList<String> validpieceplacement_A = new ArrayList<>();
        ArrayList<String> validpieceplacement_B = new ArrayList<>();
        ArrayList<String> validpieceplacement_C = new ArrayList<>();
        ArrayList<String> validpieceplacement_D = new ArrayList<>();

        if (a.equals("A")) {
            for (int i = 0; i < origin.length(); i++) {
                for (int j = 0; j < orientation_A.length(); j++) {
                    String pieceplacement = origin.charAt(i) + a + orientation_A.charAt(j);
                    if (isPlacementValid(pieceplacement)) {
                        validpieceplacement_A.add(pieceplacement);
                    }
                }
            }
        }
        else {
            for (int i = 0; i < origin.length(); i++) {
                for (int j = 0; j < orientation.length(); j++) {
                    String pieceplacement = origin.charAt(i) + a + orientation.charAt(j);
                    if (isPlacementValid(pieceplacement)) {
                        validpieceplacement_A.add(pieceplacement);
                    }
                }
            }
        }

        for (int i = 0; i < origin.length(); i++) {
            for (int j = 0; j < orientation.length(); j++) {
                String pieceplacement = origin.charAt(i) + b + orientation.charAt(j);
                if (isPlacementValid(pieceplacement)){
                    validpieceplacement_B.add(pieceplacement);
                }
            }
        }

        for (int i = 0; i < origin.length(); i++) {
            for (int j = 0; j < orientation.length(); j++) {
                String pieceplacement = origin.charAt(i) + c + orientation.charAt(j);
                if (isPlacementValid(pieceplacement)){
                    validpieceplacement_C.add(pieceplacement);
                }
            }
        }

        for (int i = 0; i < origin.length(); i++) {
            for (int j = 0; j < orientation.length(); j++) {
                String pieceplacement = origin.charAt(i) + d + orientation.charAt(j);
                if (isPlacementValid(pieceplacement)){
                    validpieceplacement_D.add(pieceplacement);
                }
            }
        }

        //boolean run = true;
        //String last_A = validpieceplacement_A.get(validpieceplacement_A.size()-1);
        //String last_B = validpieceplacement_B.get(validpieceplacement_B.size()-1);
        //String last_C = validpieceplacement_C.get(validpieceplacement_C.size()-1);
        //String last_D = validpieceplacement_D.get(validpieceplacement_D.size()-1);
        //String last_placement = last_A+last_B+last_C+last_D;

        //System.out.println(validpieceplacement_A);
        //System.out.println(validpieceplacement_B);
        //System.out.println(validpieceplacement_C);
        //System.out.println(validpieceplacement_D);
        //System.out.println(last_placement);
        //"WBJ WCJ WDJ WEJ"

            for (int i = 0; i < validpieceplacement_A.size(); i++) {
                if (output.size() == 4){
                    break;
                }
                for (int j = 0; j < validpieceplacement_B.size(); j++) {
                    if (output.size() == 4){
                        break;
                    }
                    if ( isPlacementValid(validpieceplacement_A.get(i)+validpieceplacement_B.get(j)) ) {
                        for (int k = 0; k < validpieceplacement_C.size(); k++) {
                            if (output.size() == 4){
                                break;
                            }
                            if ( isPlacementValid(validpieceplacement_A.get(i)+validpieceplacement_B.get(j)+validpieceplacement_C.get(k)) ) {
                                for (int l = 0; l < validpieceplacement_D.size(); l++) {
                                    if (output.size() == 4){
                                        break;
                                    }
                                    String placement = validpieceplacement_A.get(i) + validpieceplacement_B.get(j) + validpieceplacement_C.get(k) + validpieceplacement_D.get(l);
                                    //System.out.println(placement);

                                    String[] sols = getSolutions(placement);
                                    if (sols != null && sols.length == 1) {
                                        output.put(placement, sols[0]);
                                    }

                                }
                            }
                        }
                    }
                }
            }


        return output;

    }

    public static void main(String[] args){

        //LinkGame.getSolutions("KAF");

        //System.out.println(check("BCDE"));
        //System.out.println(isPlacementValid("BBAECAGDDIED"));

        //System.out.println(getSolutions("BBAECAGDDIED"));

        String Directory = System.getProperty("user.dir");
        ArrayList<String> pieces = new ArrayList<>();

        try {
            //TODO:: change file name here
            String path = Directory + "/src/comp1110/ass2/SolutionDictionary/JAC_FourPieces_Sols.txt";
            File filename = new File(path);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                pieces.add(line);
                line = br.readLine();
            }

            //String outfilename = "output_Sols" + ".txt";
            //File outfile = new File(Directory + "/src/comp1110/ass2/SolutionDictionary", outfilename);
            //PrintWriter writer = new PrintWriter(outfile);

//BAAJBLVCDRDA
            for (int i = 2377; i < pieces.size(); i++){
                //HashMap<String, String> sols = check(pieces.get(i));
                String[] sols = getSolutions(pieces.get(i));

                if (sols != null) {
                    //for (int j = 0; j < sols.size(); j++) {
                    //for (String key : sols.keySet()) {
                      //  writer.println(key+" "+sols.get(key));
                        //writer.println(sols);
                    //}
                    for (int j = 0; j < sols.length; j++){
                        //TODO:: change file name here
                        FileWriter fileWriter = new FileWriter("src/comp1110/ass2/SolutionDictionary/output_Sols_JAC_HBZ.txt",true);
                        PrintWriter printWriter = new PrintWriter(fileWriter);

                        printWriter.println(pieces.get(i) +" "+sols[j] + " " + sols.length);

                        fileWriter.close();
                        printWriter.close();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
