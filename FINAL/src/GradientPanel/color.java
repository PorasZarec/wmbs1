package GradientPanel;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class color extends JPanel {
    
    public color() {
        setBackground(Color.WHITE); // Set the background color of the panel
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        // Create a gradient paint
        GradientPaint gradient = new GradientPaint(
                0, 0,new Color(30,47,151), getWidth(), getHeight(), new Color(26,167,236));
        
        g2d.setPaint(gradient); // Set the paint for the graphics context
        
        // Fill the panel with the gradient paint
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Gradient Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(new color());
        frame.setVisible(true);
    }
}
