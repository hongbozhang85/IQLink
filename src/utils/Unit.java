package utils;

/**
 * Created by Yifan on 2016/9/10.
 * not used any longer. store here for file
 */
public class Unit {
    public enum UnitType {
        B1, B2, R0, R1, R2
    }

    public enum UnitOrientation {
        A, B, C, D, E, F
    }

    public Unit(UnitType unitType, UnitOrientation unitOrientation) {
    }

    public UnitOrientation rotateUnit(UnitOrientation unitOrientation) {
        if (unitOrientation == UnitOrientation.A) {
            return UnitOrientation.B;
        }
        if (unitOrientation == UnitOrientation.B) {
            return UnitOrientation.C;
        }
        if (unitOrientation == UnitOrientation.C) {
            return UnitOrientation.D;
        }
        if (unitOrientation == UnitOrientation.D) {
            return UnitOrientation.E;
        }
        if (unitOrientation == UnitOrientation.E) {
            return UnitOrientation.F;
        } else {
            return UnitOrientation.A;
        }
    }
    public UnitOrientation flipUnit(UnitOrientation unitOrientation) {
        if (unitOrientation == UnitOrientation.A) {
            return UnitOrientation.D;
        }
        if (unitOrientation == UnitOrientation.B) {
            return UnitOrientation.E;
        }
        if (unitOrientation == UnitOrientation.C) {
            return UnitOrientation.F;
        }
        if (unitOrientation == UnitOrientation.D) {
            return UnitOrientation.A;
        }
        if (unitOrientation == UnitOrientation.E) {
            return UnitOrientation.B;
        } else {
            return UnitOrientation.C;
        }
    }
}