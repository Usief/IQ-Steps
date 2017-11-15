package comp1110.ass2.gui;

import comp1110.ass2.StepsGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;


public class Board extends Application {
    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;

    private static final double PEG_START_X = 80;
    private static final double PEG_START_Y = BOARD_WIDTH/3.95;
    private static final double PEG_RADIUS = 17;
    private static final double PEG_SPACING = BOARD_WIDTH/4/4;

    private static final double CIRCLE_RADIUS = BOARD_WIDTH/4/3;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group pieces = new Group();
    private final Group solution = new Group();
    //private final Group board = new Group();
    private final Group controls = new Group();
    private final Group objective = new Group();

    private final Text completionText = new Text("Congratulation!");

    private final DraggableFXPiece[] piecesFX = new DraggableFXPiece[8];

    private Button newGameButton = new Button();
    private Button introductionButton = new Button();
    private Button restartButton = new Button();
    private Button hintButton = new Button();
    private String piecesState = new String();
    private String originalState = new String();
    private int stepCount;
    private int record=-1;
    private int indexOfIntroduction=0;
    private Text stepCountText = new Text();
    private Text recordText = new Text();


    /**
     *  The Image class for a piece on board.
     *
     *  @author Yu Yang(u6412985)
     */
    class FXPiece extends ImageView{
        int homeX, homeY;
        char piece;
        char direction = 'A';
        Image img;

        /**
         * Initialise a piece at a particular place on the board
         */
        FXPiece(char piece){
            this.piece = piece;

            img = new Image(Viewer.class.getResource(URI_BASE + this.piece + this.direction + ".png").toString());
            setImage(img);
            setFitWidth(BOARD_WIDTH/4);
            setFitHeight(BOARD_WIDTH/4);
            if(piece >='A' && piece <='D') {
                homeX = BOARD_WIDTH/4 * (piece - 'A');
                homeY = 0;
            }else{
                homeX = BOARD_WIDTH/4 * (piece - 'E');
                homeY = BOARD_HEIGHT - BOARD_WIDTH/4;
            }
            setLayoutX(homeX);
            setLayoutY(homeY);
        }
    }

    /**
     *  The dragaable piece class
     *
     *  @author Yu Yang(u6412985)
     */

    class DraggableFXPiece extends  FXPiece{
        double mouseX, mouseY;

        char pos;

