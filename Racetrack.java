import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Racetrack extends JPanel {
    private static int lanecount;
    private ArrayList<Horse> horses;
    private String lanetype;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int centerX = panelWidth / 2;
        int centerY = panelHeight / 2;
        

        if ("Circle".equals(lanetype)) {
            CircleTrack(g2d, centerX, centerY, panelWidth, panelHeight);
        } else if ("Figure-Eight".equals(lanetype)) {
            FigureEightTrack(g2d, centerX, centerY, panelWidth, panelHeight);
        } else if ("Oval".equals(lanetype)) {
            OvalTrack(g2d, centerX, centerY, panelWidth, panelHeight);
        }



        if ("Circle".equals(lanetype)) {
            for (int i = 0; i < horses.size(); i++) {
                Horse horse = horses.get(i);
                
                double progress = (double) horse.getDistanceTravelled() / MainMenu.gettracklength(); 
                int baseRadius = Math.min(panelWidth, panelHeight) / 3;
                int laneRadius = baseRadius - (i * 20); 
                
                double angle = progress * 2 * Math.PI;
                int horseX = centerX + (int)(laneRadius * Math.cos(angle));
                int horseY = centerY + (int)(laneRadius * Math.sin(angle));
                
                g2d.setFont(new Font("Arial", Font.BOLD, 14)); 
                g2d.drawString(horse.getSymbol() + "", horseX - 4, horseY + 5);       
            }
        } else if ("Figure-Eight".equals(lanetype)) {
            for (int i = 0; i < horses.size(); i++) {
                Horse horse = horses.get(i);

                double progress = (double) horse.getDistanceTravelled() / MainMenu.gettracklength(); 
                double angle = progress * 2 * Math.PI;

                int horseX;
                int horseY = 0;

                // Calculate lane radius and vertical offset for figure-eight
                int baseRadius = Math.min(panelWidth, panelHeight) / 4;
                int laneRadius = baseRadius + (i * 20);
                int verticalOffset = laneRadius;
                
                // Determine the position of the horse based on progress
                if(progress < 0.5) { // Top circle
                    double circleProgress = progress * 2;
                    angle = circleProgress * 2 * Math.PI;
                    horseY -= verticalOffset;
                } else { // Bottom circle 
                    double circleProgress = (progress - 0.5) * 2;
                    angle = (1 - circleProgress) * 2 * Math.PI; 
                    horseY += verticalOffset;
                }
        
                horseX = centerX + (int)(laneRadius * Math.cos(angle));
                horseY += centerY + (int)(laneRadius * Math.sin(angle));
                
                g2d.setFont(new Font("Arial", Font.BOLD, 14)); 
                g2d.drawString(horse.getSymbol() + "", horseX - 4, horseY + 5);       
            }
        }
        else if ("Oval".equals(lanetype)) {
            for (int i = 0; i < horses.size(); i++) {
                Horse horse = horses.get(i);

                double progress = (double) horse.getDistanceTravelled() / MainMenu.gettracklength(); 

                
                // Oval-specific calculations
                int baseWidth = Math.min(panelWidth, panelHeight) * 2 / 3;
                int baseHeight = Math.min(panelWidth, panelHeight) / 3;
                int laneSpacing = 20;

                double angle = progress * 2 * Math.PI;

                // Calculate oval dimensions for  horse lane
                int laneWidth = baseWidth + (i * laneSpacing * 2);
                int laneHeight = baseHeight + (i * laneSpacing);
                
                // Oval positioning 
                int horseX = centerX + (int)((laneWidth/2) * Math.cos(angle));
                int horseY = centerY + (int)((laneHeight/2) * Math.sin(angle));

                g2d.setFont(new Font("Arial", Font.BOLD, 14)); 
                g2d.drawString(horse.getSymbol() + "", horseX - 4, horseY + 5);                          
            } 
        }
    }


    
    public Racetrack(int numLanes, ArrayList<Horse> horseslist, String lt){
        lanecount = numLanes;
        this.horses = horseslist;
        this.lanetype = lt;
    }

    void CircleTrack(Graphics2D g2d, int centerX, int centerY, int panelWidth, int panelHeight) {
        int baseRadius = Math.min(panelWidth, panelHeight) / 3;
        
        for(int i=0; i<lanecount; i++) { 
            int laneRadius = baseRadius - (i * 20);
            
            if(laneRadius <= 0) break;
            
            g2d.drawOval(
                centerX - laneRadius,
                centerY - laneRadius,
                laneRadius * 2, 
                laneRadius * 2
            );
        }
    }

    void OvalTrack(Graphics2D g2d, int centerX, int centerY, int panelWidth, int panelHeight) {
        // Calculate base dimensions 
        int baseWidth = Math.min(panelWidth, panelHeight) * 2 / 3;  
        int baseHeight = Math.min(panelWidth, panelHeight) / 3;
        int laneSpacing = 20;  
    
        // Draw concentric oval lanes
        for(int i = 0; i < lanecount; i++) {  // Start from 0
            // Calculate lane dimensions
            int laneWidth = baseWidth + (i * laneSpacing * 2);
            int laneHeight = baseHeight + (i * laneSpacing);
            
            // Calculate position (centered)
            int topLeftX = centerX - laneWidth / 2;
            int topLeftY = centerY - laneHeight / 2;
            
            // Draw the oval lane
            g2d.drawOval(topLeftX, topLeftY, laneWidth, laneHeight);
        }
    }

    void FigureEightTrack(Graphics2D g2d, int centerX, int centerY, int panelWidth, int panelHeight) {
        //Calculate base dimensions
        int baseRadius = Math.min(panelWidth, panelHeight) / 4; 
        int laneSpacing = 20;
        
        //Draw figure-eight lanes
        for(int i = 0; i < lanecount; i++) {
            // Calculate lane radius
            int laneRadius = baseRadius + (i * laneSpacing);
            
            //Calculate vertical offset for loops
            int verticalOffset = laneRadius; 
            
            // Draw top circle
            g2d.drawOval(
                centerX - laneRadius,
                centerY - laneRadius - verticalOffset,
                laneRadius * 2,
                laneRadius * 2
            );
            
            // Draw bottom circle
            g2d.drawOval(
                centerX - laneRadius,
                centerY - laneRadius + verticalOffset,
                laneRadius * 2,
                laneRadius * 2
            );
        }
    }
}


