package comp1110.ass2;



/**
 * Created by Shijie on 2016/9/14.
 */
public class PiecePlacement {
    char origin;
    Piece piece;
    Orientation orientation;
    String ori;
    char pie;


    /**
     * By Shijie
     * constructor
     * @param pieceplacement a pieceplacement string, which is a string with three charactor
     */
    PiecePlacement (String pieceplacement){
        origin = pieceplacement.charAt(0);
        piece = Piece.valueOf(pieceplacement.substring(1, 2));
        orientation = Orientation.valueOf(pieceplacement.substring(2, 3));
        ori = pieceplacement.substring(2, 3);
        pie = pieceplacement.substring(1, 2).charAt(0);
    }

    /**
     * By Shijie
     * if a orientation is between 'G' and 'K', we should convert it between 'A' and 'F'.
     * For example
     * 'G' - 'A'
     * 'H' - 'B'
     * 'I' - 'C'
     * 'J' - 'D'
     * 'K' - 'E'
     * @param orientation
     * @return orientation with string type
     */
    String fix(String orientation){
        if (orientation.charAt(0) > 'F'){
            return (char)((orientation.charAt(0) - 'F') + ('A' - 1)) + "";
        }else {
            return orientation;
        }
    }

    /**
     * By Shijie and Hongbo
     * if the orientation of piece has changed, the orientation of units also should change.
     * @param piece
     * @param ori
     */
    void translateorientation(Piece piece, String ori){

        restorePiece();

        if ( this.pie > 'L' || this.pie < 'A' ) throw new IllegalArgumentException("wrong piece");
        if ( this.pie == 'A' ) {
            if ( ori.charAt(0) > 'F' || ori.charAt(0) < 'A' ) throw new IllegalArgumentException("wrong orientation");
        } else {
            if (ori.charAt(0) > 'L' || ori.charAt(0) < 'A') throw new IllegalArgumentException("wrong orietation");
        }

        if (ori == "A"){
            piece.uandos[0].orientation = piece.uandos[0].orientation;
            piece.uandos[1].orientation = piece.uandos[1].orientation;
            piece.uandos[2].orientation = piece.uandos[2].orientation;
        }else if (ori.charAt(0) < 'G' ) {  // modified by Hongbo Sep 21. take flip and null into account

            int change = (ori.charAt(0) - 'A');
            char o1 = piece.get1o();
            char o2 = piece.get2o();
            char o3 = piece.get3o();
            //System.out.println("before: o1,o2,o3="+o1+o2+o3);

            if ( piece.uandos[0].unit.open != 0 ) {
                piece.uandos[0].orientation = Orientation.valueOf(fix("" + (char) (change + o1)));
            }
            if ( piece.uandos[1].unit.open != 0 ) {
                piece.uandos[1].orientation = Orientation.valueOf(fix("" + (char) (change + o2)));
            }
            if ( piece.uandos[2].unit.open != 0 ) {
                piece.uandos[2].orientation = Orientation.valueOf(fix("" + (char) (change + o3)));
            }
            //System.out.println("after: o1,o2,o3="+piece.get1o()+piece.get2o()+piece.get3o());
        } else {
            translateorientationWithFlip(piece, ori);
        }
    }

    /**
     * By Hongbo
     * tranlateorientation with considering the flip case
     * @param piece
     * @param ori
     */
    void translateorientationWithFlip(Piece piece, String ori) {
        // first flip to G
        for ( int i = 0; i < 3; i++) {
            if ( piece.uandos[i].unit.open == 1 ) {
                if (piece.uandos[i].orientation == Orientation.valueOf("B") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("F");
                } else if (piece.uandos[i].orientation == Orientation.valueOf("C") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("E");
                } else if (piece.uandos[i].orientation == Orientation.valueOf("E") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("C");
                } else if (piece.uandos[i].orientation == Orientation.valueOf("F") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("B");
                }
            } else if ( piece.uandos[i].unit.open == 2 ) {
                if (piece.uandos[i].orientation == Orientation.valueOf("B") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("C");
                } else if (piece.uandos[i].orientation == Orientation.valueOf("C") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("B");
                } else if (piece.uandos[i].orientation == Orientation.valueOf("E") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("F");
                } else if (piece.uandos[i].orientation == Orientation.valueOf("F") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("E");
                } else if (piece.uandos[i].orientation == Orientation.valueOf("A") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("D");
                } else if (piece.uandos[i].orientation == Orientation.valueOf("D") ) {
                    piece.uandos[i].orientation = Orientation.valueOf("A");
                }
            }
        }

        // other case
        int change = (char)(ori.charAt(0) - 'G');
        char o1 = piece.get1o();
        char o2 = piece.get2o();
        char o3 = piece.get3o();
        if ( piece.uandos[0].unit.open != 0 ) {
            piece.uandos[0].orientation = Orientation.valueOf(fix("" + (char) (change + o1)));
        }
        if ( piece.uandos[1].unit.open != 0 ) {
            piece.uandos[1].orientation = Orientation.valueOf(fix("" + (char) (change + o2)));
        }
        if ( piece.uandos[2].unit.open != 0 ) {
            piece.uandos[2].orientation = Orientation.valueOf(fix("" + (char) (change + o3)));
        }
    }

    /**
     * By Hongbo
     * restore the orientation of unit to their original state, i.e. when piece is in orientation A.
     */
    public void restorePiece() {

        Piece.A.uandos[0].orientation = Orientation.D;
        Piece.A.uandos[1].orientation = Orientation.A;
        Piece.A.uandos[2].orientation = Orientation.A;

        Piece.B.uandos[0].orientation = Orientation.D;
        Piece.B.uandos[1].orientation = null;
        Piece.B.uandos[2].orientation = Orientation.C;

        Piece.C.uandos[0].orientation = Orientation.D;
        Piece.C.uandos[1].orientation = null;
        Piece.C.uandos[2].orientation = Orientation.B;

        Piece.D.uandos[0].orientation = Orientation.D;
        Piece.D.uandos[1].orientation = null;
        Piece.D.uandos[2].orientation = Orientation.E;

        Piece.E.uandos[0].orientation = Orientation.D;
        Piece.E.uandos[1].orientation = null;
        Piece.E.uandos[2].orientation = Orientation.A;

        Piece.F.uandos[0].orientation = Orientation.D;
        Piece.F.uandos[1].orientation = null;
        Piece.F.uandos[2].orientation = Orientation.B;

        Piece.G.uandos[0].orientation = Orientation.D;
        Piece.G.uandos[1].orientation = Orientation.F;
        Piece.G.uandos[2].orientation = Orientation.F;

        Piece.H.uandos[0].orientation = Orientation.B;
        Piece.H.uandos[1].orientation = null;
        Piece.H.uandos[2].orientation = Orientation.C;

        Piece.I.uandos[0].orientation = Orientation.D;
        Piece.I.uandos[1].orientation = Orientation.F;
        Piece.I.uandos[2].orientation = Orientation.B;

        Piece.J.uandos[0].orientation = Orientation.D;
        Piece.J.uandos[1].orientation = Orientation.F;
        Piece.J.uandos[2].orientation = Orientation.A;

        Piece.K.uandos[0].orientation = Orientation.D;
        Piece.K.uandos[1].orientation = Orientation.E;
        Piece.K.uandos[2].orientation = Orientation.E;

        Piece.L.uandos[0].orientation = Orientation.D;
        Piece.L.uandos[1].orientation = Orientation.E;
        Piece.L.uandos[2].orientation = Orientation.D;
    }

}
