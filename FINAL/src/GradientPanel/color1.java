package GradientPanel;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class color1 extends JPanel {

    
  
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        // Define the start and end points of the gradient
        int startX = 0;
        int startY = 0;
        int endX = getWidth();
        int endY = getHeight();
        
        // Define the colors for the gradient
           Color startColor = new Color(60,193,225) ;
        Color endColor = new Color(82,151,227);
        
        // Create the gradient paint object
        GradientPaint gradient = new GradientPaint(startX, startY, startColor, endX, endY, endColor);
        
        // Use the gradient paint to fill a rectangle
        g2d.setPaint(gradient);
        g2d.fill(new Rectangle(startX, startY, endX, endY));
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Gradient Color Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new color1());
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
