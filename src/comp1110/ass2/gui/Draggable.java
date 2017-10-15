package comp1110.ass2.gui;


import comp1110.ass2.LinkGame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hongbo on 16-8-24.
 * Draggable is inheritanted from NonDraggle
 * Draggable can responds to mouse/keyboard operation and some other operations such as check solution and so on.
 */
public class Draggable extends NonDraggable {

    /*
    private static final String URI_BASE = "assets/";
    private static final int PEGROW = 4;
    private static final int PEGCOL = 6;
    private static final int PEGNUM = PEGROW*PEGCOL;
    private static final double PEGINTERVALX = 55;
    private static final double PEGINTERVALY = PEGINTERVALX * 0.8660254;
    private static final double PEGRAD = PEGINTERVALX/4;
    private static final double SHIFT = PEGRAD*6;
    private static final double PEGBEGINX = 2*PEGINTERVALX + PEGRAD + SHIFT;
    private static final double PEGBEGINY = 2*PEGINTERVALX;
    private static final double PIECESIZE = 3*PEGINTERVALX;
    private double homeX;
    private double homeY;
    */

    private double mouseX;
    private double mouseY;

    private static String placement = "";
    private static String solution = "";
    //private HashMap<Character,String> allDraggableInstance = new HashMap<>();
    private boolean fl;
    private char rot;
    private char ori;
    //LinkGame forCheckValid = new LinkGame();
    //private static Board linkedBoard;

