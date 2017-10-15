package comp1110.ass2.gui;

import comp1110.ass2.LinkGame;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.image.ImageView;
import java.io.*;
import java.net.URL;
import java.util.*;

public class Board extends Application {
    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;

    // FIXME Task 8: Implement a basic playable Link Game in JavaFX that only allows pieces to be placed in valid places

    // FIXME Task 9: Implement starting placements

    // FIXME Task 11: Implement hints

    // FIXME Task 12: Generate interesting starting placements


    /* board layout */
    private static final int SQUARE_SIZE = 100;
    private static final int PIECE_IMAGE_SIZE = 3*SQUARE_SIZE;
    private static final double ROW_HEIGHT = SQUARE_SIZE * 0.8660254; // 60 degrees
    private static final int VIEWER_WIDTH = BOARD_WIDTH;
    private static final int VIEWER_HEIGHT = BOARD_HEIGHT;
    protected static final double PEGINTERVALX = 55;
    protected static final double PIECESIZE = 3*PEGINTERVALX;

    private static final int BUTTON_X = 458;
    private static final int BUTTON_Y = 650;
    private static final int BUTTON2_X = 550;
    private static final int BUTTON2_Y = 650;
    private static final int SLIDER_X = 313;
    private static final int SLIDER_Y = 650;
    private static final int SLIDER_CAPTION_X = 248;
    private static final int SLIDER_CAPTION_Y = 650;
    private static final int COMPLETION_TEXT_X = 240;
    private static final int COMPLETION_TEXT_Y = 580;

    private static final String URI_BASE = "assets/";

    private static final Group root = new Group();
    private static final Group controls = new Group();
    private static final Group pegs = new Group();
    private static final Group pieces = new Group();
    private static final Group solution = new Group();
    private final Slider difficulty = new Slider();
    private static final Text completionText = new Text("Congratulation!");
    private static Text timeRecorder = new Text("00:00:00");
    private static Text displayRecord = new Text();
    private static final Group startPieces = new Group();
    private static Group help = new Group();

    private static final Group rootAdv = new Group();
    private static final Group advControls = new Group();
    private static boolean isAdventure = false;
    private static final Text failText = new Text("Try Again!");
    private static final Text levelText = new Text();

    private static String startString = "";
    private static String theSolution = "";
    private static final double opacity = 0.2;
    private static boolean isComplete = false;
    private static int timePlayed = 0;
    private static double diff;
    private static ArrayList<Record> records = new ArrayList<>();
    private static Random ran = new Random();
    //private static String nameString = "";

    /*
      By Hongbo,
      class for record of this game
     */
    private static class Record {
        public String recorder;
        public int sec;
        Record(String name, int time) {
            recorder = name;
            sec = time;
        }
    }

    /**
     * By Hongbo
     * Draw a starting placement in the window, removing any previously drawn one,
     * and draw the starting pieces at their home position as transparent.
     */
    private static void makeStartPlacement() {
        if ( LinkGame.isPlacementValid(startString)) {
            Draggable.presetPlacement(startString);
            startPieces.getChildren().clear();
            ArrayList<String> piecePlacements = breakPlacementString(startString);
            for (int i = 0; i < piecePlacements.size(); i++) {
                startPieces.getChildren().add(new NonDraggable(piecePlacements.get(i)));
                NonDraggable tmp = new NonDraggable(piecePlacements.get(i).charAt(1),true);
                tmp.setOpacity(opacity);
                startPieces.getChildren().add(tmp);
            }
        }
    }

