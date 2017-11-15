package comp1110.ass2.gui;

import java.awt.Graphics2D;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A very simple viewer for piece placements in the steps game.
 *
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int PIECE_IMAGE_SIZE = (int) ((3*SQUARE_SIZE)*1.33);
    private static final int VIEWER_WIDTH = 1000;
    private static final int VIEWER_HEIGHT = 800;
    private static final int MARGIN_X = 70;
    private static final int PEG_RADIUS = 30;
    private static final float PIECE_OPACITY = 0.9f;

    private static final String URI_BASE = "assets/";

    private static final String LOOP_URI = Viewer.class.getResource(URI_BASE ).toString();

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group pieces = new Group();
    private final Group solution = new Group();


    private FXPiece[] piece = new FXPiece[8];

    TextField textField;

    /**
     * The visible piece class
     * @author: Weizhangxian WANG (u:6033260)
     */

    class FXPiece extends ImageView {
        char piece;
        char direction = 'A';
        int pegPosX=0;
        int pegPosY=0;
        Image img;

        /* create the which of the eight shapes is being placed */
        FXPiece(char piece){
            this.piece = piece;
            if (!(piece >= 'A' && piece <='H')){
                throw new IllegalArgumentException("Bad piece: \"" + piece + "\"");
            }
            img = new Image(Viewer.class.getResource(URI_BASE + piece + this.direction+ ".png").toString());
            setImage(img);
            setFitHeight(PIECE_IMAGE_SIZE);
            setFitWidth(PIECE_IMAGE_SIZE);
            setOpacity(PIECE_OPACITY);
        }

        /* create the which of the eight shapes is being placed and which orientation the piece is in */
        FXPiece(char piece, char position){
            this(piece);
            if(position < 'A' || position >'H'){
                throw new IllegalArgumentException("Bad position string: " + position);
            }
            position -= 'A';
            int x = position % 4;
            int y = position / 4;
            setLayoutX(x * PIECE_IMAGE_SIZE);
            setLayoutY(y * 2 * PIECE_IMAGE_SIZE);
        }

        /* create the which of the eight shapes is being placed and which orientation the piece is in and the location of piece is to be located on  */
        FXPiece(char piece, char direction, char position){
            this.piece = piece;
            this.direction = direction;
            if (!(piece >= 'A' && piece <='H')){
                throw new IllegalArgumentException("Bad piece: \"" + piece + "\"");
            }

            img = new Image(Viewer.class.getResource(URI_BASE + piece + direction+".png").toString());
            setImage(img);

            setFitHeight(PIECE_IMAGE_SIZE);
            setFitWidth(PIECE_IMAGE_SIZE);
            setOpacity(PIECE_OPACITY);

            if(position>='A' && position <='Y'){
                pegPosX = (position-'A')%10; //number of rows
                pegPosY = (position-'A')/5; //number of columns
            }else{
                pegPosX = (position-'a'+25)%10;
                pegPosY = (position-'a'+25)/5;
            }

            setLayoutX(pegPosX * PEG_RADIUS);
            setLayoutY(pegPosY * (PEG_RADIUS)+100);
        }

    }

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement  A valid placement string
     *
     * @author: Weizhangxian WANG (u:6033260)
     */

    void makePlacement(String placement) {
        //Scene scene = new Scene(root,VIEWER_WIDTH,VIEWER_HEIGHT);
        // FIXME Task 4: implement the simple placement viewer

        pieces.getChildren().clear();
        for (int i=0;i<placement.length();i=i+3){
            pieces.getChildren().add(new FXPiece(placement.charAt(i), placement.charAt(i+1), placement.charAt(i+2)));
        }
    }

    /* clear the panel and then make pieces */
    private void makePieces(){
        pieces.getChildren().clear();
        for (char p = 'A'; p <= 'H'; p++){
            piece[p-'A'] = new FXPiece(p, p);
            pieces.getChildren().add(piece[p-'A']);
        }

    }


    private void newGame(){
        makePieces();
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("StepsGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
        root.getChildren().add(pieces);
        root.getChildren().add(solution);

        makeControls();

        newGame();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
