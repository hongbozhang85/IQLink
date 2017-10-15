package comp1110.ass2.SolutionDictionary;

import comp1110.ass2.LinkGame;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by hongbo on 16-10-12.
 */
public class Difficulty {

    /**
     * By Hongbo
     * return all the strings after removing 2 piece placement from a given placement
     * @param solution
     * @param removeNum
     * @return
     */
    private static HashSet<String> removePieces(String solution, int removeNum) {
        HashSet<String> rt = new HashSet<>();
        if ( removeNum == 1 ) {
            for ( int i = 0; i < solution.length(); i=i+3) {
                rt.add(solution.replace(solution.substring(i,i+3),""));
            }
        } else {
            HashSet<String> last = removePieces(solution,removeNum-1);
            HashSet<String> tmp;
            for ( String itemLast : last ) {
                tmp = removePieces(itemLast, 1);
                for ( String itemTmp : tmp ) {
                    rt.add(itemTmp);
                }
            }
        }
        return rt;
    }

    /**
     * By Hongbo
     * for 5 pieces starting placement, add one piece to the 4 starting pieceplacement
     * @param solution
     * @return
     */
    private static HashSet<String> removePiecesFor5(String solution) {
        HashSet<String> rt = new HashSet<>();
        for ( int i = 0; i < 8; i++) {
            rt.add( solution.substring(0,12) + solution.substring(3*(i+4),3*(i+5)) );
        }
        return rt;
    }

    /**
     * generate starting placement from >1500 pre-calculated solutions
     * @param args
     */
    public static void main(String[] args) {
        try {

            String Directory = System.getProperty("user.dir");
            ArrayList<String> pieces4 = new ArrayList<>();
            ArrayList<String> solutions = new ArrayList<>();
            ArrayList<Integer> multi = new ArrayList<>();

            //TODO:: change file name here
            String path = Directory + "/src/comp1110/ass2/SolutionDictionary/output_Sols_JAC_HBZ.txt";
            File filename = new File(path);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                Scanner thisLine = new Scanner(line);
                pieces4.add(thisLine.next());
                solutions.add(thisLine.next());
                multi.add(thisLine.nextInt());
                line = br.readLine();
            }
            //System.out.println(pieces4.get(0)+" "+solutions.get(0)+" "+multi.get(0));

            boolean isAppend = true;
            PrintWriter diff01 = new PrintWriter(new FileWriter("src/comp1110/ass2/SolutionDictionary/diff01.txt",isAppend));
            PrintWriter diff02 = new PrintWriter(new FileWriter("src/comp1110/ass2/SolutionDictionary/diff02.txt",isAppend));
            PrintWriter diff03 = new PrintWriter(new FileWriter("src/comp1110/ass2/SolutionDictionary/diff03.txt",isAppend));
            PrintWriter diff04 = new PrintWriter(new FileWriter("src/comp1110/ass2/SolutionDictionary/diff04.txt",isAppend));
            PrintWriter diff05 = new PrintWriter(new FileWriter("src/comp1110/ass2/SolutionDictionary/diff05.txt",isAppend));
            PrintWriter diff06 = new PrintWriter(new FileWriter("src/comp1110/ass2/SolutionDictionary/diff06.txt",isAppend));
            PrintWriter diff07 = new PrintWriter(new FileWriter("src/comp1110/ass2/SolutionDictionary/diff07.txt",isAppend));
            PrintWriter diff08 = new PrintWriter(new FileWriter("src/comp1110/ass2/SolutionDictionary/diff08.txt",isAppend));
            PrintWriter[] diff = {diff01,diff02,diff03,diff04,diff05,diff06,diff07,diff08};

            //for ( int i = 0; i < 10; i++) {
            for ( int i = 0; i < pieces4.size(); i++) {
                System.out.println("the "+i+"th solution.");
                if ( multi.get(i) == 1 ) {
                    diff08.println(pieces4.get(i)+" "+solutions.get(i)); // 4 starting piece
                    HashSet<String> tmp = removePiecesFor5(solutions.get(i)); // 5 starting pieces
                    for (String itemTmp : tmp) {
                        int count = 0;
                        String[] tmpSolutions = LinkGame.getSolutions(itemTmp);
                        if (tmpSolutions.length == 1) {
                            diff07.println(itemTmp + " " + tmpSolutions[0]);
                        }
                        if ( count > 4 ) break;
                    }
                } else {
                    HashSet<String> tmp = removePiecesFor5(solutions.get(i)); // 5 starting pieces
                    for (String itemTmp : tmp) {
                        //int count = 0;
                        String[] tmpSolutions = LinkGame.getSolutions(itemTmp);
                        if (tmpSolutions.length == 1) {
                            diff07.println(itemTmp + " " + tmpSolutions[0]);
                        }
                        //if ( count > 4 ) break;
                    }
                }

                for ( int j = 1; j < 7; j++ ) {  // 12-j staring piece ( from 11 to 6)
                    //System.out.println("the "+(12-j)+" starting piece of the "+i+"th solution.");
                    int count = 0;
                    HashSet<String> tmp = removePieces(solutions.get(i), j);
                    for (String itemTmp : tmp) {
                        String[] tmpSolutions = LinkGame.getSolutions(itemTmp);
                        if ( tmpSolutions != null) {
                            if (tmpSolutions.length == 1) {
                                diff[j - 1].println(itemTmp + " " + tmpSolutions[0]);
                                count++;
                            }
                        }
                        if ( count > 4 ) break;
                    }
                }
            }

            diff01.close();
            diff02.close();
            diff03.close();
            diff04.close();
            diff05.close();
            diff06.close();
            diff07.close();
            diff08.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