    /**
     * By Hongbo
     * break placement string into piece placement
     * @param placement: a placement String
     * @return: arrayList of piece placement string
     */
    public static ArrayList<String> breakPlacementString(String placement) {
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
     * *  By Hongbo
     *  generate solution of a game with given starting placement,
     *  and put the solution to the Group solution.
     *  if isRead, then read the solution from file.
     *  if !isRead, then calculate the solution
     * @param isRead
     */
    public static void makeSolution(boolean isRead) {
        String solu = "";
        if ( !isRead ) {
            String[] solus = LinkGame.getSolutions(startString);
            if (solus.length == 1) {
                solu = solus[0];
                Draggable.setSolution(solu);
            }
        } else if ( isRead ) {
            solu = theSolution;
            Draggable.setSolution(solu);
        }
        //System.out.println(solu);
        solution.getChildren().clear();
        ArrayList<String> piecePlacements = breakPlacementString(solu);
        for (int i = 0; i < piecePlacements.size(); i++) {
            NonDraggable tmp = new NonDraggable(piecePlacements.get(i));
            //tmp.setOpacity(0);
            solution.getChildren().add(tmp);
        }
        solution.setOpacity(0);
    }

    /**
     * By Hongbo
     * make short cut key of the game.
     * "/" : show solution as a hint.
     * @param scene : the scene of the primaryStage
     */
    public static void makeKeyShortCut(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if ( event.getCode() == KeyCode.SLASH ) {
                solution.setOpacity(1.0);
                solution.toFront();
                pieces.setOpacity(0);
            }
        });
        scene.setOnKeyReleased(event -> {
            if ( event.getCode() == KeyCode.SLASH ) {
                solution.setOpacity(0);
                solution.toBack();
                pieces.setOpacity(1.0);
            }
        });
    }

    /**
     * By Hongbo
     * Create difficult slide bar, a restart button, a new game button and an adventure button
     * for training mode
     */
    private void makeControls(Stage primaryStage, Scene scene,Scene advanScene) {

        controls.getChildren().clear();

        Button button = new Button("New Game");
        button.setLayoutX(BUTTON_X);
        button.setLayoutY(BUTTON_Y);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                newGame();
            }
        });
        controls.getChildren().add(button);

        Button button2 = new Button("Restart");
        button2.setLayoutX(BUTTON2_X);
        button2.setLayoutY(BUTTON2_Y);
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                restart();
            }
        });
        controls.getChildren().add(button2);

        Button toAdv = new Button("Adventure");
        toAdv.setLayoutX(BUTTON2_X + 68);
        toAdv.setLayoutY(BUTTON2_Y);
        toAdv.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initializeAdventure(primaryStage,scene,advanScene);
            }
        });
        controls.getChildren().add(toAdv);

        difficulty.setMin(1);
        difficulty.setMax(8);
        difficulty.setValue(1);
        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(1);
        difficulty.setMinorTickCount(0);
        difficulty.setSnapToTicks(true);

        difficulty.setLayoutX(SLIDER_X);
        difficulty.setLayoutY(SLIDER_Y);
        controls.getChildren().add(difficulty);

        final Label difficultyCaption = new Label("Difficulty:");
        difficultyCaption.setTextFill(Color.GREY);
        difficultyCaption.setLayoutX(SLIDER_CAPTION_X);
        difficultyCaption.setLayoutY(SLIDER_CAPTION_Y);
        controls.getChildren().add(difficultyCaption);
    }

    /**
     * By Hongbo
     * Create a restart button, and an training button
     * for adventure mode
     */
    private void makeAdvControls(Stage primaryStage, Scene scene, Scene advanScene) {

        advControls.getChildren().clear();

        Button button2 = new Button("New Adventure");
        button2.setLayoutX(220);
        button2.setLayoutY(BUTTON2_Y);
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                newAdvGame();
            }
        });
        advControls.getChildren().add(button2);

        Button toFree = new Button("Training");
        toFree.setLayoutX(480);
        toFree.setLayoutY(BUTTON2_Y);
        toFree.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initializeFreeMode(primaryStage, scene, advanScene);
            }
        });
        advControls.getChildren().add(toFree);
    }

    /**
     * By Hongbo
     * make timer, record the time / count down the time. and make a fancy animation after completing the game.
     */
    private static void makeTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),
                ae -> {
                    if ( !isAdventure ) {
                        if (!isComplete) {
                            timePlayed = timePlayed + 1;
                        } else {
                            solution.setOpacity(1.0);
                            solution.toFront();
                            pieces.setOpacity(0);
                            startPieces.setOpacity(0);
                            pegs.setOpacity(0);
                            for (Node p : solution.getChildren()) {
                                NonDraggable tmp = (NonDraggable) p;
                                tmp.setFitWidth((2 * (ran.nextDouble() - 0.5) + 1) * PIECESIZE);
                                tmp.setFitWidth((2 * (ran.nextDouble() - 0.5) + 1) * PIECESIZE);
                            }
                        }
                        root.getChildren().remove(timeRecorder);
                        timeRecorder.setText(toTimeString(timePlayed / 10));
                        timeRecorder.setFill(Color.BLACK);
                        timeRecorder.setFont(Font.font("Arial", 40));
                        timeRecorder.setLayoutX(SQUARE_SIZE * 2.5);
                        timeRecorder.setLayoutY(ROW_HEIGHT * 0.75);
                        timeRecorder.setTextAlignment(TextAlignment.CENTER);
                        root.getChildren().add(timeRecorder);
                    } else {
                        if ( !isComplete ) {
                            timePlayed = timePlayed - 1;
                            if ( timePlayed < 0 ) {
                                showFail();
                            }
                        } else {
                            solution.setOpacity(1.0);
                            solution.toFront();
                            pieces.setOpacity(0);
                            startPieces.setOpacity(0);
                            pegs.setOpacity(0);
                            for ( Node p : solution.getChildren() ) {
                                NonDraggable tmp = (NonDraggable) p;
                                tmp.setFitWidth( (2*(ran.nextDouble()-0.5)+1)*PIECESIZE);
                                tmp.setFitWidth( (2*(ran.nextDouble()-0.5)+1)*PIECESIZE );
                            }
                        }
                        rootAdv.getChildren().remove(timeRecorder);
                        timeRecorder.setText(toTimeString(timePlayed/10));
                        timeRecorder.setFill(Color.BLACK);
                        timeRecorder.setFont(Font.font("Arial",40));
                        timeRecorder.setLayoutX(SQUARE_SIZE*2.5);
                        timeRecorder.setLayoutY(ROW_HEIGHT*0.75);
                        timeRecorder.setTextAlignment(TextAlignment.CENTER);
                        rootAdv.getChildren().add(timeRecorder);
                    }
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        if ( isComplete ) {
            timeline.stop();
        } else {
            timeline.play();
        }
    }

    /**
     * By Hongbo
     * make timer, count down the time, and make a fancy animation after completing the game for adventure mode only
     * not used any more.
     */
    private static void makeAdvTimer() {
        Timeline timelineAdv = new Timeline(new KeyFrame(Duration.millis(100),
                ae -> {
                    if ( !isComplete ) {
                        timePlayed = timePlayed - 1;
                        if ( timePlayed < 0 ) {
                            showFail();
                        }
                    } else {
                        solution.setOpacity(1.0);
                        solution.toFront();
                        pieces.setOpacity(0);
                        startPieces.setOpacity(0);
                        pegs.setOpacity(0);
                        for ( Node p : solution.getChildren() ) {
                            NonDraggable tmp = (NonDraggable) p;
                            tmp.setFitWidth( (2*(ran.nextDouble()-0.5)+1)*PIECESIZE);
                            tmp.setFitWidth( (2*(ran.nextDouble()-0.5)+1)*PIECESIZE );
                        }
                    }
                    rootAdv.getChildren().remove(timeRecorder);
                    timeRecorder.setText(toTimeString(timePlayed/10));
                    timeRecorder.setFill(Color.BLACK);
                    timeRecorder.setFont(Font.font("Arial",40));
                    timeRecorder.setLayoutX(SQUARE_SIZE*2.5);
                    timeRecorder.setLayoutY(ROW_HEIGHT*0.75);
                    timeRecorder.setTextAlignment(TextAlignment.CENTER);
                    rootAdv.getChildren().add(timeRecorder);
                }));
        timelineAdv.setCycleCount(Animation.INDEFINITE);
        if ( isComplete ) {
            timelineAdv.stop();
        } else {
            timelineAdv.play();
        }
    }

    /**
     * By Hongbo
     * make the GUI part of scoring system
     */
    private static void makeRecord() {
        root.getChildren().remove(displayRecord);
        String tmp = "";
        for ( int i = 0; i < records.size(); i++) {
            String formatTmp = formatName(records.get(i).recorder);
            tmp = tmp + formatTmp + "  " + toTimeString(records.get(i).sec) + "\n";
        }
        //System.out.println(tmp);
        displayRecord.setText(tmp);
        displayRecord.setFill(Color.BLACK);
        displayRecord.setFont(Font.font("Arial",20));
        displayRecord.setLayoutX(0.2*SQUARE_SIZE);
        displayRecord.setLayoutY(ROW_HEIGHT*5.5);
        displayRecord.setTextAlignment(TextAlignment.RIGHT);
        root.getChildren().add(displayRecord);
    }

    /**
     * By Hongbo
     * format output of names
     * @param name
     * @return 10 character long string. if name.length > 10, then trunct, if < 10, use space
     */
    private static String formatName(String name) {
        String rt = name;
        if ( name == null ) {
            return "Anonymous ";
        } else if ( name.length() > 10 ) {
            rt = name.substring(0,10);
        } else {
            for ( int i = 0; i<10-name.length();i++) {
                rt = rt + " ";
                //System.out.println(rt+"|");
            }
        }
        return rt;
    }

    /**
     * By Hongbo
     * transfer seconds to the formation of hh:mm:ss
     * @param seconds
     * @return a string of format hh:mm:ss
     */
    private static String toTimeString(int seconds) {
        int hours = seconds / 3600;
        int mins = (seconds - 3600*hours) / 60;
        int secs = seconds - 3600*hours - 60*mins;
        return String.format("%02d:%02d:%02d",hours,mins,secs);
    }

    /**
     * By Hongbo
     * restart the game when click the "restart" button on GUI:
     * reset the String placement in Draggable class
     * put all the non-starting pieces to their home position
     */
    private static void restart() {
        isComplete = false;
        timePlayed = 0;
        completionText.setOpacity(0);
        solution.setOpacity(0);
        solution.toBack();
        pieces.setOpacity(1.0);
        startPieces.setOpacity(1.0);
        pegs.setOpacity(1.0);
        Draggable.presetPlacement(startString);
        for ( Node n : pieces.getChildren() ) {
            ((Draggable) n).snapToHome();
        }
        readRecord((int)Math.round(diff));
        makeRecord();
    }

    /**
     * By Hongbo
     * start a new game in training mode when click "new game" button on GUI:
     * read the difficulty from the slider, generate a starting placement,
     * put lots Groups on the GUI, set the completion notification transparent.
     */
    private void newGame() {
        isComplete = false;
        timePlayed = 0;
        diff = difficulty.getValue();
        solution.setOpacity(0);
        solution.toBack();
        pieces.setOpacity(1.0);
        startPieces.setOpacity(1.0);
        pegs.setOpacity(1.0);
        setUpStartString(diff);
        makeStartPlacement();
        makePieces();
        makeSolution(true);
        completionText.setOpacity(0);
        readRecord((int)Math.round(diff));
        makeRecord();
    }

    /**
     * By Hongbo
     * start a new game in adventure mode
     */
    private static void newAdvGame() {
        isComplete = false;
        timePlayed = 18000;
        diff = 1;
        levelText.setOpacity(1);
        solution.setOpacity(0);
        solution.toBack();
        pieces.setOpacity(1.0);
        startPieces.setOpacity(1.0);
        pegs.setOpacity(1.0);
        setUpStartString(diff);
        makeStartPlacement();
        makePieces();
        makeSolution(true);
        completionText.setOpacity(0);
        failText.setOpacity(0);
    }

    /**
     * By Hongbo
     * start a new game in adventure mode with a given difficulty
     * @param difficulty
     */
    private static void newAdvGame(double difficulty) {
        isComplete = false;
        levelText.setOpacity(1);
        diff = difficulty;
        solution.setOpacity(0);
        solution.toBack();
        pieces.setOpacity(1.0);
        startPieces.setOpacity(1.0);
        pegs.setOpacity(1.0);
        setUpStartString(diff);
        makeStartPlacement();
        makePieces();
        makeSolution(true);
        completionText.setOpacity(0);
        failText.setOpacity(0);
    }

    /**
     * By Hongbo
     * read the record of a given difficulty level from file
     * @param diffi
     */
    private static void readRecord(int diffi) {
        try {
            String Directory = System.getProperty("user.dir");
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Integer> seconds = new ArrayList<>();
            String path = Directory + "/src/comp1110/ass2/SolutionDictionary/record.txt";
            File filename = new File(path);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                Scanner thisLine = new Scanner(line);
                names.add(thisLine.next());
                seconds.add(thisLine.nextInt());
                line = br.readLine();
            }
            br.close();
            records.clear();
            for ( int i = 0; i < 10; i++) {
                Record tmp = new Record(names.get(10*(diffi-1)+i),seconds.get(10*(diffi-1)+i));
                records.add(tmp);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * By Hongbo
     * If some one breaks the record, then record it
     * @param diffi
     */
    private static void updateRecord(int diffi) {
        int ind = 100;
        for ( int i = 0; i < 10; i++ ) {
            if ( timePlayed/10 < records.get(i).sec ) {
                ind = i;
                break;
            }
        }
        if ( ind < 10 ) {

            TextInputDialog grid = new TextInputDialog("anonym.");
            grid.getEditor();
            //grid.initOwner(parent);
            grid.setTitle("Input your name");
            grid.setContentText("Your Name");
            grid.setHeaderText("Congratulations!\nYou become the top player!");
            Optional<String> nameString = grid.showAndWait();

            //records.add(ind,new Record("Hongbo",timePlayed));
            if ( ! nameString.isPresent() || nameString.get().equals("") || nameString.get().equals(" ") ) {
                records.add(ind, new Record("anonym.", timePlayed/10));
            } else {
                records.add(ind, new Record(nameString.get(), timePlayed/10));
            }
            records.remove(records.size()-1);

            try {
                RandomAccessFile writeRecord = new RandomAccessFile("src/comp1110/ass2/SolutionDictionary/record.txt","rw");
                for ( int i = 0; i < (diffi-1)*10; i++ ) {
                    writeRecord.readLine();
                }
                for ( int i = 0; i < 10; i++) {
                    String willWrite = String.format("%-13s%6d%n",records.get(i).recorder,records.get(i).sec);
                    //writeRecord.writeChars(willWrite);
                    for ( int j = 0; j < willWrite.length(); j++) {
                        writeRecord.write(willWrite.charAt(j));
                    }
                    //writeRecord.writeChar('\n');
                }
                writeRecord.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    /**
     * By Hongbo
     * show the completion notification on the GUI
     */
    protected static void showCompletion() {
    //protected void showCompletion() {
        if ( ! isAdventure ) {
            completionText.setOpacity(1);
            isComplete = true;
            //a window for input username
            updateRecord((int) Math.round(diff));
            makeRecord();
        } else {
            int diffInt = (int) Math.round(diff);
            if ( diffInt < 8 ) {
                diff = diff + 1;
                newAdvGame(diff);
                diffInt++;
                levelText.setText("LEVEL "+diffInt);
            } else {
                completionText.setOpacity(1);
                isComplete = true;
                levelText.setOpacity(0);
            }
        }
    }

    /**
     * By Hongbo
     * put completion notification on the GUI
     */
    private static void makeCompletion() {
        completionText.setFill(Color.BLACK);
        completionText.setFont(Font.font("Arial", 60));
        completionText.setLayoutX(COMPLETION_TEXT_X);
        completionText.setLayoutY(COMPLETION_TEXT_Y);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }

    /**
     * By Hongbo
     * make the fail notification in adventure mode
     */
    private static void makeFail(){
        failText.setFill(Color.BLACK);
        failText.setFont(Font.font("Arial", 80));
        failText.setLayoutX(COMPLETION_TEXT_X);
        failText.setLayoutY(COMPLETION_TEXT_Y);
        failText.setTextAlignment(TextAlignment.CENTER);
        rootAdv.getChildren().add(failText);
    }

    /**
     * By Hongbo
     * notify in GUI when user fails in adventure mode
     */
    private static void showFail(){
        failText.setOpacity(1);
        isComplete = true;
        pieces.setOpacity(0);
        levelText.setOpacity(0);
    }

    /**
     * By Hongbo
     * make the completion notificatioin in adventure mode
     */
    private static void makeCompletionAdv() {
        completionText.setFill(Color.BLACK);
        completionText.setFont(Font.font("Arial", 80));
        completionText.setLayoutX(COMPLETION_TEXT_X);
        completionText.setLayoutY(COMPLETION_TEXT_Y);
        completionText.setTextAlignment(TextAlignment.CENTER);
        rootAdv.getChildren().add(completionText);
    }

    /**
     *  By Hongbo
     *  create the peg array. 24 pegs
     */
    public static void makePegArray() {
        PegArray viewerPegArray = new PegArray();
        viewerPegArray.setUpPegCircle();
        pegs.getChildren().clear();
        for ( int i = 0; i < 24; i++) {
            pegs.getChildren().add(viewerPegArray.pegCircle.get(i));
        }
    }

    /**
     * By Hongbo
     * ('A..L') - (the pieces in startString)
     * @return the non-starting pieces
     */
    private static String getRemainingPieces() {
        String remainPiecesString;
        remainPiecesString = "ABCDEFGHIJKL";
        for ( int i = 1; i < startString.length(); i = i+3) {
            //System.out.println("i = "+i+"; piece is "+startString.charAt(i));
            remainPiecesString = remainPiecesString.replace(startString.charAt(i)+"","");
        }
        //System.out.println(remainPiecesString);
        return remainPiecesString;
    }

    /**
     * By Hongbo
     * help for new users
     */
    private static void makeHelp() {
        help.getChildren().clear();
        Text helpText = new Text();
        String tmp ="";
        if ( !isAdventure ) {
            tmp = "HELP: training mode\n\nI. operation\n\n1. transparent ones are\n    not draggable.\n2. drag the piece to\n    move and snap\n" +
                    "3. right click to flip\n4. scroll to rotate\n5. press \"\\\" for hint.\n" +
                    "6. press restart btn\n    to restart.\n7. press newGame btn\n    for a new game.\n8. use slider for\n    other difficulty\n" +
                    "9. press adventure btn\n    to adventure mode.\n" +
                    "\nII. matching rules\n";
        } else {
            tmp = "HELP: adventure mode.\n\n\nIT'S COUNTING DOWN!\n\n\nI. operation\n\n1. transparent ones are\n    not draggable.\n2. drag the piece to\n    move and snap\n" +
                    "3. right click to flip\n4. scroll to rotate\n5. press \"\\\" for hint.\n" +
                    "6. press new adventure\n    btn to restart.\n" +
                    "7. press training btn\n    to training mode.\n" +
                    "\nII. matching rules\n";
        }
        helpText.setText(tmp);
        helpText.setFill(Color.BLACK);
        helpText.setFont(Font.font("Arial", 16));
        helpText.setLayoutX(725);
        helpText.setLayoutY(30);
        helpText.setTextAlignment(TextAlignment.LEFT);

        URL url1 = Draggable.class.getResource(URI_BASE + "matchRule1.png");
        ImageView rule1 = new ImageView();
        URL url2 = Draggable.class.getResource(URI_BASE + "matchRule2.png");
        ImageView rule2 = new ImageView();
        rule1.setImage(new Image(url1.toString()));
        rule2.setImage(new Image(url2.toString()));
        rule1.setFitHeight(144);
        rule1.setFitWidth(200);
        rule1.setX(725);
        rule1.setY(420);
        rule2.setFitHeight(133);
        rule2.setFitWidth(200);
        rule2.setX(725);
        rule2.setY(570);
        help.getChildren().addAll(helpText,rule1,rule2);
    }

    /**
     * By Hongbo
     * initialize the game. put the (12 - start) pieces, i.e. non-Starting pieces, on their home positions
     */
    public static void makePieces() {
        pieces.getChildren().clear();
        String remainingPieces = getRemainingPieces();
        for ( int i = 0; i < remainingPieces.length() ; i++) {
            pieces.getChildren().add(new Draggable(remainingPieces.charAt(i),true));
        }
    }

    /**
     * By Hongbo
     * for debug, no longer used
     * 8 level difficulty.
     * level 1 is easiest: 11 pieces are given in the startString.
     * level 8 is the hardest: 4 pieces are given in the startString.
     * level x : (12-x) pieces are given in the startString.
     * @param difficulty the difficult level of the game
     */
    private static void setUpStartStringTest(double difficulty) {
        diff = (int)Math.round(difficulty);
        if ( diff == 1 ) {
            startString = "KAFCBGUCAGDFLEFPFBBGESHBWIJKJAHKL";
        } else if ( diff == 2 ) {
            startString = "KAFCBGUCAGDFLEFPFBBGESHBWIJKJA";
        } else if ( diff == 3 ) {
            startString = "KAFCBGUCAGDFLEFPFBBGESHBOIA";
        } else if ( diff == 4 ) {
            startString = "KAFTBAICFRDCEELWFJJGDMHK";
        } else if ( diff == 5 ) {
             startString = "JABHBCBCGGDFIEKVFAFGG";
        } else if ( diff == 6 ) {
             startString = "JACRBHQCHCDGDELVFJ";
        } else if ( diff == 7 ) {
            startString = "IAFBBDRCEPDEWEB";
        } else if ( diff == 8 ) {
            startString = "GAEWBABCDJDA";
        }
    }

    /**
     * By Hongbo
     * 8 level difficulty.
     * level 1 is easiest: 11 pieces are given in the startString.
     * level 8 is the hardest: 4 pieces are given in the startString.
     * level x : (12-x) pieces are given in the startString.
     * @param difficulty the difficult level of the game
     */
    private static void setUpStartString(double difficulty) {
        int diffi = (int)Math.round(difficulty);

        try {
            String Directory = System.getProperty("user.dir");
            ArrayList<String> pieces = new ArrayList<>();
            ArrayList<String> sols = new ArrayList<>();
            String path = Directory + "/src/comp1110/ass2/SolutionDictionary/diff0" + diffi + ".txt";
            File filename = new File(path);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                Scanner thisLine = new Scanner(line);
                pieces.add(thisLine.next());
                sols.add(thisLine.next());
                line = br.readLine();
            }

            int totalLength = pieces.size();
            Random ran = new Random();
            int index = ran.nextInt(totalLength);
            startString = pieces.get(index);
            theSolution = sols.get(index);
            br.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * initialization method of adventure mode
     * @param primaryStage
     * @param scene
     * @param advanScene
     */
    private void initializeAdventure(Stage primaryStage, Scene scene, Scene advanScene) {
        isAdventure = true;
        rootAdv.getChildren().clear();
        rootAdv.getChildren().add(advControls);
        rootAdv.getChildren().add(pegs);
        rootAdv.getChildren().add(startPieces);
        rootAdv.getChildren().add(pieces);
        rootAdv.getChildren().add(solution);
        rootAdv.getChildren().add(help);
        //makeAdvTimer();
        makeAdvControls(primaryStage,scene,advanScene);
        makeKeyShortCut(advanScene);
        makeCompletionAdv();
        makeHelp();
        makeFail();
        newAdvGame();
        primaryStage.setScene(advanScene);

        levelText.setText("LEVEL 1");
        levelText.setFill(Color.BLACK);
        levelText.setFont(Font.font("Arial", 80));
        levelText.setLayoutX(COMPLETION_TEXT_X);
        levelText.setLayoutY(COMPLETION_TEXT_Y);
        levelText.setTextAlignment(TextAlignment.CENTER);
        rootAdv.getChildren().add(levelText);

        // separators
        Line r1 = new Line(720,15,720,685);
        r1.setStrokeWidth(2);
        r1.setStroke(Color.GRAY);
        Line l1 = new Line(15,440,715,440);
        l1.setStrokeWidth(2);
        l1.setStroke(Color.GRAY);
        rootAdv.getChildren().addAll(r1,l1);
    }

    /**
     * initialization method of training mode
     * @param primaryStage
     * @param scene
     * @param advanScene
     */
    private void initializeFreeMode(Stage primaryStage, Scene scene, Scene advanScene) {
        isAdventure = false;
        root.getChildren().clear();
        root.getChildren().add(controls);
        root.getChildren().add(pegs);
        root.getChildren().add(startPieces);
        root.getChildren().add(pieces);
        root.getChildren().add(solution);
        root.getChildren().add(help);
        //makeTimer();
        makeControls(primaryStage,scene,advanScene);
        makeKeyShortCut(scene);
        makePegArray();
        makeCompletion();
        makeHelp();
        newGame();
        primaryStage.setScene(scene);

        // separators
        Line r1 = new Line(720,15,720,685);
        r1.setStrokeWidth(2);
        //r1.setStroke(Color.web("000000"));
        r1.setStroke(Color.GRAY);
        Line l1 = new Line(15,440,715,440);
        l1.setStrokeWidth(2);
        l1.setStroke(Color.GRAY);
        Line l2 = new Line(235,445,235,685);
        l2.setStrokeWidth(2);
        l2.setStroke(Color.GRAY);
        root.getChildren().addAll(r1,l1,l2);
        /*
        Separator r1 = new Separator(Orientation.VERTICAL);
        r1.setMaxHeight(700);
        r1.setHalignment(VPos.CENTER);
        root.getChildren().add(r1);
        */
    }

    /**
     * By Hongbo
     * The main GUI
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("IQLink by thu11l");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        Scene advanScene = new Scene(rootAdv, VIEWER_WIDTH, VIEWER_HEIGHT);

        //Draggable.linkedToBoard(this);
        makeTimer();
        // adds all the Groups to root ( free play mode)
        // show free play mode as default
        initializeFreeMode(primaryStage,scene,advanScene);

        // adds all the groups to rootAdv (adventure mode)
        // not default

        primaryStage.show();
    }
}
