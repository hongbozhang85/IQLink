package comp1110.ass2.gui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

/**
 * Created by hongbo on 16-8-22.
 * This class is for generating a peg array
 */
public class PegArray {

    // TODO: unified the static final with Viewer class and Draggable class
    private static final int PEGROW = 4;
    private static final int PEGCOL = 6;
    private static final int PEGNUM = PEGROW*PEGCOL;
    private static final double PEGINTERVALX = 55;
    private static final double PEGINTERVALY = PEGINTERVALX * 0.8660254;
    private static final double PEGRAD = PEGINTERVALX/4;
    private static final double SHIFT = PEGRAD*6;
    private static final double PEGBEGINX = 2*PEGINTERVALX + PEGRAD + SHIFT;
    private static final double PEGBEGINY = 2*PEGINTERVALX;

    /**
     *  By Hongbo
     *  inner class
     *  Cartesian class provides the cartesian coordinates of a 2D point
     *  and some other relevant methods.
     *  x() and y() are the most useful methods, giving the x- and y- coordinate.
     */
    public class Cartesian {

        private ArrayList<Double> coor = new ArrayList<>(2);
        private static final int size = 2;
        private boolean isSet = false;

        /**
         * constructor, initially set the coordinates value
         * @param x x value
         * @param y y value
         */
        Cartesian(double x, double y) {
            coor.add(x);
            coor.add(y);
            isSet = true;
        }

        /**
         * By Hongbo
         * set the value of x and y coordinates
         * @param x x value
         * @param y y value
         * @return successful or not
         */
        public boolean reset(double x, double y) {
            coor.add(0,x);
            coor.add(1,y);
            isSet = true;
            if (coor.size() != size) return false;
            return true;
        }

        /**
         * By Hongbo
         * @return get the value of field isSet
         */
        public boolean getIsSet(){
            return isSet;
        }

        /**
         * By Hongbo
         * get x
         * @return x coordinate value
         */
        public double x() {
            return coor.get(0);
        }

        /**
         * By Hongbo
         * get y
         * @return y coordinate value
         */
        public double y() {
            return coor.get(1);
        }

        /**
         * By Hongbo
         * toString method for debug
         * @return human readable coordinate pair (x,y)
         */
        @Override
        public String toString() {
            return "("+coor.get(0)+", "+coor.get(1)+")";
        }
    }

    private ArrayList<Cartesian> pegCenter = new ArrayList<>(PEGNUM);
    public ArrayList<Circle>  pegCircle;
    public ArrayList<Polygon>  pegPolygon;

    /**
     *  By Hongbo
     *  Get an array of Circle as the pegs in this game
     */
    public void setUpPegCircle() {
        pegCircle = new ArrayList<>(PEGNUM);
        for ( int i = 0; i < PEGNUM; i++) {
            pegCenter.add(pegCenterCoordinate((char)(i+'A')));
            pegCircle.add(new Circle(pegCenter.get(i).x(),pegCenter.get(i).y(),PEGRAD, Color.GREY));
        }
    }

    /**
     * By Hongbo
     * not used any longer
     */
    public void setUpPegHexgon() {
        // TODO
    }

    /**
     * By Hongbo
     * call setUpPegCircle() to set up all the peg coordinates in the peg array
     */
    public void setUpPeg() {
        setUpPegCircle();
        setUpPegHexgon();
    }

    /**
     * By Hongbo
     * calculate the coordinates of a specific peg
     * @param peg: 'A' to 'X', there are 24 pegs
     * @return: the Cartesian of peg
     */
    public Cartesian pegCenterCoordinate(char peg) {
        int ind = peg - 'A';
        int row = ind / 6;
        int col = ind % 6;
        double[] xy = new double[2];
        xy[1] = row * PEGINTERVALY + PEGBEGINY;
        if ( row % 2 == 0 ) {
            xy[0] = col * PEGINTERVALX + PEGBEGINX;
        } else {
            xy[0] = col * PEGINTERVALX + PEGBEGINX + PEGINTERVALX / 2 ;
        }
        return new Cartesian(xy[0],xy[1]);
    }

    /*public static void main(String[] args) {
        PegArray myPegArray = new PegArray();
        for ( char c = 'A'; c <= 'X'; c++) {
            Cartesian tmp = myPegArray.pegCenterCoordinate(c);
            System.out.println(tmp);
        }
    }*/

}
