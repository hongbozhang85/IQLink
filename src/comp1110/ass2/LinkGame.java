package comp1110.ass2;

import javafx.collections.ArrayChangeListener;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class provides the text interface for the Link Game
 * <p>
 * The game is based directly on Smart Games' IQ-Link game
 * (http://www.smartgames.eu/en/smartgames/iq-link)
 */
public class LinkGame {

    //static HashMap<Integer,ArrayList<UandO>> occupationArray = new HashMap<>(); // record occupation status of each peg
    static HashMap<Integer, ArrayList<String>> occupationArray = new HashMap<>(); // record occupation status of each peg
    //static HashMap<Integer, ArrayList<String>> lastOccupationArray = new HashMap<>(); // record the last occupationArray
    static int[] pegIndex = new int[3]; // record the peg index occupied by the last placed piece
    static long totalDeepCopyTime = 0;
    static int callIsPlacementValid = 0;

    /**
     * By Yifan
     * Determine whether a piece placement is well-formed according to the following:
     * - it consists of exactly three characters
     * - the first character is in the range A .. X
     * - the second character is in the range A .. L
     * - the third character is in the range A .. F if the second character is A, otherwise
     * in the range A .. L
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */
    public static boolean isPiecePlacementWellFormed(String piecePlacement) {
        int x = piecePlacement.length();
        char a = piecePlacement.charAt(0);
        char b = piecePlacement.charAt(1);
        char c = piecePlacement.charAt(2);
        String s1 = "ABCDEFGHIJKLMNOPQRSTUVWX";
        String s2 = "ABCDEFGHIJKL";
        String s3 = "ABCDEFGHIJKL";
        String s4 = "ABCDEF";
        boolean b1 = Character.isUpperCase(a) && Character.isUpperCase(b) && Character.isUpperCase(c);
        int i1 = s1.indexOf(a);
        int i2 = s2.indexOf(b);
        int i3 = s3.indexOf(c);
        int i4 = s4.indexOf(c);
        if (b == 'A') {
            return (x == 3 && i1 >= 0 && i2 >= 0 && i4 >= 0 && b1);
        }
        // FIXME Task 3: determine whether a piece placement is well-formed
        else {
            return (x == 3 && i1 >= 0 && i2 >= 0 && i3 >= 0 && b1);
        }
    }

    /**
     * By Shijie
     * Determine whether a placement string is well-formed:
     * - it consists of exactly N three-character piece placements (where N = 1 .. 12);
     * - each piece placement is well-formed
     * - no piece appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    public static boolean isPlacementWellFormed(String placement) {
        // FIXME Task 4: determine whether a placement is well-forme

        String a = "";
        String b = "";

        //testempty
        if (placement == null || placement.length() == 0) {
            return false;
        } else {

            for (int i = 0; i < placement.length(); i++) {
                if ((int) placement.charAt(i) >= (int) 'A' && (int) placement.charAt(i) <= (int) 'X') {
                    a = a + placement.charAt(i);
                }
            }

            //testbad
            if (placement.equals(a) == false) {
                return false;
            } else {

                //testincomplete
                if (placement.length() % 3 != 0) {
                    return false;
                } else {

                    for (int i = 1; i < placement.length(); i = i + 3) {
                        if (b.contains(placement.charAt(i) + "") == false) {
                            b = b + placement.charAt(i);
                        }
                    }

                    //testduplicate
                    if (b.length() == placement.length() / 3) {
                        return true;
                    } else return false;
                }
            }
        }
    }

    /**
     * By Hongbo
     * The order of units in a piece: in its 0-rotation state:
     * left-most is indexed 0
     * origin of a piece ( middle) is indexed 1
     * the right unit (in A B C) and the upper unit ( in D ... L) is indexed 2.
     * Return a array of peg locations according to which pegs the given piece placement touches.
     * The values in the array should be ordered according to the units that constitute the
     * piece.
     * The code needs to account for the origin of the piece, the piece shape, and the piece
     * orientation.
     * @param piecePlacement A valid string describing a piece placement
     * @return An array of integers corresponding to the pegs which the piece placement touches,
     * listed in the normal order of units for that piece.   The value 0 corresponds to
     * peg 'A', 1 to peg 'B', etc.
     */
    static int[] getPegsForPiecePlacement(String piecePlacement) {
        // FIXME Task 6: determine the pegs touched by a piece placement
        char origin = piecePlacement.charAt(0);
        char piece = piecePlacement.charAt(1);
        char orient = piecePlacement.charAt(2);
        int[] pegsIndex = new int[3];

        HashSet<Character> iShape = new HashSet<>(); // I shape piece: A, B, C
        iShape.add('A');
        iShape.add('B');
        iShape.add('C');
        HashSet<Character> oShape = new HashSet<>(); // obtuse angle piece: D E F G H
        oShape.add('D');
        oShape.add('E');
        oShape.add('F');
        oShape.add('G');
        oShape.add('H');
        HashSet<Character> aShape = new HashSet<>(); // acute angle piece: I J K L
        aShape.add('I');
        aShape.add('J');
        aShape.add('K');
        aShape.add('L');

        for (int i = 0; i < 3; i++) {
            if (iShape.contains(piece)) {
                pegsIndex[i] = getIShapePegsForPiecePlacement(origin, orient, i);
            } else if (oShape.contains(piece)) {
                pegsIndex[i] = getOShapePegsForPiecePlacement(origin, orient, i);
            } else if (aShape.contains(piece)) {
                pegsIndex[i] = getAShapePegsForPiecePlacement(origin, orient, i);
            }
        }

        return pegsIndex;
    }

    /**
     * By Hongbo
     * return the pegs that the given unit in a give piece placement touches: I shape
     * @param origin: the origin of the piece
     * @param orient: the orientation of the piece
     * @param index:  the index of the concerned unit.
     * @return 1 ... 24, or -1 for the case if there is any unit in a piece is out of pegs
     */
    static int getIShapePegsForPiecePlacement(char origin, char orient, int index) {
        // left and right edge
        if (origin == 'A') {
            if (orient == 'A' || orient == 'G' || orient == 'B' || orient == 'H') {
                if (index == 0) return -1;
            } else if (orient == 'D' || orient == 'J' || orient == 'E' || orient == 'K') {
                if (index == 2) return -1;
            } else if (orient == 'C' || orient == 'I' || orient == 'F' || orient == 'L') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'S') {
            if (orient == 'A' || orient == 'G' || orient == 'F' || orient == 'L' || orient == 'E' || orient == 'K') {
                if (index == 0) return -1;
            } else {
                if (index == 2) return -1;
            }
        }
        if (origin == 'F') {
            if (orient == 'C' || orient == 'I' || orient == 'D' || orient == 'J' || orient == 'B' || orient == 'H') {
                if (index == 0) return -1;
            } else if (orient == 'A' || orient == 'G' || orient == 'F' || orient == 'L' || orient == 'E' || orient == 'K') {
                if (index == 2) return -1;
            }
        }
        if (origin == 'X') {
            if (orient == 'D' || orient == 'J' || orient == 'E' || orient == 'K') {
                if (index == 0) return -1;
            } else if (orient == 'A' || orient == 'G' || orient == 'B' || orient == 'H') {
                if (index == 2) return -1;
            } else if (orient == 'C' || orient == 'I' || orient == 'F' || orient == 'L') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'G') {
            if (orient == 'A' || orient == 'G') {
                if (index == 0) return -1;
            } else if (orient == 'D' || orient == 'J') {
                if (index == 2) return -1;
            }
        }
        if (origin == 'R') {
            if (orient == 'A' || orient == 'G') {
                if (index == 2) return -1;
            } else if (orient == 'D' || orient == 'J') {
                if (index == 0) return -1;
            }
        }
        if (origin == 'M') {
            if (orient == 'A' || orient == 'G' || orient == 'F' || orient == 'L' || orient == 'B' || orient == 'H') {
                if (index == 0) return -1;
            } else if (orient == 'C' || orient == 'I' || orient == 'D' || orient == 'J' || orient == 'E' || orient == 'K') {
                if (index == 2) return -1;
            }
        }
        if (origin == 'L') {
            if (orient == 'A' || orient == 'G' || orient == 'F' || orient == 'L' || orient == 'B' || orient == 'H') {
                if (index == 2) return -1;
            } else if (orient == 'C' || orient == 'I' || orient == 'D' || orient == 'J' || orient == 'E' || orient == 'K') {
                if (index == 0) return -1;
            }
        }
        /*if ( origin=='A'||origin=='G'||origin=='M'||origin=='S'||origin=='F'||origin=='L'||origin=='R'||origin=='X') {
            return -1;
        }*/
        // first and last row
        if (origin >= 'B' && origin <= 'E') {
            if (orient == 'B' || orient == 'H' || orient == 'C' || orient == 'I') {
                if (index == 0) return -1;
            } else if (orient == 'F' || orient == 'L' || orient == 'E' || orient == 'K') {
                if (index == 2) return -1;
            }
        }
        if (origin >= 'T' && origin <= 'W') {
            if (orient == 'B' || orient == 'H' || orient == 'C' || orient == 'I') {
                if (index == 2) return -1;
            } else if (orient == 'F' || orient == 'L' || orient == 'E' || orient == 'K') {
                if (index == 0) return -1;
            }
        }
        /*if ( ((origin>='B'&&origin<='E')||(origin>='T'&&origin<='W')) && ((orient!='A')&&(orient!='G')) ) {
            return -1;
        }*/
        // other situations
        if (orient == 'A' || orient == 'G') {
            return ((char) ((int) origin + (index - 1))) - 'A';
        } else if (orient == 'B' || orient == 'H') {
            if ((origin >= 'G' && origin <= 'L') || (origin >= 'S' && origin <= 'X')) {
                if (index == 0 || index == 1) {
                    return ((char) ((int) origin + (index - 1) * 6)) - 'A';
                } else {
                    return ((char) ((int) origin + (index - 1) * 6 + 1)) - 'A';
                }
            } else if (origin >= 'M' && origin <= 'R' || (origin >= 'A' && origin <= 'F')) {
                if (index == 0) {
                    return ((char) ((int) origin + (index - 1) * 6 - 1)) - 'A';
                } else {
                    return ((char) ((int) origin + (index - 1) * 6)) - 'A';
                }
            }
        } else if (orient == 'C' || orient == 'I') {
            if (origin >= 'G' && origin <= 'L' || (origin >= 'S' && origin <= 'X')) {
                if (index == 0) {
                    return ((char) ((int) origin + (index - 1) * 6 + 1)) - 'A';
                } else {
                    return ((char) ((int) origin + (index - 1) * 6)) - 'A';
                }
            } else if (origin >= 'M' && origin <= 'R' || (origin >= 'A' && origin <= 'F')) {
                if (index == 0 || index == 1) {
                    return ((char) ((int) origin + (index - 1) * 6)) - 'A';
                } else {
                    return ((char) ((int) origin + (index - 1) * 6 - 1)) - 'A';
                }
            }
        } else if (orient == 'D' || orient == 'J') {
            return ((char) ((int) origin - (index - 1))) - 'A';
        } else if (orient == 'E' || orient == 'K') {
            if (origin >= 'G' && origin <= 'L' || (origin >= 'S' && origin <= 'X')) {
                if (index == 2 || index == 1) {
                    return ((char) ((int) origin - (index - 1) * 6)) - 'A';
                } else {
                    return ((char) ((int) origin - (index - 1) * 6 + 1)) - 'A';
                }
            } else if (origin >= 'M' && origin <= 'R' || (origin >= 'A' && origin <= 'F')) {
                if (index == 2) {
                    return ((char) ((int) origin - (index - 1) * 6 - 1)) - 'A';
                } else {
                    return ((char) ((int) origin - (index - 1) * 6)) - 'A';
                }
            }
        } else if (orient == 'F' || orient == 'L') {
            if (origin >= 'G' && origin <= 'L' || (origin >= 'S' && origin <= 'X')) {
                if (index == 2) {
                    return ((char) ((int) origin - (index - 1) * 6 + 1)) - 'A';
                } else {
                    return ((char) ((int) origin - (index - 1) * 6)) - 'A';
                }
            } else if (origin >= 'M' && origin <= 'R' || (origin >= 'A' && origin <= 'F')) {
                if (index == 2 || index == 1) {
                    return ((char) ((int) origin - (index - 1) * 6)) - 'A';
                } else {
                    return ((char) ((int) origin - (index - 1) * 6 - 1)) - 'A';
                }
            }
        }
        return -1;
    }

    /**
     * By Hongbo
     * return the pegs that the given unit in a give piece placement touches: octuse angle
     * @param origin: the origin of the piece
     * @param orient: the orientation of the piece
     * @param index:  the index of the concerned unit.
     * @return 1 ... 24, or -1 for the case if there is any unit in a piece is out of pegs
     */
    static int getOShapePegsForPiecePlacement(char origin, char orient, int index) {
        // boundary case
        // left and right edge
        if (origin == 'A') {
            if (orient == 'B' || orient == 'C' || orient == 'G' || orient == 'L') {
                if (index == 0) return -1;
            } else if (orient == 'D' || orient == 'E' || orient == 'J' || orient == 'K') {
                if (index == 2) return -1;
            } else if (orient == 'A' || orient == 'F' || orient == 'H' || orient == 'I') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'S') {
            if (orient == 'A' || orient == 'F' || orient == 'K' || orient == 'L') {
                if (index == 0) return -1;
            } else if (orient == 'C' || orient == 'D' || orient == 'H' || orient == 'I') {
                if (index == 2) return -1;
            } else if (orient == 'E' || orient == 'G') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'F') {
            if (orient == 'C' || orient == 'D' || orient == 'H' || orient == 'I') {
                if (index == 0) return -1;
            } else if (orient == 'A' || orient == 'F' || orient == 'K' || orient == 'L') {
                if (index == 2) return -1;
            } else if (orient == 'B' || orient == 'J') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'X') {
            if (orient == 'E' || orient == 'F' || orient == 'I' || orient == 'J') {
                if (index == 0) return -1;
            } else if (orient == 'A' || orient == 'B' || orient == 'G' || orient == 'H') {
                if (index == 2) return -1;
            } else if (orient == 'C' || orient == 'D' || orient == 'K' || orient == 'L') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'G') {
            if (orient == 'A' || orient == 'G') {
                if (index == 0) return -1;
            } else if (orient == 'E' || orient == 'I') {
                if (index == 2) return -1;
            }
        }
        if (origin == 'R') {
            if (orient == 'B' || orient == 'L') {
                if (index == 2) return -1;
            } else if (orient == 'D' || orient == 'J') {
                if (index == 0) return -1;
            }
        }
        if (origin == 'M') {
            if (orient == 'A' || orient == 'B' || orient == 'G' || orient == 'L') {
                if (index == 0) return -1;
            } else if (orient == 'D' || orient == 'E' || orient == 'I' || orient == 'J') {
                if (index == 2) return -1;
            } else if (orient == 'F' || orient == 'H') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'L') {
            if (orient == 'A' || orient == 'B' || orient == 'G' || orient == 'L') {
                if (index == 2) return -1;
            } else if (orient == 'D' || orient == 'E' || orient == 'I' || orient == 'J') {
                if (index == 0) return -1;
            } else if (orient == 'C' || orient == 'K') {
                if (index == 0 || index == 2) return -1;
            }
        }
        // first and last row
        if (origin >= 'B' && origin <= 'E') {
            if (orient == 'B' || orient == 'H' || orient == 'C' || orient == 'I') {
                if (index == 0) return -1;
            } else if (orient == 'A' || orient == 'F' || orient == 'J' || orient == 'K') {
                if (index == 2) return -1;
            }
        }
        if (origin >= 'T' && origin <= 'W') {
            if (orient == 'C' || orient == 'D' || orient == 'G' || orient == 'H') {
                if (index == 2) return -1;
            } else if (orient == 'E' || orient == 'F' || orient == 'K' || orient == 'L') {
                if (index == 0) return -1;
            }
        }
        // second row and last row
        if (origin >= 'G' && origin <= 'L' || (origin >= 'S' && origin <= 'X')) {
            if (orient == 'A') {
                if (index == 0) {
                    return ((char) ((int) origin - 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 5)) - 'A';
                }
            } else if (orient == 'B') {
                if (index == 0) {
                    return ((char) ((int) origin - 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 1)) - 'A';
                }
            } else if (orient == 'C') {
                if (index == 0) {
                    return ((char) ((int) origin - 5)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 7)) - 'A';
                }
            } else if (orient == 'D') {
                if (index == 0) {
                    return ((char) ((int) origin + 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 6)) - 'A';
                }
            } else if (orient == 'E') {
                if (index == 0) {
                    return ((char) ((int) origin + 7)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 1)) - 'A';
                }
            } else if (orient == 'F') {
                if (index == 0) {
                    return ((char) ((int) origin + 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 6)) - 'A';
                }
            } else if (orient == 'G') {
                if (index == 0) {
                    return ((char) ((int) origin - 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 7)) - 'A';
                }
            } else if (orient == 'H') {
                if (index == 0) {
                    return ((char) ((int) origin - 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 6)) - 'A';
                }
            } else if (orient == 'I') {
                if (index == 0) {
                    return ((char) ((int) origin - 5)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 1)) - 'A';
                }
            } else if (orient == 'J') {
                if (index == 0) {
                    return ((char) ((int) origin + 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 6)) - 'A';
                }
            } else if (orient == 'K') {
                if (index == 0) {
                    return ((char) ((int) origin + 7)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 5)) - 'A';
                }
            } else if (orient == 'L') {
                if (index == 0) {
                    return ((char) ((int) origin + 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 1)) - 'A';
                }
            }
        }
        // third row and first row
        if (origin >= 'M' && origin <= 'R' || (origin >= 'A' && origin <= 'F')) {
            if (orient == 'A') {
                if (index == 0) {
                    return ((char) ((int) origin - 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 6)) - 'A';
                }
            } else if (orient == 'B') {
                if (index == 0) {
                    return ((char) ((int) origin - 7)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 1)) - 'A';
                }
            } else if (orient == 'C') {
                if (index == 0) {
                    return ((char) ((int) origin - 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 6)) - 'A';
                }
            } else if (orient == 'D') {
                if (index == 0) {
                    return ((char) ((int) origin + 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 5)) - 'A';
                }
            } else if (orient == 'E') {
                if (index == 0) {
                    return ((char) ((int) origin + 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 1)) - 'A';
                }
            } else if (orient == 'F') {
                if (index == 0) {
                    return ((char) ((int) origin + 5)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 7)) - 'A';
                }
            } else if (orient == 'G') {
                if (index == 0) {
                    return ((char) ((int) origin - 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 6)) - 'A';
                }
            } else if (orient == 'H') {
                if (index == 0) {
                    return ((char) ((int) origin - 7)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 5)) - 'A';
                }
            } else if (orient == 'I') {
                if (index == 0) {
                    return ((char) ((int) origin - 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 1)) - 'A';
                }
            } else if (orient == 'J') {
                if (index == 0) {
                    return ((char) ((int) origin + 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 7)) - 'A';
                }
            } else if (orient == 'K') {
                if (index == 0) {
                    return ((char) ((int) origin + 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 6)) - 'A';
                }
            } else if (orient == 'L') {
                if (index == 0) {
                    return ((char) ((int) origin + 5)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 1)) - 'A';
                }
            }
        }
        return -1;
    }

    /**
     * By Hongbo
     * return the pegs that the given unit in a give piece placement touches: acute angle
     * @param origin: the origin of the piece
     * @param orient: the orientation of the piece
     * @param index:  the index of the concerned unit.
     * @return 1 ... 24, or -1 for the case if there is any unit in a piece is out of pegs
     */
    static int getAShapePegsForPiecePlacement(char origin, char orient, int index) {
        //boundary case
        /*if ( origin == 'A' && ( orient != 'D' && orient != 'K' )) return -1;
        if ( origin == 'F' && ( orient != 'F' && orient != 'G' )) return -1;
        if ( origin == 'S' && ( orient != 'B' && orient != 'C' && orient != 'I' && orient != 'J' )) return -1;
        if ( origin == 'X' && ( orient != 'A' && orient != 'H' )) return -1;
        if ( origin == 'G' && ( orient == 'A' || orient == 'F' || orient == 'G' || orient == 'H' )) return -1;
        if ( origin == 'M' && ( orient != 'C' && orient != 'D' && orient != 'J' && orient != 'K' )) return -1;
        if ( origin == 'L' && ( orient != 'A' && orient != 'F' && orient != 'G' && orient != 'H' )) return -1;
        if ( origin == 'R' && ( orient == 'C' || orient == 'D' || orient == 'J' || orient == 'K' )) return -1;
        if ( origin>'A' && origin<'F' && (orient=='A'||orient=='B'||orient=='C'||orient=='H'||orient=='I'||orient=='J')) return -1;
        if ( origin>'S' && origin<'X' && (orient=='D'||orient=='E'||orient=='F'||orient=='G'||orient=='K'||orient=='L')) return -1;*/
        // left and right edge
        if (origin == 'A') {
            if (orient == 'C' || orient == 'L') {
                if (index == 0) return -1;
            } else if (orient == 'E' || orient == 'J') {
                if (index == 2) return -1;
            } else if (orient == 'A' || orient == 'B' || orient == 'F' || orient == 'G' || orient == 'H' || orient == 'I') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'S') {
            if (orient == 'A' || orient == 'K') {
                if (index == 0) return -1;
            } else if (orient == 'D' || orient == 'H') {
                if (index == 2) return -1;
            } else if (orient == 'E' || orient == 'F' || orient == 'G' || orient == 'L') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'F') {
            if (orient == 'H' || orient == 'D') {
                if (index == 0) return -1;
            } else if (orient == 'A' || orient == 'K') {
                if (index == 2) return -1;
            } else if (orient == 'B' || orient == 'C' || orient == 'I' || orient == 'J') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'X') {
            if (orient == 'F' || orient == 'I') {
                if (index == 0) return -1;
            } else if (orient == 'B' || orient == 'G') {
                if (index == 2) return -1;
            } else if (orient == 'C' || orient == 'D' || orient == 'E' || orient == 'J' || orient == 'K' || orient == 'L') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'G') {
            if (orient == 'A' || orient == 'G') {
                if (index == 0) return -1;
            } else if (orient == 'F' || orient == 'H') {
                if (index == 2) return -1;
            }
        }
        if (origin == 'R') {
            if (orient == 'C' || orient == 'K') {
                if (index == 2) return -1;
            } else if (orient == 'D' || orient == 'J') {
                if (index == 0) return -1;
            }
        }
        if (origin == 'M') {
            if (orient == 'B' || orient == 'L') {
                if (index == 0) return -1;
            } else if (orient == 'E' || orient == 'I') {
                if (index == 2) return -1;
            } else if (orient == 'A' || orient == 'F' || orient == 'G' || orient == 'H') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin == 'L') {
            if (orient == 'B' || orient == 'L') {
                if (index == 2) return -1;
            } else if (orient == 'E' || orient == 'I') {
                if (index == 0) return -1;
            } else if (orient == 'C' || orient == 'J' || orient == 'D' || orient == 'K') {
                if (index == 0 || index == 2) return -1;
            }
        }
        // first and last row
        if (origin >= 'B' && origin <= 'E') {
            if (orient == 'H' || orient == 'C') {
                if (index == 0) return -1;
            } else if (orient == 'A' || orient == 'J') {
                if (index == 2) return -1;
            } else if (orient == 'B' || orient == 'I') {
                if (index == 0 || index == 2) return -1;
            }
        }
        if (origin >= 'T' && origin <= 'W') {
            if (orient == 'D' || orient == 'G') {
                if (index == 2) return -1;
            } else if (orient == 'F' || orient == 'K') {
                if (index == 0) return -1;
            } else if (orient == 'E' || orient == 'L') {
                if (index == 0 || index == 2) return -1;
            }
        }
        // first and third row
        if ((origin >= 'A' && origin <= 'F') || (origin >= 'M' && origin <= 'R')) {
            if (orient == 'A') {
                if (index == 0) {
                    return ((char) ((int) origin - 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 7)) - 'A';
                }
            } else if (orient == 'B') {
                if (index == 0) {
                    return ((char) ((int) origin - 7)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 6)) - 'A';
                }
            } else if (orient == 'C') {
                if (index == 0) {
                    return ((char) ((int) origin - 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 1)) - 'A';
                }
            } else if (orient == 'D') {
                if (index == 0) {
                    return ((char) ((int) origin + 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 6)) - 'A';
                }
            } else if (orient == 'E') {
                if (index == 0) {
                    return ((char) ((int) origin + 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 5)) - 'A';
                }
            } else if (orient == 'F') {
                if (index == 0) {
                    return ((char) ((int) origin + 5)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 1)) - 'A';
                }
            } else if (orient == 'G') {
                if (index == 0) {
                    return ((char) ((int) origin - 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 5)) - 'A';
                }
            } else if (orient == 'H') {
                if (index == 0) {
                    return ((char) ((int) origin - 7)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 1)) - 'A';
                }
            } else if (orient == 'I') {
                if (index == 0) {
                    return ((char) ((int) origin - 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 7)) - 'A';
                }
            } else if (orient == 'J') {
                if (index == 0) {
                    return ((char) ((int) origin + 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 6)) - 'A';
                }
            } else if (orient == 'K') {
                if (index == 0) {
                    return ((char) ((int) origin + 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 1)) - 'A';
                }
            } else if (orient == 'L') {
                if (index == 0) {
                    return ((char) ((int) origin + 5)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 6)) - 'A';
                }
            }
        }
        // second and last row
        if ((origin >= 'G' && origin <= 'L') || (origin >= 'S' && origin <= 'X')) {
            if (orient == 'A') {
                if (index == 0) {
                    return ((char) ((int) origin - 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 6)) - 'A';
                }
            } else if (orient == 'B') {
                if (index == 0) {
                    return ((char) ((int) origin - 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 5)) - 'A';
                }
            } else if (orient == 'C') {
                if (index == 0) {
                    return ((char) ((int) origin - 5)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 1)) - 'A';
                }
            } else if (orient == 'D') {
                if (index == 0) {
                    return ((char) ((int) origin + 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 7)) - 'A';
                }
            } else if (orient == 'E') {
                if (index == 0) {
                    return ((char) ((int) origin + 7)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 6)) - 'A';
                }
            } else if (orient == 'F') {
                if (index == 0) {
                    return ((char) ((int) origin + 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 1)) - 'A';
                }
            } else if (orient == 'G') {
                if (index == 0) {
                    return ((char) ((int) origin - 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 6)) - 'A';
                }
            } else if (orient == 'H') {
                if (index == 0) {
                    return ((char) ((int) origin - 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 1)) - 'A';
                }
            } else if (orient == 'I') {
                if (index == 0) {
                    return ((char) ((int) origin - 5)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 6)) - 'A';
                }
            } else if (orient == 'J') {
                if (index == 0) {
                    return ((char) ((int) origin + 1)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin - 5)) - 'A';
                }
            } else if (orient == 'K') {
                if (index == 0) {
                    return ((char) ((int) origin + 7)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 1)) - 'A';
                }
            } else if (orient == 'L') {
                if (index == 0) {
                    return ((char) ((int) origin + 6)) - 'A';
                } else if (index == 1) {
                    return origin - 'A';
                } else {
                    return ((char) ((int) origin + 7)) - 'A';
                }
            }
        }
        return -1;
    }

    /**
     * By Hongbo
     * @param placement: the placement array, such as BAAHBATCJRDKWEBEFDNGLPHEDIFMJJQKIKLJ
     * @return: a string array, each element is a pieceplacement string. such asS BAA,HBA,TCJ,RDK,WEB,EFD,NGL,PHE,DIF,MJJ,QKI,KLJ
     */
    static String[] toPlacementStringArray(String placement) {
        String[] piecePlaceArray = new String[placement.length() / 3];
        if (isPlacementWellFormed(placement)) {
            for (int i = 0; i < placement.length() / 3; i++) {
                piecePlaceArray[i] = placement.substring(3 * i, 3 * i + 3);
            }
        }
        return piecePlaceArray;
    }

    /**
     * By Hongbo
     * not used any more
     * TODO upgrade the algorithm: record the last one, use iteration, and only check the pegs of new placing piece
     * Determine whether a placement is valid.  To be valid, the placement must be well-formed
     * and each piece must correctly connect with each other.
     * @param placement A placement string
     * @return True if the placement is valid
    */
    public static boolean isPlacementValidOld(String placement) {
        // FIXME Task 7: determine whether a placement is valid

        //placement = "KAFCBGUCAGDFLEFPFBBGESHBWIJKJAHKLJLH";
        //placement = "BAAHBATCJRDKWEBEFDNGLPHEDIFMJJQKIKLJ";
        occupationArray.clear();
        if (!isPlacementWellFormed(placement)) return false;

        if ( outOfPeg(placement) ) return false;

        setOccupationArray(placement);

        //System.out.println(occupationArray);

        return checkOccupationArray();
    }

    /**
     * By Hongbo
     * use iteration method
     * Determine whether a placement is valid.  To be valid, the placement must be well-formed
     * and each piece must correctly connect with each other.
     * @param placement A placement string
     * @return True if the placement is valid
     */
    public static boolean isPlacementValid(String placement) {
        int len = placement.length();
        if ( len > 3 ) {
            if ( isPlacementValid(placement.substring(0,len-3)) ) {
                return isPlacementValid(placement.substring(0, len - 3), placement.substring(len - 3, len));
            }
        } else {
            if (!isPlacementWellFormed(placement)) return false;
            occupationArray.clear();
            if ( outOfPegSinglePiece(placement) ) return false;
            setOccupationArray("",placement);
            return checkOccupationArraySinglePiece();
        }
        return false;
    }

    /**
     * By Hongbo
     * TODO upgrade the algorithm: record the last one, use iteration, and only check the pegs of new placing piece
     * check whether any unit of pieces in placement is out of peg or not
     * @param placement placement String
     * @return true if some unit is out of peg, false if none unit is out of peg
     */
    private static boolean outOfPeg(String placement) {
        String[] piecePlaceArray = toPlacementStringArray(placement);
        for (int i = 0; i < piecePlaceArray.length; i++) {
            int[] pegOccupied = getPegsForPiecePlacement(piecePlaceArray[i]);
            for (int j = 0; j < 3; j++) {
                if (pegOccupied[j] == -1) return true;
            }
        }
        return false;
    }

    /**
     * By Hongbo
     * TODO upgrade the algorithm: record the last one, use iteration, and only check the pegs of new placing piece
     * @param placement set occupationArray, which record the occupation status of each peg
     */
    private static void setOccupationArray(String placement) {
        // set occupationArray which record which pegs are occupied by which units.
        String[] piecePlaceArray = toPlacementStringArray(placement);
        //String[] piecePlaceArray = toPlacementStringArray("BAAHBATCJRDKWEBEFDNGLPHEDIFMJJQKIKLJ");
        for (int i = 0; i < piecePlaceArray.length; i++) {
            String currentPiecePlacementString = piecePlaceArray[i];
            int[] pegOccupied = getPegsForPiecePlacement(currentPiecePlacementString);
            //System.out.println(pegOccupied[0] + " " + pegOccupied[1] + " " + pegOccupied[2]);
            PiecePlacement currentPiecePlacement = new PiecePlacement(currentPiecePlacementString);

            //System.out.println("currentPiecePlacementString"+currentPiecePlacementString);
            //System.out.println(currentPiecePlacement.piece);
            //System.out.println(currentPiecePlacement.piece.uandos[2]);

            currentPiecePlacement.translateorientation(currentPiecePlacement.piece, currentPiecePlacement.ori);

            for (int j = 0; j < 3; j++) {
                //if (pegOccupied[j] == -1) return false;
                if (!occupationArray.containsKey(pegOccupied[j])) {
                    //ArrayList<UandO> newUandOArray = new ArrayList<>();
                    ArrayList<String> newUandOArray = new ArrayList<>();
                    newUandOArray.add(currentPiecePlacement.piece.uandos[j].toString());
                    occupationArray.put(pegOccupied[j], newUandOArray);
                    //System.out.println(pegOccupied[j] +" "+ occupationArray.get(pegOccupied[j]));
                } else {
                    //System.out.println(pegOccupied[j] +" "+ occupationArray.get(pegOccupied[j]));
                    occupationArray.get(pegOccupied[j]).add(currentPiecePlacement.piece.uandos[j].toString());
                    //System.out.println(pegOccupied[j] +" "+ occupationArray.get(pegOccupied[j]));
                }
            }
        }
    }

    /**
     * By Hongbo
     * TODO upgrade the algorithm: record the last one, use iteration, and only check the pegs of new placing piece
     * check the placement is valid through checking the occupation array
     * a peg cannot place more than two units
     * a peg cannot place two balls or two rings
     * if a peg is placed with a ball and a ring, they must satisfy: ball1~ring1, bing2~ring2, bing1~ring2
     * the ball and the ring on the same peg must match each other
     * @return true if valid, false if invalid
     */
    private static boolean checkOccupationArray() {
        for (Integer keys : occupationArray.keySet()) {

            // test whether a peg is occupied by more than two units
            if (occupationArray.get(keys).size() > 2) return false;

            // test whether a peg is occupied by two balls or two rings
            if (occupationArray.get(keys).size() == 2) {
                //if ( occupationArray.get(keys).get(0).unit.type == occupationArray.get(keys).get(1).unit.type ) {
                if (occupationArray.get(keys).get(0).charAt(0) == occupationArray.get(keys).get(1).charAt(0)) {
                    return false;
                }
            }

            /*// test whether a peg is occupied by a ball and a ring with the same opening
            if ( occupationArray.get(keys).size() == 2 ) {
                //if ( occupationArray.get(keys).get(0).unit.open != occupationArray.get(keys).get(1).unit.open ) {
                if ( occupationArray.get(keys).get(0).charAt(1) != occupationArray.get(keys).get(1).charAt(1) ) {
                    return false;
                }
            }*/

            // test ball1~ring1, bing2~ring2, bing1~ring2
            if (occupationArray.get(keys).size() == 2) {
                if (occupationArray.get(keys).get(0).charAt(1) != occupationArray.get(keys).get(1).charAt(1)) {
                    String first = occupationArray.get(keys).get(0).substring(0, 2);
                    String second = occupationArray.get(keys).get(1).substring(0, 2);
                    boolean condition1 = first.equals("B1") && second.equals("R2");
                    boolean condition2 = first.equals("R2") && second.equals("B1");
                    boolean condition = condition1 || condition2;
                    if (!condition) {
                        return false;
                    }
                }
            }

            // test whether a peg is occupied by ball1 and ring1 with matching direction
            if (occupationArray.get(keys).size() == 2) {
                if (occupationArray.get(keys).get(0).charAt(1) == occupationArray.get(keys).get(1).charAt(1)) {
                    //if ( occupationArray.get(keys).get(0).orientation != occupationArray.get(keys).get(1).orientation ) {
                    if (occupationArray.get(keys).get(0).charAt(2) != occupationArray.get(keys).get(1).charAt(2)) {
                        return false;
                    }
                }
            }

            // test whether a peg is occupied by ball1 and ring2 with matching direction
            if (occupationArray.get(keys).size() == 2) {
                if (occupationArray.get(keys).get(0).charAt(1) != occupationArray.get(keys).get(1).charAt(1)) {
                    String first = "";
                    String second = "";
                    if (occupationArray.get(keys).get(0).substring(0, 2).equals("B1") && occupationArray.get(keys).get(1).substring(0, 2).equals("R2")) {
                        first = occupationArray.get(keys).get(0);
                        second = occupationArray.get(keys).get(1);
                    } else if (occupationArray.get(keys).get(0).substring(0, 2).equals("R2") && occupationArray.get(keys).get(1).substring(0, 2).equals("B1")) {
                        first = occupationArray.get(keys).get(1);
                        second = occupationArray.get(keys).get(0);
                    } else {
                        System.out.println("Error in matching between B1 and R2");
                    }
                    if (!isMatchBetweenB1AndR2(first.charAt(2), second.charAt(2))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * By Hongbo
     * check whether orietation of Ball 1 and Ring 2 matches with each other or not
     * @param b1 the orientation of ball1
     * @param r2 the orientation of ring2
     * @return
     */
    private static boolean isMatchBetweenB1AndR2(char b1, char r2) {
        boolean result = false;
        boolean condition1 = ((r2 == 'A') && ((b1 == 'B') || (b1 == 'C')));
        boolean condition2 = ((r2 == 'B') && ((b1 == 'C') || (b1 == 'D')));
        boolean condition3 = ((r2 == 'C') && ((b1 == 'D') || (b1 == 'E')));
        boolean condition4 = ((r2 == 'D') && ((b1 == 'E') || (b1 == 'F')));
        boolean condition5 = ((r2 == 'E') && ((b1 == 'F') || (b1 == 'A')));
        boolean condition6 = ((r2 == 'F') && ((b1 == 'A') || (b1 == 'B')));
        result = condition1 || condition2 || condition3 || condition4 || condition5 || condition6;
        return result;
    }

    /**
     * By Hongbo
     * "placement" is already a valid placement (but its length is less than 3*12),
     * and then we add a piece placement "newPiece" to placement.
     * check whether the "placement"+"newPiece" is valid or not
     * @param placement it is already a valid placement (but its length is less than 3*12)
     * @param newPiece the new piece placement "newPiece" to be added at the end of "placement"
     * @return true: valid; false: invalid
     */
    public static boolean isPlacementValid(String placement, String newPiece) {
        callIsPlacementValid++;
        if ( placement.equals("") && newPiece.equals("")) return true;
        if (!isPlacementWellFormed(placement+newPiece)) return false;
        if ( placement.length() == 0 ) occupationArray.clear();
        if ( outOfPegSinglePiece(newPiece) ) return false;
        setOccupationArray(placement, newPiece);
        return checkOccupationArraySinglePiece();
    }

    /**
     * By Hongbo
     * check whether the given piece is out of peg or not
     * @param newPiece single piece placement String
     * @return true if some unit is out of peg, false if none unit is out of peg
     */
    private static boolean outOfPegSinglePiece(String newPiece) {
        pegIndex = getPegsForPiecePlacement(newPiece);
        for (int j = 0; j < 3; j++) {
            if (pegIndex[j] == -1) return true;
        }
        return false;
    }

    /**
     * By Hongbo
     * we already set the occupationArray based on "placement",
     * and then we add a new piece placement "newPiece" to the "placement".
     * in this case, occupationArray is to be updated by this method
     * @param placement it is already a valid placement (but its length is less than 36)
     * @param newPiece the new piece placement "newPiece" to be added at the end of "placement"
     */
    private  static void setOccupationArray(String placement, String newPiece) {
        PiecePlacement newPiecePlacement = new PiecePlacement(newPiece);
        newPiecePlacement.translateorientation(newPiecePlacement.piece, newPiecePlacement.ori);
        for (int j = 0; j < 3; j++) {
            if (!occupationArray.containsKey(pegIndex[j])) {
                ArrayList<String> newUandOArray = new ArrayList<>();
                newUandOArray.add(newPiecePlacement.piece.uandos[j].toString());
                occupationArray.put(pegIndex[j], newUandOArray);
            } else {
                occupationArray.get(pegIndex[j]).add(newPiecePlacement.piece.uandos[j].toString());
            }
        }
    }

    /**
     * By Hongbo
     * only check the new added piece placement is valid or not.
     * only check the pegIndex, which record the pegs occupied by the last piece placement in occupationArray.
     * a peg cannot place more than two units
     * a peg cannot place two balls or two rings
     * if a peg is placed with a ball and a ring, they must satisfy: ball1~ring1, bing2~ring2, bing1~ring2
     * the ball and the ring on the same peg must match each other
     * @return true if valid, false if invalid
     */
    private static boolean checkOccupationArraySinglePiece() {
        for (int i = 0; i < 3; i++) {

            Integer keys = pegIndex[i];

            // test whether a peg is occupied by more than two units
            if (occupationArray.get(keys).size() > 2) return false;

            // test whether a peg is occupied by two balls or two rings
            if (occupationArray.get(keys).size() == 2) {
                //if ( occupationArray.get(keys).get(0).unit.type == occupationArray.get(keys).get(1).unit.type ) {
                if (occupationArray.get(keys).get(0).charAt(0) == occupationArray.get(keys).get(1).charAt(0)) {
                    return false;
                }
            }

            // test ball1~ring1, bing2~ring2, bing1~ring2
            if (occupationArray.get(keys).size() == 2) {
                if (occupationArray.get(keys).get(0).charAt(1) != occupationArray.get(keys).get(1).charAt(1)) {
                    String first = occupationArray.get(keys).get(0).substring(0, 2);
                    String second = occupationArray.get(keys).get(1).substring(0, 2);
                    boolean condition1 = first.equals("B1") && second.equals("R2");
                    boolean condition2 = first.equals("R2") && second.equals("B1");
                    boolean condition = condition1 || condition2;
                    if (!condition) {
                        return false;
                    }
                }
            }

            // test whether a peg is occupied by ball1 and ring1 with matching direction
            if (occupationArray.get(keys).size() == 2) {
                if (occupationArray.get(keys).get(0).charAt(1) == occupationArray.get(keys).get(1).charAt(1)) {
                    //if ( occupationArray.get(keys).get(0).orientation != occupationArray.get(keys).get(1).orientation ) {
                    if (occupationArray.get(keys).get(0).charAt(2) != occupationArray.get(keys).get(1).charAt(2)) {
                        return false;
                    }
                }
            }

            // test whether a peg is occupied by ball1 and ring2 with matching direction
            if (occupationArray.get(keys).size() == 2) {
                if (occupationArray.get(keys).get(0).charAt(1) != occupationArray.get(keys).get(1).charAt(1)) {
                    String first = "";
                    String second = "";
                    if (occupationArray.get(keys).get(0).substring(0, 2).equals("B1") && occupationArray.get(keys).get(1).substring(0, 2).equals("R2")) {
                        first = occupationArray.get(keys).get(0);
                        second = occupationArray.get(keys).get(1);
                    } else if (occupationArray.get(keys).get(0).substring(0, 2).equals("R2") && occupationArray.get(keys).get(1).substring(0, 2).equals("B1")) {
                        first = occupationArray.get(keys).get(1);
                        second = occupationArray.get(keys).get(0);
                    } else {
                        System.out.println("Error in matching between B1 and R2");
                    }
                    if (!isMatchBetweenB1AndR2(first.charAt(2), second.charAt(2))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * By Hongbo
     * "placement" is already a valid placement (but its length is less than 3*12),
     * and then we add a piece placement "newPiece" to placement.
     * check whether the "placement"+"newPiece" is valid or not
     * does not use the static occupationArray. occupy records the peg occupied status of String placement.
     * @param placement it is already a valid placement (but its length is less than 3*12)
     * @param newPiece the new piece placement "newPiece" to be added at the end of "placement"
     * @return true: valid; false: invalid
     */
    public static boolean isPlacementValid(String placement, String newPiece, HashMap<Integer, ArrayList<String>> occupy) {
        if (!isPlacementWellFormed(placement+newPiece)) return false;
        if ( outOfPegSinglePiece(newPiece) ) return false;
        setOccupationArray(placement, newPiece, occupy);
        return checkOccupationArraySinglePiece(occupy);
    }


    /**
     * By Hongbo
     * we already set the occupy based on "placement",
     * and then we add a new piece placement "newPiece" to the "placement".
     * in this case, occupy is to be updated by this method
     * does not use the static occupationArray.
     * occupy records the peg occupied status of String placement. it will be update to record the peg occupied status of String "placement+newPiece" in this method
     * @param placement it is already a valid placement (but its length is less than 36)
     * @param newPiece the new piece placement "newPiece" to be added at the end of "placement"
     */
    private  static void setOccupationArray(String placement, String newPiece, HashMap<Integer, ArrayList<String>> occupy) {
        PiecePlacement newPiecePlacement = new PiecePlacement(newPiece);
        newPiecePlacement.translateorientation(newPiecePlacement.piece, newPiecePlacement.ori);
        for (int j = 0; j < 3; j++) {
            if (!occupy.containsKey(pegIndex[j])) {
                ArrayList<String> newUandOArray = new ArrayList<>();
                newUandOArray.add(newPiecePlacement.piece.uandos[j].toString());
                occupy.put(pegIndex[j], newUandOArray);
            } else {
                occupy.get(pegIndex[j]).add(newPiecePlacement.piece.uandos[j].toString());
            }
        }
    }

    /**
     * By Hongbo
     * occupy records the peg occupied status of String placement.
     * only check the new added piece placement is valid or not.
     * only check the pegIndex, which record the pegs occupied by the last piece placement in occupy.
     * a peg cannot place more than two units
     * a peg cannot place two balls or two rings
     * if a peg is placed with a ball and a ring, they must satisfy: ball1~ring1, bing2~ring2, bing1~ring2
     * the ball and the ring on the same peg must match each other
     * @return true if valid, false if invalid
     */
    private static boolean checkOccupationArraySinglePiece(HashMap<Integer, ArrayList<String>> occupy) {
        for (int i = 0; i < 3; i++) {

            Integer keys = pegIndex[i];

            // test whether a peg is occupied by more than two units
            if (occupy.get(keys).size() > 2) return false;

            // test whether a peg is occupied by two balls or two rings
            if (occupy.get(keys).size() == 2) {
                if (occupy.get(keys).get(0).charAt(0) == occupy.get(keys).get(1).charAt(0)) {
                    return false;
                }
            }

            // test ball1~ring1, bing2~ring2, bing1~ring2
            if (occupy.get(keys).size() == 2) {
                if (occupy.get(keys).get(0).charAt(1) != occupy.get(keys).get(1).charAt(1)) {
                    String first = occupy.get(keys).get(0).substring(0, 2);
                    String second = occupy.get(keys).get(1).substring(0, 2);
                    boolean condition1 = first.equals("B1") && second.equals("R2");
                    boolean condition2 = first.equals("R2") && second.equals("B1");
                    boolean condition = condition1 || condition2;
                    if (!condition) {
                        return false;
                    }
                }
            }

            // test whether a peg is occupied by ball1 and ring1 with matching direction
            if (occupy.get(keys).size() == 2) {
                if (occupy.get(keys).get(0).charAt(1) == occupy.get(keys).get(1).charAt(1)) {
                    if (occupy.get(keys).get(0).charAt(2) != occupy.get(keys).get(1).charAt(2)) {
                        return false;
                    }
                }
            }

            // test whether a peg is occupied by ball1 and ring2 with matching direction
            if (occupy.get(keys).size() == 2) {
                if (occupy.get(keys).get(0).charAt(1) != occupy.get(keys).get(1).charAt(1)) {
                    String first = "";
                    String second = "";
                    if (occupy.get(keys).get(0).substring(0, 2).equals("B1") && occupy.get(keys).get(1).substring(0, 2).equals("R2")) {
                        first = occupy.get(keys).get(0);
                        second = occupy.get(keys).get(1);
                    } else if (occupy.get(keys).get(0).substring(0, 2).equals("R2") && occupy.get(keys).get(1).substring(0, 2).equals("B1")) {
                        first = occupy.get(keys).get(1);
                        second = occupy.get(keys).get(0);
                    } else {
                        System.out.println("Error in matching between B1 and R2");
                    }
                    if (!isMatchBetweenB1AndR2(first.charAt(2), second.charAt(2))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * By Shijie and Hongbo
     *
     * Return an array of all solutions given a starting placement.
     * TODO upgrade the algorithm: record the last one, use the upgraded methods
     * @param placement A valid piece placement string.
     * @return An array of strings, each describing a solution to the game given the
     * starting point provied by placement.
     */
    public static String[] getSolutions(String placement){
        // FIXME Task 10: determine all solutions to the game, given a particular starting placement
        ArrayList<ArrayList> solutions = new ArrayList<>();
        ArrayList outarray = new ArrayList();
        String validpiece = getvalidpiece(placement);
        String startorigin ="ABCDEFGHIJKLMNOPQRSTUVWX";
        // if ( !isPlacementValid(placement) ) System.out.println("invalid starting piece"); //*
        String validorigin = startorigin; // = getvalidorigin(startorigin, placement); // *
        String orientation = "ABCDEFGHIJKL";

        ArrayList<String> start = new ArrayList<>();
        start.add(placement);
        solutions.add(start);

        ArrayList<ArrayList<HashMap<Integer,ArrayList<String>>>> lastOccupationArrayList = new ArrayList<>(); // * record last occupationArray
        ArrayList<ArrayList<String>> lastValidOriginArray = new ArrayList<>(); // * record the last validOrigin
        ArrayList<String> startValidOrigin = new ArrayList<>(); // *

        callIsPlacementValid = 0;

        int count = 0;

        if (placement.length() != 36 && isPlacementValid(placement)) {

            ArrayList<HashMap<Integer, ArrayList<String>>> startOccupationArray = new ArrayList<>(); // * By Hongbo Oct 1st
            HashMap<Integer, ArrayList<String>> startTmp; // *
            startTmp = deepCopy(occupationArray); // *
            startOccupationArray.add(startTmp); // *
            lastOccupationArrayList.add(startOccupationArray); // *

            validorigin = getvalidorigin(startorigin,true,'A'); // * any value of the third argument is OK
            startValidOrigin.add(validorigin); // *
            lastValidOriginArray.add(startValidOrigin); // *

            //System.out.println("startTmp: "+lastOccupationArrayList.get(0).get(0)); // *
            while (count < validpiece.length()) { // the count-th piece
                //System.out.println(validpiece.length()-count); // *
                ArrayList<String> validplacement = new ArrayList<>();
                ArrayList<HashMap<Integer,ArrayList<String>>> tmpOccuptionArray = new ArrayList<>(); // *
                ArrayList<String> tmpValidOrigin = new ArrayList<>();
/*
                if ( count == 3 ) {
                    outputFourPieces(solutions.get(0));
                    break;
                }
*/
                //System.out.println(count);
                for (int i = 0; i < solutions.get(0).size(); i++) { // the ith solution
                    occupationArray = deepCopy(lastOccupationArrayList.get(0).get(i)); // *
                    //System.out.println(occupationArray); // *
                    //validorigin = getvalidorigin(validorigin, solutions.get(0).get(i)+""); // *
                    //System.out.println(solutions.get(0).get(i)); // *
                    validorigin = lastValidOriginArray.get(0).get(i); // *
                    validorigin = validOriginForThisPiece(validorigin,validpiece.charAt(count)); //**
                    //System.out.println(validorigin); // *
                    for (int j = 0; j < validorigin.length(); j++) { // the jth origin
                        orientation = getValidOrientation("ABCDEFGHIJKL",validpiece.charAt(count),validorigin.charAt(j));
                        for (int k = 0; k < orientation.length(); k++) { // the kth orientation
                            String subplacecment = "" + solutions.get(0).get(i) + validorigin.charAt(j) + validpiece.charAt(count) + orientation.charAt(k);
                            String thisPlacement = "" + solutions.get(0).get(i); // *
                            String newOne = "" + validorigin.charAt(j) + validpiece.charAt(count) + orientation.charAt(k); // *
                            //System.out.println("startTmp: "+lastOccupationArrayList.get(0).get(0)); // *
                            //System.out.println("before: " + occupationArray); // *
                            //System.out.println(thisPlacement+", "+newOne); // *
                            //System.out.println("length of placcement: " + subplacecment.length() + ", checkplacement: " + subplacecment +", is valid: " + isPlacementValid(subplacecment));
                            //if (isPlacementValid(subplacecment)) {
                            if (isPlacementValid(thisPlacement,newOne)) { // *
                                //System.out.println(thisPlacement+", "+newOne+", true"); // *
                                if (subplacecment.length() == 36) {
                                    //System.out.println("/////validplacement/////" + subplacecment);
                                    outarray.add(subplacecment);
                                    //System.out.println(subplacecment);
                                } else {
                                    validplacement.add(subplacecment);
                                    tmpOccuptionArray.add( deepCopy(occupationArray) ); // *
                                    //System.out.println(tmpOccuptionArray); // *
                                    tmpValidOrigin.add(getvalidorigin(lastValidOriginArray.get(0).get(i), false,validpiece.charAt(count))); // *
                                    //System.out.println("tmp: "+validorigin); // *
                                }
                            }  // else { // *
                               // System.out.println(thisPlacement+", "+newOne+", false"); // *
                            // } // *
                            //System.out.println("after : " + occupationArray); // *
                            //occupationArray = deepCopy(lastOccupationArrayList.get(0).get(i)); // * not used anymore
                            boolean isOutofPeg = false;
                            for (int jj = 0; jj < 3; jj++) {
                                if (pegIndex[jj] == -1) isOutofPeg = true;
                            }
                            if ( ! isOutofPeg ) {
                                restoreOccupationArray();
                            }
                        }
                    }
                }

                //System.out.println(validplacement);
                solutions.add(validplacement);
                solutions.remove(solutions.get(0));
                lastOccupationArrayList.add(tmpOccuptionArray); // *
                lastOccupationArrayList.remove(lastOccupationArrayList.get(0)); // *
                lastValidOriginArray.add(tmpValidOrigin); // *
                lastValidOriginArray.remove(lastValidOriginArray.get(0)); // *
                count++;
            }

            ////System.out.println("call isPlacementValid(String,String): "+ callIsPlacementValid+" times.");

            //System.out.println("validorigin are: " + validorigin + ", and length is: " + validorigin.length());
            //System.out.println(occupationArray);
            //System.out.println("validpiece are: " + validpiece + ", count number is: " + count);
            ////System.out.println("For this placement: " + placement + " this is outarray: " + outarray);
            ////System.out.println("------------------------------------------------------------");

            int l = outarray.size();
/*
            if ( l == 1 ) {
                System.out.println("call isPlacementValid(String,String): "+ callIsPlacementValid+" times.");
                System.out.println("For this placement: " + placement + " this is outarray: " + outarray);
                System.out.println("------------------------------------------------------------");
            }
*/
            if (l > 0) {
                String[] output = new String[outarray.size()];
                for (int i = 0; i < outarray.size(); i++) {
                    output[i] = outarray.get(i) + "";
                }
                return output;
            } else {
                return null;
            }

        } else {
            if (isPlacementValid(placement)) {
                String[] output = {placement};
                return output;
            } else {
                return null;
            }
        }
        //return output;
    }

    private static void outputFourPieces(ArrayList<String> fourSolution ) {
        try {
            String Directory = System.getProperty("user.dir");
            String outfilename = "JAC_FourPieces_Sols" + ".txt";
            File outfile = new File(Directory + "/src/comp1110/ass2/SolutionDictionary", outfilename);

            PrintWriter writer = new PrintWriter(outfile);

            for (int i = 0; i < fourSolution.size(); i++) {
                writer.println(fourSolution.get(i));
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("open file error");
        }

    }

    /**
     * By Shijie
     * improved by Shijie
     * 1: check only the pegs in occupationArray, instead of checking all of 24 pegs every time.
     * debug by Hongbo
     * 1: occupationArray.get(origin - 'A').get(0).equals("R0null")
     * 2: validorigin = validorigin.replace(origin + "", ""); and for (int i = 0; i < validorigin.length(); i++) are not consist
     * improved by Hongbo
     * 1: only update static array, pegIndex[3], if getvalidorigin is not run for the first time
     * 2: check deadPeg ( pegIndex[3], and their neighbors)
     * 3: check the peg facing single occupied unit R1 is valid peg or not
     * 4: check the matching between the shape of piece and availability of neighbor pegs
     * @param validorigin the last validorigin array, contains all the valid origin before this operation.
     * @param isFirst if true, check all the occupationArray; if false, only check pegIndex
     * @param piece the shape of piece is relevant in this method
     * @return obtain all the pegs on which can place a piece
     */
    static String getvalidorigin(String validorigin, boolean isFirst, char piece) {
    //static String getvalidorigin(String validorigin, String placement) {
        /*
        String validorigin = "ABCDEFGHIJKLMNOPQRSTUVWX";
        if (isPlacementValid(placement)) {
            if (!occupationArray.isEmpty()) {
                for (int i = 0; i < 24; i++){
                    if (occupationArray.get(i) != null){
                        if (occupationArray.get(i).size() > 1 || occupationArray.get(i).get(0).equals("R0null")){
                            char origin = (char) (i + 'A');
                            validorigin = validorigin.replace(origin+"","");
                        }
                    }
                }
            }
        }
        */
        //System.out.println(occupationArray); // *
        String tmpValidOrigin = new String(validorigin); // *
        //System.out.println("before: "+tmpValidOrigin); // *
        if ( isFirst ) {
            if (!occupationArray.isEmpty()) {
                for (int i = 0; i < validorigin.length(); i++) {
                    if (occupationArray.get(validorigin.charAt(i) - 'A') != null) {
                        // check valid peg or not
                        char origin = validorigin.charAt(i);
                        if (occupationArray.get(origin - 'A').size() > 1 || occupationArray.get(origin - 'A').get(0).equals("R0null")) { // *
                            //System.out.println(occupationArray.get(origin - 'A').get(0)); // *
                            //validorigin = validorigin.replace(origin + "", ""); // *
                            tmpValidOrigin = tmpValidOrigin.replace(origin + "", ""); // *
                        }
                        // check dead peg or not ( include check its neighbors)
                        if ( isDeadPeg(origin-'A')) tmpValidOrigin = tmpValidOrigin.replace(origin + "", ""); // *
                        int[] neighbors = getNeighbors(origin-'A'); // *
                        for ( int j = 0; j < 6; j++) { // *
                            if ( (neighbors[j] != -1) && tmpValidOrigin.contains(""+(char)(neighbors[j]+'A')) ) { // *
                                if( isDeadPeg(neighbors[j]) ) tmpValidOrigin = tmpValidOrigin.replace((char)(neighbors[j]+'A') + "", ""); // *
                            }
                        }
                        // check whether the peg facing R1 is valid or not
                        if ( occupationArray.get(origin-'A').size() == 1 ) {
                            if ( occupationArray.get(origin-'A').get(0).substring(0,2).equals("R1") ) {
                                if ( ! pegFaceRing1(origin - 'A', occupationArray.get(origin - 'A').get(0).charAt(2)) ) {
                                    tmpValidOrigin = tmpValidOrigin.replace(origin + "", "");
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (!occupationArray.isEmpty()) {  // * only check pegIndex
                for (int i = 0; i < 3; i++) { // *
                    int tmp = pegIndex[i];
                    // check valid peg or not
                    char origin = (char) (tmp + 'A'); // *
                    if (occupationArray.get(tmp).size() > 1 || occupationArray.get(tmp).get(0).equals("R0null")) { // *
                        tmpValidOrigin = tmpValidOrigin.replace(origin + "", ""); // *
                    }
                    // check dead peg or not ( include check its neighbors )
                    int[] neighbors = getNeighbors(origin-'A'); // *
                    //if ( isDeadPeg(tmp,neighbors) ) tmpValidOrigin = tmpValidOrigin.replace(origin + "", ""); // *
                    if ( isDeadPeg(tmp) ) tmpValidOrigin = tmpValidOrigin.replace(origin + "", ""); // *
                    for ( int j = 0; j < 6; j++) { // *
                        if ( (neighbors[j] != -1) && tmpValidOrigin.contains(""+(char)(neighbors[j]+'A')) ) { // *
                            if( isDeadPeg(neighbors[j]) ) tmpValidOrigin = tmpValidOrigin.replace((char)(neighbors[j]+'A') + "", ""); // *
                        }
                    }
                    // check whether the peg facing R1 is valid or not
                    if ( occupationArray.get(tmp).size() == 1 ) {
                        if ( occupationArray.get(tmp).get(0).substring(0,2).equals("R1") ) {
                            //if ( ! pegFaceRing1(tmp, occupationArray.get(tmp).get(0).charAt(2), neighbors) ) {
                            if ( ! pegFaceRing1(tmp, occupationArray.get(tmp).get(0).charAt(2)) ) {
                                tmpValidOrigin = tmpValidOrigin.replace(origin + "", "");
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("after: "+tmpValidOrigin); // *
        //return validorigin; // *
        return tmpValidOrigin; // *
    }

    /**
     * By Hongbo
     * considering the nature of center unit of a given piece to prune by removing invalid peg,
     * e.g. piece B,C,D,E,F,H 's center is R0, there should be nothing on that peg,
     * piece G,K,L 's center is R1, there should be nothing or B1 on that peg
     * piece I,J 's center is B2, there should be nothing or R2 on that peg
     * piece A 's center is R2, there should be nothing or B2,B1 on that peg
     * @param validorigin
     * @param piece
     * @return
     */
    static String validOriginForThisPiece(String validorigin, char piece) {
        String tmpValidOrigin = new String(validorigin); // *
        if (!occupationArray.isEmpty()) {
            for (int i = 0; i < validorigin.length(); i++) {
                if (occupationArray.get(validorigin.charAt(i) - 'A') != null) {
                    char origin = validorigin.charAt(i);
                    int[] neighbors = getNeighbors(origin-'A');
                    // check the matching between piece shape and neighbor pegs
                    if ( ! pegNeigbhorAndPieceShape(origin-'A',piece) ) tmpValidOrigin = tmpValidOrigin.replace(origin + "", "");
                    // check the details of the peg. e.g. piece B,C,D,E,F,H 's center is R0, there should be nothing on that peg,
                    // piece G,K,L 's center is R1, there should be nothing or B1 on that peg
                    // piece I,J 's center is B2, there should be nothing or R2 on that peg
                    // piece A 's center is R2, there should be nothing or B2,B1 on that peg
                    if ( "BCDEFH".contains(piece+"") ) {
                        tmpValidOrigin = tmpValidOrigin.replace(origin + "", "");
                    } else if ( "GKL".contains(piece+"") ) {
                        if ( ! occupationArray.get(origin-'A').get(0).substring(0,2).equals("B1") ) {
                            tmpValidOrigin = tmpValidOrigin.replace(origin + "", "");
                        }
                    } else if ( "IJ".contains(piece+"") ) {
                        if ( ! occupationArray.get(origin-'A').get(0).substring(0,2).equals("R2") ) {
                            tmpValidOrigin = tmpValidOrigin.replace(origin + "", "");
                        }
                    } else if ( "A".contains(piece+"") ) {
                        if ( ! occupationArray.get(origin-'A').get(0).substring(0,1).equals("B") ) {
                            tmpValidOrigin = tmpValidOrigin.replace(origin + "", "");
                        }
                    }
                }
            }
        }
        return tmpValidOrigin; // *
    }

    /**
     * By Shijie
     * @param placement placemenet string
     * @return the pieces which are not in the placement
     */
    static String getvalidpiece(String placement){
        String validpiece = "ABCDEFGHIJKL";
        String unvalidpiece = "";

        for (int i = 1; i < placement.length(); i = i + 3){
            unvalidpiece = unvalidpiece + placement.charAt(i);
        }

        for (int i = 0; i < unvalidpiece.length(); i++){
            if (validpiece.contains(unvalidpiece.charAt(i)+"")){
                validpiece = validpiece.replace(unvalidpiece.charAt(i)+"", "");
            }
        }
        return validpiece;
    }

    /**
     * By Hongbo
     * @param in input HashMap<Integer, ArrayList<String>> object to be copy deeply
     * @return a new object
     */
    public static HashMap<Integer, ArrayList<String>> deepCopy(HashMap<Integer, ArrayList<String>> in) {
        //long startTime = System.nanoTime();
        HashMap<Integer, ArrayList<String>> out = new HashMap<>();
        for ( Integer keys : in.keySet() ) {
            ArrayList<String> inValue = in.get(keys);
            ArrayList<String> tmp = new ArrayList<>();
            for ( int i = 0; i < inValue.size(); i++ ) {
                tmp.add(inValue.get(i));
            }
            out.put(keys,tmp);
        }
        //long endTime = System.nanoTime();
        //totalDeepCopyTime = totalDeepCopyTime + endTime - startTime;
        //System.out.println(totalDeepCopyTime);
        return out;
    }

    /**
     * By Hongbo
     * get all the neighbors of a give peg
     *
     *   rt[0]  rt[1]
     * rt[5]  peg  rt[2]
     *   rt[4]  rt[3]
     *
     * @param peg which peg
     * @return all the neighbors of peg
     */
    private static int[] getNeighbors(int peg) {
        int[] neig = {-1,-1,-1,-1,-1,-1};
        if ( peg == 0 ) {
            neig[2] = 1;
            neig[3] = 6;
        } else if ( peg > 0 && peg < 5 ) {
            neig[5] = peg - 1;
            neig[4] = peg + 5;
            neig[3] = peg + 6;
            neig[2] = peg + 1;
        } else if ( peg == 5 ) {
            neig[5] = 4; //peg - 1;
            neig[4] = 10; //peg + 5;
            neig[3] = 11; //peg + 6;
        } else if ( peg == 6 ) {
            neig[0] = 0; //peg - 6;
            neig[1] = 1; //peg - 5;
            neig[2] = 7; //peg + 1;
            neig[3] = 13; //peg + 7;
            neig[4] = 12; //peg + 6;
        } else if ( peg > 6 && peg < 11) {
            neig[0] = peg - 6;
            neig[1] = peg - 5;
            neig[2] = peg + 1;
            neig[3] = peg + 7;
            neig[4] = peg + 6;
            neig[5] = peg - 1;
        } else if ( peg == 11) {
            neig[0] = 5; //peg - 6;
            neig[4] = 17; //peg + 6;
            neig[5] = 10; //peg - 1;
        } else if ( peg == 12) {
            neig[1] = 6; //peg - 6;
            neig[2] = 13; //peg + 1;
            neig[3] = 18; //peg + 6;
        } else if ( peg > 12 && peg < 17) {
            neig[0] = peg - 7;
            neig[1] = peg - 6;
            neig[2] = peg + 1;
            neig[3] = peg + 6;
            neig[4] = peg + 5;
            neig[5] = peg - 1;
        } else if ( peg == 17) {
            neig[0] = 10; //peg - 7;
            neig[1] = 11; //peg - 6;
            neig[3] = 23; //peg + 6;
            neig[4] = 22; //peg + 5;
            neig[5] = 16; //peg - 1;
        } else if ( peg == 18) {
            neig[0] = 12; //peg - 6;
            neig[1] = 13; //peg - 5;
            neig[2] = 19; //peg + 1;
        } else if ( peg > 18 && peg < 23) {
            neig[0] = peg - 6;
            neig[1] = peg - 5;
            neig[2] = peg + 1;
            neig[5] = peg - 1;
        } else if ( peg == 23 ) {
            neig[0] = 17; //peg - 6;
            neig[5] = 22; //peg - 1;
        } else {
            System.out.println("wrong peg");
        }
        return neig;
    }

    /**
     * By Hongbo
     * for any given peg, it has at most 6 neighbors.
     * Among its neighbors pegs, if the number of pegs which can be placed with another unit is less than 2, we call the give peg dead peg.
     * Because it can't place any piece on this given peg.
     * @param peg the peg to be checked
     * @return true, if dead peg; false, if not dead peg
     */
    private static boolean isDeadPeg(int peg) {
        int points;
        int[] neighbor = getNeighbors(peg);
        points = 0;
        for ( int i = 0; i < 6; i++) {
            if ( neighbor[i] != -1 ) {
                if (occupationArray.get(neighbor[i]) == null) {
                    points = points + 1;
                } else {
                    if (!(occupationArray.get(neighbor[i]).size() > 1 || occupationArray.get(neighbor[i]).get(0).equals("R0null"))) {
                        points = points + 1;
                    }
                }
            }
        }
        return (points < 2);
    }

    /**
     * By Hongbo
     * for any given peg, it has at most 6 neighbors.
     * Among its neighbors pegs, if the number of pegs which can be placed with another unit is less than 2, we call the give peg dead peg.
     * Because it can't place any piece on this given peg.
     * @param peg the peg to be checked
     * @param neighbor the neighbors of peg
     * @return true, if dead peg; false, if not dead peg
     */
    private static boolean isDeadPeg(int peg, int[] neighbor) {
        int points;
        //int[] neighbor = getNeighbors(peg);
        points = 0;
        for ( int i = 0; i < 6; i++) {
            if ( neighbor[i] != -1 ) {
                if (occupationArray.get(neighbor[i]) == null) {
                    points = points + 1;
                } else {
                    if (!(occupationArray.get(neighbor[i]).size() > 1 || occupationArray.get(neighbor[i]).get(0).equals("R0null"))) {
                        points = points + 1;
                    }
                }
            }
        }
        return (points < 2);
    }

    /**
     * By Hongbo
     * @param peg on which peg the Ring1 is
     * @param ori the ori of Ring1
     * @return true, if the peg facing opening of R1 is a valid peg; false, if not
     */
    private static boolean pegFaceRing1(int peg, char ori) {
        int[] neighbors = getNeighbors(peg);
        int facedPeg;
        if ( ori == 'A') {
            facedPeg = neighbors[5];
        } else {
            facedPeg = neighbors[ori-'B'];
        }
        if ( facedPeg == -1 ) return false;
        if ( occupationArray.get(facedPeg) == null ) return true;
        if ( occupationArray.get(facedPeg).size() > 1 || occupationArray.get(facedPeg).get(0).equals("R0null") ) return false;
        return true;
    }

    /**
     * By Hongbo
     * @param peg on which peg the Ring1 is
     * @param ori the ori of Ring1
     * @param neighbors the neighbors of peg
     * @return true, if the peg facing opening of R1 is a valid peg; false, if not
     */
    private static boolean pegFaceRing1(int peg, char ori, int[] neighbors) {
        //int[] neighbors = getNeighbors(peg);
        int facedPeg;
        if ( ori == 'A') {
            facedPeg = neighbors[5];
        } else {
            facedPeg = neighbors[ori-'B'];
        }
        if ( facedPeg == -1 ) return false;
        if ( occupationArray.get(facedPeg) == null ) return true;
        if ( occupationArray.get(facedPeg).size() > 1 || occupationArray.get(facedPeg).get(0).equals("R0null") ) return false;
        return true;
    }

    /**
     * determine whether a given peg is valid or not by checking whether
     * the shape of piece and the valid pegs in the neighbors of a given peg matches or not.
     * @param peg peg index
     * @param piece the shape of the piece is relevant
     * @return true, if match; false, if not match
     */
    private static boolean pegNeigbhorAndPieceShape(int peg, char piece) {
        int shape = -1; // 1 for line shape; 2 for obtuse shape; 3 for acute shape
        if ( "ABC".contains(piece+"") ) {
            shape = 1;
        } else if ( "DEFGH".contains(piece+"") ) {
            shape = 2;
        } else if ( "IJKL".contains(piece+"") ) {
            shape = 3;
        }
        int[] neighbor = getNeighbors(peg);
        boolean[] neighborValid = {false,false,false,false,false,false};
        for ( int i = 0; i < 6; i++) {
            if ( neighbor[i] != -1 ) {
                if (occupationArray.get(neighbor[i]) == null) {
                    neighborValid[i] = true;
                } else {
                    if (!(occupationArray.get(neighbor[i]).size() > 1 || occupationArray.get(neighbor[i]).get(0).equals("R0null"))) {
                        neighborValid[i] = true;
                    }
                }
            }
        }
        boolean condition1, condition2, condition3, condition4, condition5, condition6;
        if ( shape == 1) {
            condition1 = neighborValid[0] && neighborValid[3];
            condition2 = neighborValid[1] && neighborValid[4];
            condition3 = neighborValid[2] && neighborValid[5];
            if ( condition1 || condition2 || condition3 ) return true;
        } else if ( shape == 2 ) {
            condition1 = neighborValid[0] && neighborValid[2];
            condition2 = neighborValid[1] && neighborValid[3];
            condition3 = neighborValid[2] && neighborValid[4];
            condition4 = neighborValid[3] && neighborValid[5];
            condition5 = neighborValid[4] && neighborValid[0];
            condition6 = neighborValid[5] && neighborValid[1];
            if ( condition1 || condition2 || condition3 || condition4 || condition5 || condition6 ) return true;
        } else if ( shape == 3 ) {
            condition1 = neighborValid[0] && neighborValid[1];
            condition2 = neighborValid[1] && neighborValid[2];
            condition3 = neighborValid[2] && neighborValid[3];
            condition4 = neighborValid[3] && neighborValid[4];
            condition5 = neighborValid[4] && neighborValid[5];
            condition6 = neighborValid[5] && neighborValid[0];
            if ( condition1 || condition2 || condition3 || condition4 || condition5 || condition6 ) return true;
        }
        return false;
    }

    /**
     * determine whether a given peg is valid or not by checking whether
     * the shape of piece and the valid pegs in the neighbors of a given peg matches or not.
     * @param peg peg index
     * @param piece the shape of the piece is relevant
     * @param neighbor the neighbors of the peg
     * @return true, if match; false, if not match
     */
    private static boolean pegNeigbhorAndPieceShape(int peg, char piece, int[] neighbor) {
        int shape = -1; // 1 for line shape; 2 for obtuse shape; 3 for acute shape
        if ( "ABC".contains(piece+"") ) {
            shape = 1;
        } else if ( "DEFGH".contains(piece+"") ) {
            shape = 2;
        } else if ( "IJKL".contains(piece+"") ) {
            shape = 3;
        }
        //int[] neighbor = getNeighbors(peg);
        boolean[] neighborValid = {false,false,false,false,false,false};
        for ( int i = 0; i < 6; i++) {
            if ( neighbor[i] != -1 ) {
                if (occupationArray.get(neighbor[i]) == null) {
                    neighborValid[i] = true;
                } else {
                    if (!(occupationArray.get(neighbor[i]).size() > 1 || occupationArray.get(neighbor[i]).get(0).equals("R0null"))) {
                        neighborValid[i] = true;
                    }
                }
            }
        }
        boolean condition1, condition2, condition3, condition4, condition5, condition6;
        if ( shape == 1) {
            condition1 = neighborValid[0] && neighborValid[3];
            condition2 = neighborValid[1] && neighborValid[4];
            condition3 = neighborValid[2] && neighborValid[5];
            if ( condition1 || condition2 || condition3 ) return true;
        } else if ( shape == 2 ) {
            condition1 = neighborValid[0] && neighborValid[2];
            condition2 = neighborValid[1] && neighborValid[3];
            condition3 = neighborValid[2] && neighborValid[4];
            condition4 = neighborValid[3] && neighborValid[5];
            condition5 = neighborValid[4] && neighborValid[0];
            condition6 = neighborValid[5] && neighborValid[1];
            if ( condition1 || condition2 || condition3 || condition4 || condition5 || condition6 ) return true;
        } else if ( shape == 3 ) {
            condition1 = neighborValid[0] && neighborValid[1];
            condition2 = neighborValid[1] && neighborValid[2];
            condition3 = neighborValid[2] && neighborValid[3];
            condition4 = neighborValid[3] && neighborValid[4];
            condition5 = neighborValid[4] && neighborValid[5];
            condition6 = neighborValid[5] && neighborValid[0];
            if ( condition1 || condition2 || condition3 || condition4 || condition5 || condition6 ) return true;
        }
        return false;
    }

    /**
     * By Hongbo
     * use the shape of piece and availability of neighbors to constraint the possible orientation of piece
     * @param startOrientation "A..L" length = 12
     * @param piece the shape of piece is relevant here
     * @param peg which peg, the neighbors are relevant here
     * @return
     */
    public static String getValidOrientation(String startOrientation, char piece, char peg) {
        String rt = startOrientation;
        int shape = -1; // 1 for line shape; 2 for obtuse shape; 3 for acute shape
        if ( "ABC".contains(piece+"") ) {
            shape = 1;
        } else if ( "DEFGH".contains(piece+"") ) {
            shape = 2;
        } else if ( "IJKL".contains(piece+"") ) {
            shape = 3;
        }

        if ( piece == 'A' ) rt = rt.replace("G","").replace("H","").replace("I","").replace("J","").replace("K","").replace("L","");

        int[] neighbor = getNeighbors(peg-'A');
        boolean[] neighborValid = {false,false,false,false,false,false};
        for ( int i = 0; i < 6; i++) {
            if ( neighbor[i] != -1 ) {
                if (occupationArray.get(neighbor[i]) == null) {
                    neighborValid[i] = true;
                } else {
                    if (!(occupationArray.get(neighbor[i]).size() > 1 || occupationArray.get(neighbor[i]).get(0).equals("R0null"))) {
                        neighborValid[i] = true;
                    }
                }
            }
        }
        //for ( int ii = 0; ii < 6; ii++) {
          //  System.out.print(neighborValid[ii]+" ");
        //}
        //System.out.print("\n");
        boolean condition1, condition2, condition3, condition4, condition5, condition6;
        if ( shape == 1) {
            condition1 = neighborValid[0] && neighborValid[3];
            condition2 = neighborValid[1] && neighborValid[4];
            condition3 = neighborValid[2] && neighborValid[5];
            if ( ! condition1 ) rt = rt.replace("B","").replace("E","").replace("H","").replace("K","");
            if ( ! condition2 ) rt = rt.replace("C","").replace("F","").replace("I","").replace("L","");
            if ( ! condition3 ) rt = rt.replace("A","").replace("D","").replace("G","").replace("J","");
        } else if ( shape == 2 ) {
            condition1 = neighborValid[0] && neighborValid[2];
            condition2 = neighborValid[1] && neighborValid[3];
            condition3 = neighborValid[2] && neighborValid[4];
            condition4 = neighborValid[3] && neighborValid[5];
            condition5 = neighborValid[4] && neighborValid[0];
            condition6 = neighborValid[5] && neighborValid[1];
            if ( ! condition1 ) rt = rt.replace("B","").replace("J","");
            if ( ! condition2 ) rt = rt.replace("C","").replace("K","");
            if ( ! condition3 ) rt = rt.replace("D","").replace("L","");
            if ( ! condition4 ) rt = rt.replace("E","").replace("G","");
            if ( ! condition5 ) rt = rt.replace("F","").replace("H","");
            if ( ! condition6 ) rt = rt.replace("A","").replace("I","");
        } else if ( shape == 3 ) {
            condition1 = neighborValid[0] && neighborValid[1];
            condition2 = neighborValid[1] && neighborValid[2];
            condition3 = neighborValid[2] && neighborValid[3];
            condition4 = neighborValid[3] && neighborValid[4];
            condition5 = neighborValid[4] && neighborValid[5];
            condition6 = neighborValid[5] && neighborValid[0];
            if ( ! condition1 ) rt = rt.replace("B","").replace("I","");
            if ( ! condition2 ) rt = rt.replace("C","").replace("J","");
            if ( ! condition3 ) rt = rt.replace("D","").replace("K","");
            if ( ! condition4 ) rt = rt.replace("E","").replace("L","");
            if ( ! condition5 ) rt = rt.replace("F","").replace("G","");
            if ( ! condition6 ) rt = rt.replace("A","").replace("H","");
        }
        return rt;
    }

    /**
     * By Hongbo
     * avoid to deepCope a Hashmap in every iterations
     */
    private static void restoreOccupationArray() {
        int key;
        for (int j = 0; j < 3; j++) {
            key = pegIndex[j];
            int len = occupationArray.get(key).size();
            if ( len == 1) {
                occupationArray.remove(key);
            } else {
                occupationArray.get(key).remove(len - 1);
            }
        }
    }

    // for debug
    public static void main(String[] args) {
        //isPlacementValid("BAAHBATCJRDKWEBEFDNGLPHEDIFMJJQKIKLJ");
        //isPlacementValid("KAFUBAICCPDALEFEFEQGHSHBNIB");
        //System.out.println(isPlacementValid("KAFUBAICCPDALEFEFEQGHSHBNIB"));
        //isPlacementValid("JABHBCBCGGDFIEKVFAFGGSHBXIAJJJUKHKLK");
        //System.out.println(isPlacementValid("JABHBCBCGGDFIEKVFAFGGSHBXIAJJJUKHKLK"));
        //System.out.println(isPlacementValid("KAFUBAICCPDALEFEFEQGHSHBNIB","CJF"));
        //System.out.println(isPlacementValid("KAFCBGUCAGDFLEFPFBBGESHB","OIA"));
        //isPlacementValid("JABJJJ");
        //isPlacementValid("KAFUBAICC");
        //isPlacementValid("KAFUBA");
        //isPlacementValid("KAF");
        //isPlacementValid("KAFCBGUCAGDFLEFPFBBGESHBWIJKJAHKLJLH")
        //isPlacementValid("RDK");
        //isPlacementValid("RDKWEB");
        //isPlacementValid("KAFEFE");
        //getSolutions("JAC");
    }

}
