/**
 * Deals with Best Times Stats of the tracks
 *
 * @AssiaOuaoua 
 * @27/04/2025
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Stats {
    public static ArrayList<Double> bestTimesCircle = new ArrayList<>();
    public static ArrayList<Double> bestTimesFigureEight = new ArrayList<>();
    public static ArrayList<Double> bestTimesOval = new ArrayList<>();
    private static JFrame mainFrame;

    //Main Stats method to load stats from CSV and create the GUI
    /**
     * Main method to load stats from CSV and create the GUI
     * @param args command line arguments
     */
    public static void StatsMain(String[] args) {
        loadStatsFromCSV();
        if (mainFrame == null) {
            mainFrame = new JFrame("Horse Management System");
            mainFrame.setSize(600, 600);
            mainFrame.setLocationRelativeTo(null); 

            JPanel mainPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            mainFrame.add(mainPanel);

            // Title
            JLabel titleLabel = new JLabel("Best Stats", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            mainPanel.add(titleLabel, gbc);

            //Best Times 
            JButton bestTimeButton = new JButton("Best Times");
            bestTimeButton.addActionListener(e -> displayBestTimes());
            gbc.gridy = 1;
            mainPanel.add(bestTimeButton, gbc);
            gbc.gridy = 2;

            //Back to Main Menu 
            JButton backButton = new JButton("Back to Main Menu");
            backButton.addActionListener(e -> {
                mainFrame.dispose();
                mainFrame = null;
            });
            
            gbc.gridy = 3;
            mainPanel.add(backButton, gbc);

            mainFrame.setVisible(true);
        }

    }

    //Method to display the best times in a new window
    public static void displayBestTimes() {
        JFrame frame = new JFrame("Best Times");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.gridx  = 0;
        gbc.anchor = GridBagConstraints.CENTER;
    
        // Title
        JLabel title = new JLabel("Best Times", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy    = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);
    
        String circleText = "";
        for (int i = 0; i < bestTimesCircle.size(); i++) {
            circleText += bestTimesCircle.get(i);
            if (i < bestTimesCircle.size() - 1) {
                circleText += ", ";
            }
        }
    
        String fig8Text = "";
        for (int i = 0; i < bestTimesFigureEight.size(); i++) {
            fig8Text += bestTimesFigureEight.get(i);
            if (i < bestTimesFigureEight.size() - 1) {
                fig8Text += ", ";
            }
        }
    
        String ovalText = "";
        for (int i = 0; i < bestTimesOval.size(); i++) {
            ovalText += bestTimesOval.get(i);
            if (i < bestTimesOval.size() - 1) {
                ovalText += ", ";
            }
        }
    
        gbc.gridwidth = 1;
        gbc.gridy     = 1;
        gbc.gridx     = 0;
        panel.add(new JLabel("Circle:"), gbc);
    
        JTextField circleField = new JTextField(circleText, 20);
        circleField.setEditable(false);
        gbc.gridx = 1;
        panel.add(circleField, gbc);
    
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Figure Eight:"), gbc);
    
        JTextField fig8Field = new JTextField(fig8Text, 20);
        fig8Field.setEditable(false);
        gbc.gridx = 1;
        panel.add(fig8Field, gbc);
    
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Oval:"), gbc);
    
        JTextField ovalField = new JTextField(ovalText, 20);
        ovalField.setEditable(false);
        gbc.gridx = 1;
        panel.add(ovalField, gbc);
    
        frame.add(panel);
        frame.setVisible(true);
    }
    

    //Method put the best times in the right track
    public static void racetracksort(double raceTime){
        if(MainMenu.getlanetype().equals("Circle")){
            bestTimesCircle = getBestTimes(bestTimesCircle, raceTime);         
        }
        else if(MainMenu.getlanetype().equals("Figure Eight")){
            bestTimesFigureEight = getBestTimes(bestTimesFigureEight, raceTime);
        }
        else if(MainMenu.getlanetype().equals("Oval")){
            bestTimesOval = getBestTimes(bestTimesOval, raceTime);
        }
        saveStatsToCSV();
    }

    //GETTERS
    public static ArrayList<Double> getcircletimes(){
        return bestTimesCircle;
    }

    public static ArrayList<Double> getfigureeighttimes(){
        return bestTimesFigureEight;
    }

    public static ArrayList<Double> getovaltimes(){
        return bestTimesOval;
    }
    
    public static ArrayList<Double> getBestTimes(ArrayList<Double> bestTimes, double newtime){
        int i = 0;
        while (i < bestTimes.size() && bestTimes.get(i) <= newtime) {
            i++;
        }
        bestTimes.add(i, newtime);
        return bestTimes;
    }

    //Method to save the best times to a CSV file
    //The file will be named "stats.csv" and will be created in the current working directory.
    public static void saveStatsToCSV() {
        try {
            FileWriter writer = new FileWriter("stats.csv");
            writer.write("CircleTimes,FigureEightTimes,OvalTimes\n");

            String circleCell = "";
            for (int i = 0; i < bestTimesCircle.size(); i++) {
                circleCell += bestTimesCircle.get(i);
                if (i < bestTimesCircle.size() - 1) {
                    circleCell += ";";
                }
            }

            String fig8Cell = "";
            for (int i = 0; i < bestTimesFigureEight.size(); i++) {
                fig8Cell += bestTimesFigureEight.get(i);
                if (i < bestTimesFigureEight.size() - 1) {
                    fig8Cell += ";";
                }
            }

            String ovalCell = "";
            for (int i = 0; i < bestTimesOval.size(); i++) {
                ovalCell += bestTimesOval.get(i);
                if (i < bestTimesOval.size() - 1) {
                    ovalCell += ";";
                }
            }

            writer.write(circleCell + "," + fig8Cell + "," + ovalCell + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to load the best times from a CSV file
    //The file will be named "stats.csv" and will be created in the current working directory.
    public static void loadStatsFromCSV() {
        bestTimesCircle.clear();
        bestTimesFigureEight.clear();
        bestTimesOval.clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("stats.csv"));
            reader.readLine();
            String line = reader.readLine();
            if (line != null) {
                String[] cols = line.split(",", -1);

                if (!cols[0].isEmpty()) {
                    String[] parts = cols[0].split(";");
                    for (String s : parts) {
                        bestTimesCircle.add(Double.parseDouble(s));
                    }
                }
                if (cols.length > 1 && !cols[1].isEmpty()) {
                    String[] parts = cols[1].split(";");
                    for (String s : parts) {
                        bestTimesFigureEight.add(Double.parseDouble(s));
                    }
                }
                if (cols.length > 2 && !cols[2].isEmpty()) {
                    String[] parts = cols[2].split(";");
                    for (String s : parts) {
                        bestTimesOval.add(Double.parseDouble(s));
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

