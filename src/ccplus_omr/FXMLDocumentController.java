/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccplus_omr;

import static java.lang.Math.abs;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 *
 * @author Acer
 */
public class FXMLDocumentController implements Initializable {
    
    
    @FXML
    private ImageView imageView;
    
    @FXML
    private Region colorchecker;
    
    @FXML
    private TextField tfFilas;
    
    @FXML
    private TextField tfColumnas;
    
    @FXML
    private TextField tfPrimer;
    
    @FXML
    private TextField tfSegunda;
    
    private Image image2;
    
    private int counter;
    
    private int[] primerC = new int[2];
    private int[] segundaC = new int[2];
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        imageView.setPreserveRatio(true);
        image2 = new Image("file:C:\\Users\\Acer\\Documents\\ccplus\\OMR\\test2.jpg");
        imageView.setImage(image2);
                
    } 
    
    
    @FXML
    private void handleImageViewOnMouseEnter(Event event) {
        imageView.getScene().setCursor(Cursor.CROSSHAIR);
    }
    
    @FXML
    private void handleImageViewOnMouseExit(Event event) {
        imageView.getScene().setCursor(Cursor.DEFAULT);
    }
    
    @FXML
    private void handleImageViewAction(MouseEvent event) {
        
        if(counter==0){

            double cursor_width = event.getX();
            double cursor_height = event.getY();

            double node_height = imageView.getBoundsInParent().getHeight();
            double node_width = imageView.getBoundsInParent().getWidth();

            double image_height = image2.getHeight();
            double image_width = image2.getWidth();

            double d1 = (cursor_width/node_width)*image_width;
            double d2 = (cursor_height/node_height)*image_height;

            int i1 = (int) d1;
            int i2 = (int) d2;

            PixelReader pr = image2.getPixelReader();
            Color color = pr.getColor(i1, i2);

            colorchecker.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            
            primerC[0] = i1;
            primerC[1] = i2;
            
            tfPrimer.setText(i1 + ", " + i2);
            
        }else if(counter==1){
            
            double cursor_width = event.getX();
            double cursor_height = event.getY();

            double node_height = imageView.getBoundsInParent().getHeight();
            double node_width = imageView.getBoundsInParent().getWidth();

            double image_height = image2.getHeight();
            double image_width = image2.getWidth();

            double d1 = (cursor_width/node_width)*image_width;
            double d2 = (cursor_height/node_height)*image_height;

            int i1 = (int) d1;
            int i2 = (int) d2;

            PixelReader pr = image2.getPixelReader();
            Color color = pr.getColor(i1, i2);

            colorchecker.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            
            segundaC[0] = i1;
            segundaC[1] = i2;
            
            tfSegunda.setText(i1 + ", " + i2);
            
            int distancia_x = segundaC[0] - primerC[0];
            int distancia_y = segundaC[1] - primerC[1];
            
            int read_filas = 0;
            int read_columnas = 0;
            
            read_filas = Integer.parseInt(tfFilas.getText());
            read_columnas = Integer.parseInt(tfColumnas.getText());
            
            int filas = read_filas;
            int columnas = read_columnas;
            
            for (int i = 0; i < filas; i++) {
                
                for (int j = 0; j < columnas; j++) {
                    
                    double g1 = (double) distancia_x / (double) (columnas-1);
                    double g2 = (double) distancia_y / (double) (filas-1);
                    
                    double dif_x = j * g1;
                    double dif_y = i * g2;
                    
                    double a = primerC[0] + dif_x;
                    double b = primerC[1] + dif_y;
                    
//                    pr = image2.getPixelReader();
//                    color = pr.getColor((int) a, (int) b);
//                    double total = color.getBlue() + color.getGreen() + color.getRed();

                    double total = getMeanRGB((int)a, (int)b, 33);
                    int t = (int) (total+0.5);
                    
                    System.out.print(t + " - ");
                    
//                    System.out.print("(" + a + ", " + b + ") - ");

                }
                
                System.out.println("");
                
            }
            
        }
        
        counter++;
        
    }
    
    private double getMeanRGB(int x, int y, int radius){
        
        double total = 0;
        double c = 0;
        
        int initial_x = x - radius;
        int initial_y = y - radius; //agregar caso de initial_y < 0
        
        
        PixelReader pr1 = image2.getPixelReader();
        
        int a = 0;
        int b = 0;
        int distance_x = 0;
        int distance_y = 0;
        Color color;
        
        for (int i = 0; i < ((radius*2) + 1); i++) {
            
            for (int j = 0; j < ((radius*2) + 1); j++) {
                
                a = initial_x + i;
                b = initial_y + j;
                
                if(isInsideRadius(a, b, x, y, radius)){
                    c = c + 1.0;
                    color = pr1.getColor(a, b);
                    total = total + ((color.getBlue() + color.getGreen() + color.getRed())/3.0);
                }
                
            }
            
        }
        
        total = total/c;
        
        return total;
    }
    
    private boolean isInsideRadius(int pos_x, int pos_y, int center_x, int center_y, int radius){
        
        int distance_x = abs(pos_x-center_x);
        int distance_y = abs(pos_y-center_y);
        
        double distance = Math.sqrt(Math.pow(distance_x, 2) + Math.pow(distance_y, 2));
        
        if(distance<=(double)radius) return true;
        
        return false;
    }
       
    
}
