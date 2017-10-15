package comp1110.ass2.gui;

import comp1110.ass2.LinkGame;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.image.ImageView;
import java.net.URL;

/**
 * Created by hongbo on 16-9-26.
 * NonDraggable is for the pieces which cannot be operated by mouse
 * Draggable is inherited from this class
 */
public class NonDraggable extends ImageView {

    // TODO unified the static final with PegArray class and Viewer class
    protected static final String URI_BASE = "assets/";
    protected static final int PEGROW = 4;
    protected static final int PEGCOL = 6;
    protected static final int PEGNUM = PEGROW*PEGCOL;
    protected static final double PEGINTERVALX = 55;
    protected static final double PEGINTERVALY = PEGINTERVALX * 0.8660254;
    protected static final double PEGRAD = PEGINTERVALX/4;
    protected static final double SHIFT = PEGRAD*6;
    protected static final double PEGBEGINX = 2*PEGINTERVALX + PEGRAD + SHIFT;
    protected static final double PEGBEGINY = 2*PEGINTERVALX;
    protected static final double PIECESIZE = 3*PEGINTERVALX;
    protected double homeX;
    protected double homeY;


    /**
     * By Hongbo
     * constructor: other constructor will call this one.
     * put specific piece on the game board
     * @param ch : 'A' to 'L', specified which piece
     * @param isHome: true: put the piece on its home position; false: put the piece at most left-up corner
     */
    NonDraggable(char ch, boolean isHome) {
        if ( ch < 'A' || ch > 'L') throw new RuntimeException("Piece should be A to L, instead of "+ch);
        //Draggable tmp = new Draggable();
        //URL url = tmp.getClass().getResource(URI_BASE + ch + ".png")
        URL url = Draggable.class.getResource(URI_BASE + ch + ".png");
        setImage(new Image(url.toString()));
        setFitHeight(PIECESIZE);
        setFitWidth(PIECESIZE);
        if (!isHome) {
            setX(PEGBEGINX);
            setY(PEGBEGINY);
        } else {
            if ( ch < 'E') {
                homeX = 0;
                homeY = PIECESIZE*1.25/2*(ch - 'A');
            } else if ( ch < 'I') {
                homeX = PEGBEGINX - SHIFT + (ch - 'E')*2*PEGINTERVALX;
                homeY = PIECESIZE*1.25/2*('D' - 'A');
            } else {
                homeX = PEGBEGINX + (PEGCOL+1)*PEGINTERVALX;
                homeY = PIECESIZE*1.25/2*(ch - 'I');
            }
            setX(homeX);
            setY(homeY);
        }

        //toFront();
    }

    /**
     * By Hongbo
     * constructor: a constructor with specific piece and the peg where its origin is on.
     * @param p : 'A' to 'L', specified which piece
     * @param orin: 'A' to 'X', the peg where p's origin is on.
     */
    NonDraggable(char p, char orin) {
        this(p,true);
        PegArray tmp = new PegArray();
        setX(tmp.pegCenterCoordinate(orin).x() - PIECESIZE/2);
        setY(tmp.pegCenterCoordinate(orin).y() - PIECESIZE/2 );
    }

    /**
     * By Hongbo
     * constructor: a constructor with specific piece, the peg where origin is on and its orientation
     * @param piecePlace: a string with three charactor: origin+piece+orientation
     */
    NonDraggable(String piecePlace) {
        this(piecePlace.charAt(1),piecePlace.charAt(0));
        char orient = piecePlace.charAt(2);
        if ( orient >= 'A' && orient <= 'F') {
            setRotate( (orient - 'A') * 60 % 360 );
        } else if ( orient >= 'G' && orient <= 'L') {
            setScaleY(-1);
            setRotate((orient - 'A') * 60 % 360);
        }
    }

}