        DraggableFXPiece(char piece){
            super(piece);

            /* event handlers */
            setOnScroll(event -> {
                rotate();
                event.consume();
            });

            setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.SECONDARY) {
                    if (this.direction <= 'D') {
                        this.direction = (char) (this.direction + 4);
                    } else {
                        this.direction = (char) (this.direction - 4);
                    }
                    this.img = new Image(Viewer.class.getResource(URI_BASE + this.piece + this.direction + ".png").toString());
                    setImage(this.img);
                }
            });

            setOnMousePressed(event ->{
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });

            setOnMouseDragged(event ->{
                int pieceIndex = isPieceOnBoard();
                if (pieceIndex != -1)                       // if the piece is on board, then remove the piece from the piecesState
                    piecesState = piecesState.substring(0, pieceIndex) + piecesState.substring(pieceIndex+3, piecesState.length());
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();

            });

            setOnMouseReleased(event -> {
                if(onBoard()) {
                    updateStepCount();
                    snapToGrid();
                }else{
                    snapToHome();
                }
            });

        }

        /**
         * Determine whether the piece is over the board.
         * @return true if the piece is on the board; flase, if the piece is over the border.
         */
        private boolean onBoard(){
            if (this.piece == 'B'){
                if (this.direction == 'A' || this.direction == 'G')
                    return getLayoutX()>= PEG_START_X - 200 && getLayoutX() + this.getFitWidth() <= PEG_START_X + PEG_SPACING * 10 + 20
                            && getLayoutY() >= PEG_START_Y - 80 && getLayoutY() + this.getFitHeight() <= PEG_START_Y + PEG_SPACING * 5 + 50;
                else if (this.direction == 'D' || this.direction == 'F')
                    return getLayoutX()>= PEG_START_X - 80 && getLayoutX() + this.getFitWidth() <= PEG_START_X + PEG_SPACING * 10 + 20
                            && getLayoutY() >= PEG_START_Y - 80 && getLayoutY() + this.getFitHeight() <= PEG_START_Y + PEG_SPACING * 5 + 150;
                else if (this.direction == 'B' || this.direction == 'H')
                    return getLayoutX()>= PEG_START_X - 80 && getLayoutX() + this.getFitWidth() <= PEG_START_X + PEG_SPACING * 10 + 20
                            && getLayoutY() >= PEG_START_Y - 160 && getLayoutY() + this.getFitHeight() <= PEG_START_Y + PEG_SPACING * 5 + 50;
                else
                    return getLayoutX()>= PEG_START_X - 80 && getLayoutX() + this.getFitWidth() <= PEG_START_X + PEG_SPACING * 10 + 120
                            && getLayoutY() >= PEG_START_Y - 80 && getLayoutY() + this.getFitHeight() <= PEG_START_Y + PEG_SPACING * 5 + 50;
            }else if(this.piece == 'E'){
                if (this.direction == 'C' || this.direction == 'E')
                    return getLayoutX()>= PEG_START_X - 200 && getLayoutX() + this.getFitWidth() <= PEG_START_X + PEG_SPACING * 10 + 20
                            && getLayoutY() >= PEG_START_Y - 80 && getLayoutY() + this.getFitHeight() <= PEG_START_Y + PEG_SPACING * 5 + 50;
                else if (this.direction == 'B' || this.direction == 'H')
                    return getLayoutX()>= PEG_START_X - 80 && getLayoutX() + this.getFitWidth() <= PEG_START_X + PEG_SPACING * 10 + 20
                            && getLayoutY() >= PEG_START_Y - 80 && getLayoutY() + this.getFitHeight() <= PEG_START_Y + PEG_SPACING * 5 + 150;
                else if (this.direction == 'D' || this.direction == 'F')
                    return getLayoutX()>= PEG_START_X - 80 && getLayoutX() + this.getFitWidth() <= PEG_START_X + PEG_SPACING * 10 + 20
                            && getLayoutY() >= PEG_START_Y - 160 && getLayoutY() + this.getFitHeight() <= PEG_START_Y + PEG_SPACING * 5 + 50;
                else
                    return getLayoutX()>= PEG_START_X - 80 && getLayoutX() + this.getFitWidth() <= PEG_START_X + PEG_SPACING * 10 + 120
                            && getLayoutY() >= PEG_START_Y - 80 && getLayoutY() + this.getFitHeight() <= PEG_START_Y + PEG_SPACING * 5 + 50;
            }
            return getLayoutX()>= PEG_START_X - 80 && getLayoutX() + this.getFitWidth() <= PEG_START_X + PEG_SPACING * 10 + 20
                    && getLayoutY() >= PEG_START_Y - 80 && getLayoutY() + this.getFitHeight() <= PEG_START_Y + PEG_SPACING * 5 + 50;
        }

        /**
         *  set the piece to the nearest peg.
         */
        private void snapToGrid(){
            double centreX;
            double centreY;

            centreX = getLayoutX() + this.getFitWidth()/2;
            centreY = getLayoutY() + this.getFitHeight()/2;

            setLayoutX((int)((centreX-PEG_START_X+CIRCLE_RADIUS/2)/PEG_SPACING)*PEG_SPACING + PEG_START_X - this.getFitWidth()/2);
            setLayoutY((int)((centreY-PEG_START_Y+CIRCLE_RADIUS/2)/PEG_SPACING)*PEG_SPACING + PEG_START_Y - this.getFitHeight()/2);
            setPosition();
            if(!StepsGame.isPlacementSequenceValid(piecesState)){
                snapToHome();
                piecesState.substring(0, piecesState.length()-3);
            }

            gameCompletion();
        }

        /**
         * set the piece position to the original place.
         */
        private void snapToHome(){
            int pieceIndex = isPieceOnBoard();
            if (pieceIndex != -1)
                piecesState = piecesState.substring(0, pieceIndex) + piecesState.substring(pieceIndex+3, piecesState.length());
            setLayoutX(homeX);
            setLayoutY(homeY);
        }

        /**
         * rotate the piece.
         */
        private void rotate(){
            setRotate((getRotate()+90)%360);
            if(this.direction<'E') {
                this.direction = (char) (getRotate() / 90 + 'A');
            }else{
                this.direction = (char) (getRotate() /90 + 'E');
            }
        }

        /**
         * change the piece position when dragging.
         */
        private void setPosition(){
            int x = (int)((getLayoutX() - PEG_START_X + this.img.getWidth()/2) / PEG_SPACING);
            int y = (int)((getLayoutY() - PEG_START_Y + this.img.getHeight()/2) / PEG_SPACING);

            this.pos =(char) (y * 10 + x);
            if (this.pos < 25){
                this.pos = (char) ('A' + this.pos);
            }else{
                this.pos = (char) ('a' + this.pos - 25);
            }
            piecesState += String.valueOf(this.piece) + String.valueOf(this.direction) +String.valueOf(this.pos);       //should be updated. When the piece cannot be putted on the board, the piecesState should not changed.
        }

        private void setPosition(String placement){
            int x=0;
            int y=0;

            piecesState += placement;

            this.direction = placement.charAt(1);
            this.pos = placement.charAt(2);
            if(this.direction <'E') {
                this.img = new Image(Viewer.class.getResource(URI_BASE + this.piece + "A" + ".png").toString());
                setImage(this.img);
                setRotate((this.direction-'A')*90);
            }else{
                this.img = new Image(Viewer.class.getResource(URI_BASE + this.piece + "E" + ".png").toString());
                setImage(this.img);
                setRotate((this.direction-'E')*90);
            }

            if(this.pos >= 'A' && this.pos <='Y'){
                x = (pos - 'A') % 10;
                y = (pos - 'A') / 10;
            }else if(this.pos >='a' && this.pos <= 'y'){
                x = (pos - 'a' + 25) % 10;
                y = (pos - 'a' + 25) / 10;
            }

            setLayoutX(PEG_START_X+PEG_SPACING*x-this.getFitWidth()/2);
            setLayoutY(PEG_START_Y+PEG_SPACING*y-this.getFitHeight()/2);

        }

        /**
         * determine whether the piece is already putted on the board.
         * @return an integer, the index of the piece in piecesState;
         *          -1 if the piece is not on the board.
         */

        private int isPieceOnBoard(){
            for(int i=0; i < piecesState.length();i+=3){
                if(piecesState.charAt(i)==this.piece)
                    return i;
            }
            return -1;
        }
    }

    private void gameCompletion(){
        if(piecesState.length() == 24){
            showCompletion();
            updateRecord();
        }
    }


    /**
     * get the board statement and judge the the end condition.
     */
    private void makeBoardStatement(){
        return;
    }


    /**
     * Display the pegs and board.
     *
     * @author Yu Yang(u6412985)
     */
    private void makeBoard(){
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                double x;
                if (i%2==0)
                    x = PEG_START_X + PEG_SPACING * 2 * j;
                else
                    x = PEG_START_X + PEG_SPACING + PEG_SPACING * 2 * j;
                double y = i * PEG_SPACING + PEG_START_Y;
                Circle circle = new Circle(x, y, PEG_RADIUS);
                circle.setFill(Color.GRAY);
                root.getChildren().add(circle);
            }
        }
    }

    private void makeStater(){
        // Task 8: Implement starting placements

        String[] placement = {"", "BGSEFBGCgAHQCDNHFlDAi","EFBFCgBGSGHn","EFBBFqCHSGHn","GDLCGOEEn","FBgEElBEe", "EFBBFS", "GDLCGOEEn", "EFBBFqCGQ"};// "EALBFqAESCEnHGlFAP"
//        System.out.println(StepsGame.isPlacementSequenceValid(placement[1]+"FHn"));
        Random r = new Random();
        int i = r.nextInt(placement.length);
        originalState = placement[i];
        for (int j=0; j<placement[i].length();j=j+3){
            piecesFX[placement[i].charAt(j) - 'A'].setPosition(placement[i].substring(j, j+3));
        }


        return;
    }

    /**
     *  make buttons for the basic functions, like introduction, restart and newgame.
     */
    private void makeControls(){

        newGameButton.setText("New Game");
        newGameButton.setLayoutX(BOARD_WIDTH-BOARD_WIDTH/4);
        newGameButton.setLayoutY(BOARD_HEIGHT/9*5-20);
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newGame();
            }
        });
        controls.getChildren().add(newGameButton);

        introductionButton.setText("Introduction");
        introductionButton.setLayoutX(BOARD_WIDTH-BOARD_WIDTH/8);
        introductionButton.setLayoutY(BOARD_HEIGHT/9*5-20);
        introductionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                makeIntroduction();
            }
        });
        controls.getChildren().add(introductionButton);

        restartButton.setText("Restart");
        restartButton.setLayoutX(BOARD_WIDTH-BOARD_WIDTH/4);
        restartButton.setLayoutY(BOARD_HEIGHT/9*5+20);
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                restart();
            }
        });
        controls.getChildren().add(restartButton);

        hintButton.setText("Hint");
        hintButton.setLayoutX(BOARD_WIDTH-BOARD_WIDTH/8);
        hintButton.setLayoutY(BOARD_HEIGHT/9*5+20);
        hintButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getHint();
            }
        });
        controls.getChildren().add(hintButton);

        return;
    }

    private void makeIntroduction(){

        String placement = "CFjBGKGAgHElAFnDDNFGQEDI";
        introductionButton.setText("Next Step");
        piecesFX[placement.charAt(indexOfIntroduction) - 'A'].setPosition(placement.substring(indexOfIntroduction, indexOfIntroduction+3));
        indexOfIntroduction = (indexOfIntroduction+3)%24;
        if (indexOfIntroduction==0){
            introductionButton.setText("Introduction");
            showCompletion();
        }

    }

    private void makeCompletion(){
        DropShadow ds = new DropShadow();
        ds.setOffsetY(4.0f);
        ds.setColor(Color.CORAL);
        completionText.setFill(Color.WHITE);
        completionText.setEffect(ds);
        completionText.setCache(true);
        completionText.setFont(Font.font("Times new Roman", FontWeight.EXTRA_BOLD, 100));
        completionText.setLayoutX(BOARD_WIDTH/9);
        completionText.setLayoutY(BOARD_HEIGHT/7*3);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);


    }

    /**
     * Show the completion message
     */
    private  void showCompletion(){
        completionText.toFront();;
        completionText.setOpacity(1);
    }

    /**
     * Hide the completion message
     */

    private void hideCompletion(){
        completionText.toBack();
        completionText.setOpacity(0);
    }

    private void makeStepCount(){
        stepCount = 0;
        stepCountText.setFill(Color.BLACK);
        stepCountText.setCache(true);
        stepCountText.setFont(Font.font("Times new Roman", FontWeight.NORMAL, 30));
        stepCountText.setLayoutX(BOARD_WIDTH-BOARD_WIDTH/4);
        stepCountText.setLayoutY(BOARD_HEIGHT/9*4);
        stepCountText.setTextAlignment(TextAlignment.CENTER);
        stepCountText.setText("Steps: " + String.valueOf(stepCount));
        stepCountText.toFront();

    }

    private void updateStepCount(){
        stepCount += 1;
        stepCountText.setText("Steps: " + String.valueOf(stepCount));
    }

    private void makeRecord(){
        recordText.setFill(Color.BLACK);
        recordText.setCache(true);
        recordText.setFont(Font.font("Times new Roman", FontWeight.NORMAL, 30));
        recordText.setLayoutX(BOARD_WIDTH-BOARD_WIDTH/4);
        recordText.setLayoutY(BOARD_HEIGHT/9*3.5);
        recordText.setTextAlignment(TextAlignment.CENTER);
        recordText.setText("Best Record:");
        recordText.toFront();
       // root.getChildren().add(recordText);
    }

    private void updateRecord(){
        if(record == -1){
            record = stepCount;
        }else if(stepCount < record){
            record = stepCount;
        }
        recordText.setText("Best Record: "+String.valueOf(record));
    }

    /**
     * put the pieces to their home position.
     *
     * @author Yu Yang(u6412985)
     */
    private void resetPieces(){
        pieces.getChildren().clear();
        for (char p='A';p<='H';p++) {
            piecesFX[p-'A'] = new DraggableFXPiece(p);
            pieces.getChildren().add(piecesFX[p-'A']);
        }
    }


    private void restart(){
        piecesState = "";
        stepCount = -1;
        updateStepCount();
        resetPieces();
        for(int i=0;i<originalState.length();i+=3){
            piecesFX[originalState.charAt(i)-'A'].setPosition(originalState.substring(i, i+3));
        }
    }

    private void getHint(){
        String[] hint = StepsGame.getHints(piecesState);
        if (hint.length==0){
            piecesFX[piecesState.charAt(piecesState.length()-3)-'A'].snapToHome();
        }else {
            piecesFX[hint[0].charAt(0) - 'A'].setPosition(hint[0]);
        }
    }

    /**
     *  Create a new game.
     */
    private void newGame() {
        resetPieces();
        try {
            piecesState = "";
            makeStepCount();
            hideCompletion();
            makeStater();
        }catch(IllegalArgumentException e){
            System.err.println("Sorry. "+ e);
            e.printStackTrace();
            Platform.exit();
        }

        return;
    }

    private void makeGrid(){
        Polyline board = new Polyline(0, 0, BOARD_WIDTH, 0, BOARD_WIDTH, BOARD_HEIGHT, 0, BOARD_HEIGHT, 0, 0);
        root.getChildren().add(board);
        Polyline grid = new Polyline(BOARD_WIDTH-273, BOARD_HEIGHT/3, BOARD_WIDTH, BOARD_HEIGHT/3, BOARD_WIDTH, BOARD_HEIGHT/3*2, BOARD_WIDTH-273, BOARD_HEIGHT/3*2, BOARD_WIDTH-273, BOARD_HEIGHT/3);
        root.getChildren().add(grid);
    }

    // FIXME Task 7: Implement a basic playable Steps Game in JavaFX that only allows pieces to be placed in valid places

    // FIXME Task 10: Implement hints

    // FIXME Task 11: Generate interesting starting placements


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("IQ-Steps");
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
        root.getChildren().add(pieces);
        root.getChildren().add(controls);
        root.getChildren().add(stepCountText);
        root.getChildren().add(recordText);
        makeBoard();
        makeControls();
        makeCompletion();
        makeRecord();
        makeGrid();
        hideCompletion();
        resetPieces();

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
