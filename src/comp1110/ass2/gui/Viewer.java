package comp1110.ass2.gui;

import comp1110.ass2.LinkGame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * A very simple viewer for piece placements in the link game.
 *
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 100;
    private static final int PIECE_IMAGE_SIZE = 3*SQUARE_SIZE;
    private static final double ROW_HEIGHT = SQUARE_SIZE * 0.8660254; // 60 degrees
    private static final int VIEWER_WIDTH = 750;
    private static final int VIEWER_HEIGHT = 500;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group pegs = new Group();
    private final Group pieces = new Group();
    TextField textField;

    //private LinkGame viewerLinkGame = new LinkGame();

    /**
     * By Hongbo
     * Draw a placement in the window, removing any previously drawn one
     * @param placement  A valid placement string
     */
    void makePlacement(String placement) {
        // FIXME Task 5: implement the simple placement viewer
        pieces.getChildren().clear();
        ArrayList<String> piecePlacements = breakPlacementString(placement);
        for ( int i = 0; i < piecePlacements.size(); i++) {
            pieces.getChildren().add(new Draggable(piecePlacements.get(i)));
        }

    }

    /**
     * By Hongbo
     * break placement string into piece placement
     * @param placement
     * @return: arrayList of piece placement string
     */
    public ArrayList<String> breakPlacementString(String placement) {
        ArrayList<String> piecePlacementStrings = new ArrayList<>();
        if ( ! LinkGame.isPlacementWellFormed(placement) ) {
            throw new RuntimeException("Bad input placement string: "+ placement);
        } else {
            for ( int i = 0; i<placement.length(); i=i+3) {
                String thispiece = placement.substring(i,i+3);
                if ( ! LinkGame.isPiecePlacementWellFormed(thispiece) ) {
                    throw new RuntimeException("Bad pieceplacement string: "+thispiece);
                } else {
                    piecePlacementStrings.add(thispiece);
                }
            }
        }
        return piecePlacementStrings;
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    /**
     * By Hongbo
     * create the peg array. 24 pegs
     */
    public  void makePegArray() {
        PegArray viewerPegArray = new PegArray();
        viewerPegArray.setUpPegCircle();
        for ( int i = 0; i < 24; i++) {
            pegs.getChildren().add(viewerPegArray.pegCircle.get(i));
        }
    }

    /**
     * By Hongbo
     * initialize the game. put the 12 pieces on their home positions
     */
    public  void makePieces() {
        pieces.getChildren().clear();
        for ( char ch = 'A'; ch <= 'L'; ch++) {
            pieces.getChildren().add(new Draggable(ch,true));
        }
    }

    /**
     * By Hongbo
     * used for debug, not used any longer
     * put the 12 pieces on the given position with default orientation
     */
    public void makePiecesWithOrigin() {
        pieces.getChildren().clear();
        for ( char ch = 'A'; ch <= 'L'; ch++) {
            pieces.getChildren().add(new Draggable(ch,ch));
        }
    }

    /**
     * By Hongbo
     * used for debug, not used any longer
     * put the 12 pieces on the given position with given orientation
     */
    public void makePiecesWithOriginAndRotation() {
        pieces.getChildren().clear();
        for ( char ch = 'A'; ch <= 'L'; ch++) {
            char[] pieceChar = {ch,ch,ch};
            String piece = new String(pieceChar);
            pieces.getChildren().add(new Draggable(piece));
        }
    }

    /**
     * By Hongbo
     * main GUI of viewer
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("LinkGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
        makeControls();

        root.getChildren().add(pegs);
        makePegArray();

        root.getChildren().add(pieces);
        makePieces();
        //makePiecesWithOrigin(); // for test
        //makePiecesWithOriginAndRotation(); // for test

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
