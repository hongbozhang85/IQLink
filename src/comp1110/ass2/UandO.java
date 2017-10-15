package comp1110.ass2;

/**
 * Created by Shijie on 2016/9/14.
 */
public class UandO {
    Unit unit;
    Orientation orientation;

    /**
     * By Shijie
     *
     * definition of orientation of unit:
     *
     * Ring 1: from the center of ring to its opening, '*' is the center of ring
     *  B   C
     * A  *  D
     *  F  E
     *
     *  Ball 1: from the center of ball to its connection, '*' is the center of ball
     *  B   C
     * A  *  D
     *  F  E
     *
     *  Ring 2: from the center of ring to the middle of its opening, '*' is the center of ring
     *     A
     *  F     B
     *     *
     *  E     C
     *     D
     *
     *  Ball 2: from the center of ball to the middle of its two connections, '*' is the center of ball
     *     A
     *  F     B
     *     *
     *  E     C
     *     D
     *
     * @param unit
     * @param orientation
     */
    public UandO(Unit unit, Orientation orientation){
        this.unit = unit;
        this.orientation = orientation;

    }

    /**
     * By Hongbo
     * for the purpose of debug and used by LinkGame::isPlacementValid
     * @return three characters, such as R1E. The first one is b or r, the second one is 0, 1 or 2, the third one is A..E or null
     */
    @Override
    public String toString() {
        return unit.type + unit.open + orientation;
    }
}
