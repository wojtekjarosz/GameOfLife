package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import java.util.Random;
import static java.lang.Thread.sleep;

public class Controller {
    int width = 100;
    int height = 50;
    int cellSize=(1200)/((width>height)? width : height);
    Board board;

    @FXML
    CheckBox checkbox;
    @FXML
    Canvas canvas;
    @FXML
    AnchorPane anchorPaneLeft;
    @FXML
    AnchorPane anchorPaneRight;
    @FXML
    TextField textField;
    GraphicsContext gc;
    Random rand = new Random();
    Thread thread;
    Color cellColor = Color.SANDYBROWN;
    Color backgroundColor = Color.LIGHTYELLOW;
    private volatile boolean running = true;
    @FXML
    public void initialize() {
        board =new Board(width, height);
        gc = canvas.getGraphicsContext2D();
        drawLines();
        gc.setFill(cellColor);
        canvas.setOnMouseClicked(event -> {
            try {
                double x=event.getSceneX();
                double y=event.getSceneY();
                System.out.println("Clicked on: "+ x + ", " + y);

                int cell_x =(int) x*width/(width*cellSize);
                int cell_y =(int) y*height/(height*cellSize);
                System.out.println("Cell nr: "+ cell_x + ", " + cell_y);
                if(!board.getCellValue(cell_x,cell_y)) {
                    board.setCellValue(cell_x, cell_y, true);
                    gc.setFill(cellColor);
                    gc.fillRect(x + 1 - (x % cellSize), y + 1 - (y % cellSize), cellSize-2, cellSize-2);
                }else{
                    board.setCellValue(cell_x, cell_y, false);
                    gc.setFill(backgroundColor);
                    gc.fillRect(x + 1 - (x % cellSize), y + 1 - (y % cellSize), cellSize-2, cellSize-2);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        checkbox.setSelected(true);
        textField.setText(width*height*3/5 +"");
        /*canvas.setWidth(width*cellSize);
        canvas.setHeight(height*cellSize);*/
        /*anchorPaneLeft.setMinWidth(width*cellSize);
        anchorPaneLeft.setMinHeight(height*cellSize);
        anchorPaneLeft.setMaxWidth(width*cellSize);
        anchorPaneLeft.setMaxHeight(height*cellSize);*/
        anchorPaneLeft.prefWidth(width*cellSize);
    }

    @FXML
    public void handleStart() {
        running = true;
        thread = new Thread(() -> {
            while (running) {
                Platform.runLater(() -> startFunction());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void startFunction(){
        board.setPeriod(checkbox.isSelected());
        board.nextCycle();
        drawBoard();
        board.printBoard();
        /*try {
               sleep(100);
        } catch (InterruptedException e) {
              e.printStackTrace();
        }*/
    }

    @FXML
    public void handleStop() {
        running = false;
        thread.interrupt();
    }

    @FXML
    public void handleClear(){
        for(int i=0; i<width; i++){
            for(int j=0;j<height;j++){
                board.setCellValue(i,j,false);
                gc.setFill(backgroundColor);
                gc.fillRect(i*cellSize+1,j*cellSize+1,cellSize-2,cellSize-2);
            }
        }
    }

    @FXML
    public void handleRand(){
        try {
            int numberOfCells = Integer.parseInt(textField.getText());
            if(numberOfCells>(width*height)){
                numberOfCells = width*height;
                textField.setText(numberOfCells+"");
            }
            for (int i = 0; i < numberOfCells; i++) {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);
                if(!board.getCellValue(x,y)) {
                    board.setCellValue(x, y, true);
                    gc.setFill(cellColor);
                    gc.fillRect(x * cellSize + 1, y * cellSize + 1, cellSize - 2, cellSize - 2);
                }
                else i--;

            }
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Not a number");
            alert.showAndWait();
        }
    }

    public void drawLines(){
        gc.setFill(Color.BLACK);
        for(int i=0; i<(width*cellSize); i++){
            for(int j=0; j<(height*cellSize); j++){
                if((i%cellSize==0 )||(j%cellSize==0))
                gc.fillRect(i,j,1,1);
            }
        }
        drawBoard();
    }
    public void drawBoard(){
        for(int i=0;i<width;i++){
            for (int j=0;j<height;j++){
                if(board.getCellValue(i,j)){
                    System.out.println(i + " "+ j);
                    gc.setFill(cellColor);
                    gc.fillRect(i*cellSize+1,j*cellSize+1,cellSize-2,cellSize-2);
                }else{
                    gc.setFill(backgroundColor);
                    gc.fillRect(i*cellSize+1,j*cellSize+1,cellSize-2,cellSize-2);
                }
            }
        }
    }
}