    /**
     * By Hongbo
     * constructor: other constructor will call this one.
     * supported operation:
     * scroll: rotate
     * right click: flip
     * drag: move
     * @param ch : 'A' to 'L', specified which piece
     * @param isHome: true: put the piece on its home position; false: put the piece at most left-up corner
     */
    Draggable(char ch, boolean isHome) {
        /*
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
        */
        super(ch,isHome);

        //toFront();

        setOnMousePressed( event  -> {
            if ( event.isPrimaryButtonDown()) { // left click for preparation for drag
                //mouseX = event.getScreenX();
                mouseX = event.getSceneX();
                //mouseY = event.getScreenY();
                mouseY = event.getSceneY();
                removeFromPlacement(ch);
            } else if ( event.isSecondaryButtonDown()) { // right click for flip
                flip();
                changeFlipInPlacement(ch);
                if (!LinkGame.isPlacementValid(placement)) {
                    snapToHome(); // snap to home if this placement is not valid
                    removeFromPlacement(ch); // remove the invalid pieceplacement from placement string
                }
                checkMove(); // check whether finish the game or not
            }
            });

        setOnMouseDragged( event -> {  // drag to move the piece
            // don't use the following two lines.
            // otherwise the piece will jump a distance of PIECESIZE/2 to make the mouse cursor to the origin of image
            // However, it will not affect running of the game. But my obsession does not allow.
            /*setX(event.getSceneX());
            setY(event.getSceneY());*/
            if ( event.getButton() == MouseButton.PRIMARY ) {
                toFront(); // otherwise, it may be covered by other piece.
                double mouseOffSetX = event.getSceneX() - mouseX;
                double mouseOffSetY = event.getSceneY() - mouseY;
                setX(getX() /*- PIECESIZE/2*/ + mouseOffSetX);
                setY(getY() /*- PIECESIZE/2*/ + mouseOffSetY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            }
        });

        setOnScroll( event -> { // scroll to rotate
            rotate();
            changeRotateInPlacement(ch);
            if (!LinkGame.isPlacementValid(placement)) {
                snapToHome();
                removeFromPlacement(ch); // remove the invalid pieceplacement from placement string
            }
            checkMove(); // check whether finish the game or not
            event.consume();
        });

        setOnMouseReleased( event -> { // release left mouse button to snap to grid
            if ( event.getButton() == MouseButton.PRIMARY) {
                rot = (char)('A' + getRotate()/60);
                if ( getScaleY() == 1 ) {
                    fl = false;
                } else {
                    fl = true;
                }
                int pegIndex;
                pegIndex = snap();
                if ( pegIndex != -1 ) {
                    ori = (char) ('A' + pegIndex);
                    //System.out.println("ch,fl,rot,ori: " + ch + fl + rot + ori);
                    addToPlacement(ch, fl, rot, ori); // add the piecePlacement to placement
                    if (!LinkGame.isPlacementValid(placement)) {
                        snapToHome();
                        removeFromPlacement(ch); // remove the invalid pieceplacement from placement string
                    }
                }
                checkMove();
            }
        });
    }

    /**
     * By Hongbo
     * constructor: a constructor with specific piece and the peg where its origin is on.
     * @param p : 'A' to 'L', specified which piece
     * @param orin: 'A' to 'X', the peg where p's origin is on.
     */
    Draggable(char p, char orin) {
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
    Draggable(String piecePlace) {
        this(piecePlace.charAt(1),piecePlace.charAt(0));
        char orient = piecePlace.charAt(2);
        if ( orient >= 'A' && orient <= 'F') {
            setRotate( (orient - 'A') * 60 % 360 );
        } else if ( orient >= 'G' && orient <= 'L') {
            setScaleY(-1);
            setRotate((orient - 'A') * 60 % 360);
        }
    }

    // TODO: Draggable(PiecePlacement piecePlacement)

    /**
     * By Hongbo
     * flip the piece
     */
    private void flip() {
        setRotate(0);
        setScaleY(getScaleY()*(-1));
    }

    /**
     * By Hongbo
     * if piece ch is already in the String placement, remove it if this piece placement is not correct.
     * @param ch A to L, specify which piece
     */
    private static void removeFromPlacement(char ch) {
        for ( int i = 0; i < placement.length()/3; i++) {
            if ( ch == placement.charAt(i*3+1) ) {
                placement = placement.substring(0,3*i) + placement.substring(3*i+3);
            }
        }
        //System.out.println("after removeFromPlacement: " + placement);
    }

    /**
     * By Hongbo
     * if one clicks the right button on a given piece ch, change the orientation of ch correspondingly if this piece is in String placement.
     * @param ch A to L, specify which piece
     */
    private void changeFlipInPlacement(char ch) {
        for ( int i = 0; i < placement.length()/3; i++) {
            if ( ch == placement.charAt(i*3+1) ) {
                if ( getScaleY() == 1 ) {
                    placement = placement.substring(0,3*i+2) + "A" + placement.substring(3*i+3);
                } else {
                    placement = placement.substring(0,3*i+2) + "G" + placement.substring(3*i+3);
                }
            }
        }
        //System.out.println("after changeFlipInPlacement: " + placement);
    }

    /**
     * if one scrolls on a give piece ch, change the orietation of ch correspondingly if this piece is in the String placement.
     * @param ch A to L, specify which piece
     */
    private void changeRotateInPlacement(char ch) {
        for ( int i = 0; i < placement.length()/3; i++) {
            if ( ch == placement.charAt(i*3+1) ) {
                if ( getScaleY() == 1 ) {
                    char or = (char)(getRotate()/60 + 'A');
                    placement = placement.substring(0,3*i+2) + or + placement.substring(3*i+3);
                } else {
                    char or = (char)(getRotate()/60 + 'G');
                    placement = placement.substring(0,3*i+2) + or + placement.substring(3*i+3);
                }
            }
        }
        //System.out.println("after changeFlipInPlacement: " + placement);
    }

    /**
     * By Hongbo
     * add a piece placement to placement, after drop the piece on the peg
     * @param ch A to L, specify which piece
     * @param fl flip or not
     * @param rot orientation of the piece
     * @param ori on which peg is origin of piece
     */
    private static void addToPlacement(char ch, boolean fl, char rot, char ori) {
        if ( !fl ) {
            placement = placement + ori + ch + rot;
        } else {
            rot = (char)((rot - 'A') + 'G');
            placement = placement + ori + ch + rot;
        }
        //System.out.println("placement: "+placement);
    }

    /**
     * By Hongbo
     * check whether finish the game or not, if the game is finished, show completion notification on the GUI
     */
    private static void checkMove() {
        if ( checkPlacement(placement,solution) ) {
            //System.out.println("DONE!");
            //board.showCompletion();
            Board.showCompletion();
        }
    }

    /**
     * By Hongbo
     * check the placement is equal to solution or not
     * @param placement
     * @param solution
     * @return
     */
    private static boolean checkPlacement(String placement, String solution) {
        if ( placement.length() != 36 ) return false;
        ArrayList<String> piecePlacement = new ArrayList<>();
        ArrayList<String> tmpSolution = new ArrayList<>();
        for ( int i = 0; i < piecePlacement.size(); i=i+3) {
            piecePlacement.add(placement.substring(i,i+3));
            tmpSolution.add(solution.substring(i,i+3));
        }
        for ( int i = 0; i < piecePlacement.size(); i++) {
            if ( tmpSolution.contains(piecePlacement.get(i)) ) {
                tmpSolution.remove(piecePlacement.get(i));
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * By Hongbo
     * set static field of Draggable solution to str
     * @param str the solution of this game
     */
    public static void setSolution(String str) {
        solution = str;
        //System.out.println("solution "+solution);
    }

    /**
     * By Hongbo
     * remove the last piece placement in the String placement
     * not used any longer
     */
    private static void toLastPlacement() {
        placement = placement.substring(0,placement.length()-3);
    }

    /**
     * By Hongbo
     * set placement to empty. used for restarting the game
     * not used any longer
     */
    public static void resetPlacement() {
        placement = "";
    }

    /**
     * By Hongbo
     * rotate the piece by 60 degree if some one scrolls the mouse
     */
    private void rotate() {
        setRotate( ( getRotate() + 60 ) % 360 );
    }

    /**
     * By Hongbo
     * snap the piece to the nearest peg
     * @return the peg index which is snapped
     */
    private int snap() { // snap the piece to the peg
        int index = -1;
        PegArray tmp = new PegArray();
        boolean isSnapHome;
        isSnapHome = true;
        for ( int i = 0; i< PEGNUM; i++ ) {
            double tmpX = tmp.pegCenterCoordinate((char)(i+'A')).x();
            double tmpY = tmp.pegCenterCoordinate((char)(i+'A')).y();
            boolean conditionX = (Math.abs(getX() + PIECESIZE/2 - tmpX) <= PEGINTERVALX/2);
            boolean conditionY = (Math.abs(getY() + PIECESIZE/2 - tmpY) <= PEGINTERVALX/2);
            if (  conditionX && conditionY ) {
                setX(tmpX - PIECESIZE/2);
                setY(tmpY - PIECESIZE/2);
                isSnapHome = false;
                index = i;
            }
        }
        if (isSnapHome) {
            snapToHome();
        }
        return index;
    }

    /**
     * By Hongbo
     * snap the piece to its home position
     */
    public void snapToHome() { // put the piece to its home if it is out of peg array
        setX(homeX);
        setY(homeY);
        setRotate(0);
        setScaleY(1);
    }

    /**
     * By Hongbo
     * set the static field of Draggable class placement to startString
     * @param startString starting placement
     */
    public static void presetPlacement(String startString) {
        placement = startString;
    }

    /*
    protected static void linkedToBoard(Board theBoard) {
        linkedBoard = theBoard;
    }
    */

}
