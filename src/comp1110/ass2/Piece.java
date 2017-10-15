package comp1110.ass2;

import static comp1110.ass2.Unit.*;


/**
 * Created by Shijie on 2016/8/31.
 */
public enum Piece {
    A(new UandO(BALL1 ,Orientation.D), new UandO(RING2, Orientation.A), new UandO(BALL1, Orientation.A)),
    B(new UandO(BALL1, Orientation.D), new UandO(RING0, null), new UandO(RING1, Orientation.C)),
    C(new UandO(BALL1, Orientation.D), new UandO(RING0, null), new UandO(RING1, Orientation.B)),
    D(new UandO(BALL1, Orientation.D), new UandO(RING0, null), new UandO(RING1, Orientation.E)),
    E(new UandO(BALL1, Orientation.D), new UandO(RING0, null), new UandO(RING1, Orientation.A)),
    F(new UandO(BALL1, Orientation.D), new UandO(RING0, null), new UandO(RING1, Orientation.B)),
    G(new UandO(BALL1, Orientation.D), new UandO(RING1, Orientation.F), new UandO(BALL1, Orientation.F)),
    H(new UandO(RING1, Orientation.B), new UandO(RING0, null), new UandO(RING1, Orientation.C)),
    I(new UandO(BALL1, Orientation.D), new UandO(BALL2, Orientation.F), new UandO(RING1, Orientation.B)),
    J(new UandO(BALL1, Orientation.D), new UandO(BALL2, Orientation.F), new UandO(RING1, Orientation.A)),
    K(new UandO(BALL1, Orientation.D), new UandO(RING1,Orientation.E), new UandO(BALL1, Orientation.E)),
    L(new UandO(BALL1, Orientation.D), new UandO(RING1,Orientation.E), new UandO(RING1, Orientation.D));

    UandO[] uandos = new UandO[3];

    /**
     * By Shijie
     * constructor of enum piece
     * the left most unit is the first unit, the origin is the second unit, the other is the third unit
     * @param a the first unit
     * @param b the second unit
     * @param c the third unit
     */
    Piece(UandO a, UandO b, UandO c) {
        uandos[0] = a;
        uandos[1] = b;
        uandos[2] = c;
    }

    /**
     * By Shijie
     * toString method
     * @return
     */
    @Override
    public String toString(){
        return "(" + uandos[0].unit +", " + uandos[0].orientation + ")" + ", " + "(" + uandos[1].unit +", " + uandos[1].orientation + ")" + ", " + "(" + uandos[2].unit +", " + uandos[2].orientation + ")";
    }

    /**
     * By Shijie
     * get the orientation of each units.
     * @return orientation of each units.
     */
    char get1o(){
        return ("" + uandos[0].orientation).charAt(0);
    }

    char get2o(){
        return ("" + uandos[1].orientation).charAt(0);
    }

    char get3o(){
        return ("" + uandos[2].orientation).charAt(0);
    }
}
