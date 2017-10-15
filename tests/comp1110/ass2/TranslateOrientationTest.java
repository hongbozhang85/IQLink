package comp1110.ass2;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by hongbo on 16-9-22.
 */
public class TranslateOrientationTest {

    // test the invalid piece
    @Test(expected = IllegalArgumentException.class)
    public void testWrongPiece() {
        PiecePlacement piecetry = new PiecePlacement("BZA");
        piecetry.translateorientation(piecetry.piece,piecetry.ori);
    }

    // test the invalid orietation: piece = A and orietation > F
    @Test(expected = IllegalArgumentException.class)
    public void testWrongOrientationPieceA() {
        PiecePlacement piecetry2 = new PiecePlacement("BAH");
        piecetry2.translateorientation(piecetry2.piece,piecetry2.ori);
    }


    // test the invalid orietation: orietation > L
    @Test(expected = IllegalArgumentException.class)
    public void testWrongOrientationOther() {
        PiecePlacement piecetry1 = new PiecePlacement("BBZ");
        piecetry1.translateorientation(piecetry1.piece,piecetry1.ori);
    }

    // test the orietation from A to F
    @Test
    public void testNonFlip() {
        String[] input = {"BAC", "BBC" ,"BCC", "BDC"};
        String[] output = { "C","E","D", "A" };
        ArrayList<PiecePlacement> piecetryArray = new ArrayList<>();
        boolean result = true;
        for ( int i = 0 ; i < input.length; i++) {
            piecetryArray.add(new PiecePlacement(input[i]));
            piecetryArray.get(i).translateorientation(piecetryArray.get(i).piece,piecetryArray.get(i).ori);
            System.out.println(piecetryArray.get(i).piece.uandos[2].orientation.toString());
            if ( piecetryArray.get(i).piece.uandos[2].orientation.toString() != output[i] ) result = false;
            assertTrue("the orientation of the 3rd unit of piece "+input[i]+" Expect: "+output[i]+", but got "+piecetryArray.get(i).piece.uandos[2].orientation.toString(),result);
        }
    }

    // test the orientation from G to L
    @Test
    public void testFlip() {
        String[] input = {"BIJ", "BJJ", "BKJ","BLJ"};
        String[] output = { "C","D", "F", "A"};
        ArrayList<PiecePlacement> piecetryArray = new ArrayList<>();
        boolean result = true;
        for ( int i = 0 ; i < input.length; i++) {
            piecetryArray.add(new PiecePlacement(input[i]));
            piecetryArray.get(i).translateorientation(piecetryArray.get(i).piece,piecetryArray.get(i).ori);
            System.out.println(piecetryArray.get(i).piece.uandos[2].orientation.toString());
            if ( piecetryArray.get(i).piece.uandos[2].orientation.toString() != output[i] ) result = false;
            assertTrue("the orientation of the 3rd unit of piece "+input[i]+" Expect: "+output[i]+", but got "+piecetryArray.get(i).piece.uandos[2].orientation.toString(),result);
        }
    }


    // test ring without opening
    @Test
    public void testNull() {
        PiecePlacement pieceNull = new PiecePlacement("BBC");
        boolean result = false;
        pieceNull.translateorientation(pieceNull.piece,pieceNull.ori);
        if (  pieceNull.piece.uandos[1].orientation == null ) result = true;
        assertTrue("expected null in the case of ring without opening",result);
    }

    // test duplicate. test "BBC" twice
    @Test
    public void testDuplicate() {
        String[] input = {"BBC" ,"BBC"};
        String[] output = { "E","E"};
        ArrayList<PiecePlacement> piecetryArray = new ArrayList<>();
        boolean result = true;
        for ( int i = 0 ; i < input.length; i++) {
            piecetryArray.add(new PiecePlacement(input[i]));
            piecetryArray.get(i).translateorientation(piecetryArray.get(i).piece,piecetryArray.get(i).ori);
            System.out.println(piecetryArray.get(i).piece.uandos[2].orientation.toString());
            if ( piecetryArray.get(i).piece.uandos[2].orientation.toString() != output[i] ) result = false;
            assertTrue("the orientation of the 3rd unit of piece "+input[i]+" Expect: "+output[i]+", but got "+piecetryArray.get(i).piece.uandos[2].orientation.toString(),result);
        }
    }
}
