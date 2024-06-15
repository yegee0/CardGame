package cardgame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
    
     GamePanel() {
        JButton myButton = new JButton("My Button");
        JPanel optionPanel = createOptionPanel(myButton);
        add(optionPanel); 
    }
	
	public static JPanel createOptionPanel(JButton button) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension arcs = new Dimension(13, 13); // roundness 
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g;
                graphics.setColor(getBackground());
                graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height); // paint background
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.add(button, BorderLayout.CENTER);
        return panel;
    }
	
	public static void customizeButton(JButton button) {
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Align text
        button.setFocusPainted(false);
    }
}
