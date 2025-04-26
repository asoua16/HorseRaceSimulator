import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class HorseManage {

    private static JComboBox<String> breedtypesBox;
    private static JComboBox<String> colorsBox;
    private static JComboBox<String> horseshoeBox;
    private static ArrayList<Horse> horses = new ArrayList<>();
    private static JFrame mainFrame;
    private static JPanel listPanel;

    public static JComboBox<String> breedtypes() {
        String[] breedStrings = {"Quarter Horse", "Arabian", "Thoroughbred"};
        breedtypesBox = new JComboBox<>(breedStrings);
        return breedtypesBox;
    }

    public static JComboBox<String> colorstypes(){
        String[] colorStrings = {"Bay", "Chestnut", "Black", "Grey"};
        colorsBox = new JComboBox<>(colorStrings);
        return colorsBox;
    }

    public static JComboBox<String> horseshoetypes(){
        String[] horseshoeStrings = {"Regular", "Lightweight", "Iron"};
        horseshoeBox = new JComboBox<>(horseshoeStrings);
        return horseshoeBox;
    }

    // The add-horse window
    public static void addhorse(){
        JFrame frame = new JFrame("Add Horse");
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null); // Center on screen

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets   = new Insets(10, 10, 10, 10);
        gbc.fill     = GridBagConstraints.HORIZONTAL;
        gbc.gridx    = 0;
        gbc.anchor   = GridBagConstraints.CENTER;

        // Title
        JLabel titleLabel = new JLabel("Add Horse", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx    = 0;
        gbc.gridy    = 0;
        gbc.gridwidth = 2; 
        panel.add(titleLabel, gbc);

        // Horse Name
        gbc.gridwidth = 1;
        gbc.gridy    = 1;
        gbc.gridx    = 0;
        panel.add(new JLabel("Horse Name:"), gbc);
        JTextField horseName = new JTextField(20);
        gbc.gridx    = 1;
        panel.add(horseName, gbc);

        // Horse Symbol
        gbc.gridy    = 2;
        gbc.gridx    = 0;
        panel.add(new JLabel("Horse Symbol:"), gbc);
        JTextField horseSymbol = new JTextField(20);
        gbc.gridx    = 1;
        panel.add(horseSymbol, gbc);

        // Breeds
        gbc.gridx    = 0;
        gbc.gridy    = 3;
        panel.add(new JLabel("Breed:"), gbc);
        gbc.gridx    = 1;
        panel.add(breedtypes(), gbc);

        // Color
        gbc.gridx    = 0;
        gbc.gridy    = 4;
        panel.add(new JLabel("Color:"), gbc);
        gbc.gridx    = 1;
        panel.add(colorstypes(), gbc);

        // Accessories Section
        gbc.gridx    = 0;
        gbc.gridy    = 5;
        panel.add(new JLabel("Accessories:"), gbc);
        gbc.gridx    = 1;
        JCheckBox accessoriesCheckbox = new JCheckBox("Accessories");
        panel.add(accessoriesCheckbox, gbc);

        // Create accessory components 
        JLabel horseShoeLabel   = new JLabel("Horse Shoe:");
        JComboBox<String> horseShoeBox = horseshoetypes(); 
        JCheckBox saddleCheckbox  = new JCheckBox("Saddle");
        JCheckBox hatCheckbox     = new JCheckBox("Hat");
        JCheckBox bridleCheckbox  = new JCheckBox("Bridle");

        accessoriesCheckbox.addActionListener(e -> {
            if (accessoriesCheckbox.isSelected()) {
                // Add accessory components
                gbc.gridx = 0;
                gbc.gridy = 6;
                panel.add(horseShoeLabel, gbc);

                gbc.gridx = 1;
                panel.add(horseShoeBox, gbc);

                gbc.gridx = 0;
                gbc.gridy = 7;
                panel.add(saddleCheckbox, gbc);

                gbc.gridx = 0;
                gbc.gridy = 8;
                panel.add(hatCheckbox, gbc);

                gbc.gridx = 0;
                gbc.gridy = 9;
                panel.add(bridleCheckbox, gbc);
            } else {
                // Remove accessory components
                panel.remove(horseShoeLabel);
                panel.remove(horseShoeBox);
                panel.remove(saddleCheckbox);
                panel.remove(hatCheckbox);
                panel.remove(bridleCheckbox);
            }
            panel.revalidate();
            panel.repaint();
        });

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name       = horseName.getText().trim();
            String symbolText = horseSymbol.getText().trim();
            if (name.isEmpty() || symbolText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }
            char symbol     = symbolText.charAt(0);
            String breed    = (String) breedtypesBox.getSelectedItem();
            String color    = (String) colorsBox.getSelectedItem();
            String horseshoe = (String) horseshoeBox.getSelectedItem();
            boolean hasAccessories = accessoriesCheckbox.isSelected();
            boolean hasSaddle      = saddleCheckbox.isSelected();
            boolean hasHat         = hatCheckbox.isSelected();
            boolean hasBridle      = bridleCheckbox.isSelected();

            ArrayList<String> accessories = new ArrayList<>();
            Horse newHorse;
            if (hasAccessories) {
                if (hasSaddle)  accessories.add("Saddle");
                if (hasHat)     accessories.add("Hat");
                if (hasBridle)  accessories.add("Bridle");
                newHorse = new Horse(symbol, name, breed, color, horseshoe, accessories);
            } else {
                newHorse = new Horse(symbol, name, breed, color);
            }

            if (horses.contains(newHorse)) {
                JOptionPane.showMessageDialog(frame, "Horse already exists!");
            } else {
                horses.add(newHorse);
                JOptionPane.showMessageDialog(frame, "Horse added successfully!");
            }
            frame.dispose(); 
            updateHorseList();
            saveHorsesToCSV();

        });
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        panel.add(submitButton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    //Horse Management window
    public static void HorseMain(String[] args) {
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

            // Title
            JLabel titleLabel = new JLabel("Horse Management", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            mainPanel.add(titleLabel, gbc);

            // Add Horse Button
            JButton addHorseButton = new JButton("Add Horse");
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            mainPanel.add(addHorseButton, gbc);
            addHorseButton.addActionListener(e -> addhorse());

            // Panel to hold horse buttons
            listPanel = new JPanel(new GridBagLayout());
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            mainPanel.add(listPanel, gbc);

            mainFrame.add(new JScrollPane(mainPanel));
            mainFrame.setVisible(true);

            JButton backButton = new JButton("Back to Main Menu");
            backButton.addActionListener(e -> {
                mainFrame.dispose();
            });
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            mainPanel.add(backButton, gbc);
            loadHorsesFromCSV();
        }
        updateHorseList();
    }

    public static ArrayList<Horse> getHorses() {
        return horses;
    }

    private static void updateHorseList(){
        listPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets   = new Insets(10, 10, 10, 10);
        gbc.fill     = GridBagConstraints.HORIZONTAL;
        gbc.gridx    = 0;
        gbc.anchor   = GridBagConstraints.CENTER;
        int row = 0;
        for (Horse horse : horses) {
            JButton horseButton = new JButton(horse.getName() + " (" + horse.getSymbol() + ")");
            gbc.gridy = row++;
            horseButton.addActionListener(e -> {
                System.out.println("Clicked: " + horse.getName());
                displayHorse(horse);
            });
            listPanel.add(horseButton, gbc);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    public static void displayHorse(Horse horse) {
        JFrame horseFrame = new JFrame("Horse Details");
        horseFrame.setSize(400, 300);
        horseFrame.setLocationRelativeTo(null); // Center on screen

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets   = new Insets(10, 10, 10, 10);
        gbc.fill     = GridBagConstraints.HORIZONTAL;
        gbc.gridx    = 0;
        gbc.anchor   = GridBagConstraints.CENTER;

        // Title
        JLabel titleLabel = new JLabel("Horse Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy    = 0;
        gbc.gridwidth = 2; 
        panel.add(titleLabel, gbc);

        // Horse Name
        gbc.gridwidth = 1;
        gbc.gridy    = 1;
        gbc.gridx    = 0;
        panel.add(new JLabel("Horse Name:"), gbc);
        JTextField horseNameField = new JTextField(horse.getName(), 20);
        horseNameField.setEditable(false);
        gbc.gridx    = 1;
        panel.add(horseNameField, gbc);

        // Horse Symbol
        gbc.gridy    = 2;
        gbc.gridx    = 0;
        panel.add(new JLabel("Horse Symbol:"), gbc);
        JTextField horseSymbolField = new JTextField(String.valueOf(horse.getSymbol()), 20);
        horseSymbolField.setEditable(false);
        gbc.gridx    = 1;
        panel.add(horseSymbolField, gbc);

        // Breed
        gbc.gridy    = 3;
        gbc.gridx    = 0;
        panel.add(new JLabel("Breed:"), gbc);
        JTextField breedField = new JTextField(horse.getBreed(), 20);
        breedField.setEditable(false);
        gbc.gridx    = 1;
        panel.add(breedField, gbc);

        // Color
        gbc.gridy    = 4;
        gbc.gridx    = 0;
        panel.add(new JLabel("Color:"), gbc);
        JTextField colorField = new JTextField(horse.getColor(), 20);
        colorField.setEditable(false);
        gbc.gridx    = 1;
        panel.add(colorField, gbc);

        horseFrame.add(panel);
        horseFrame.setVisible(true);

        //Horse Speed Average
        gbc.gridy = 5;
        gbc.gridx = 0;
        panel.add(new JLabel("Horse Speed Average:"), gbc);
        JTextField horseSpeedField = new JTextField(String.valueOf(horse.getaverageSpeed()), 20);
        horseSpeedField.setEditable(false);
        gbc.gridx = 1;
        panel.add(horseSpeedField, gbc);
        if (horse.getaverageSpeed() > 0) {
            horseSpeedField.setBackground(Color.GREEN);
        } else {
            horseSpeedField.setBackground(Color.RED);
        }
        horseSpeedField.setOpaque(true); 

    }

    public static void saveHorsesToCSV() {
        try {
            FileWriter writer = new FileWriter("horses.csv");
            writer.write(
                "Name,Symbol,Breed,CoatColor,Horseshoe,Accessories," +
                "Confidence,HorseSpeed,SpeedHistory," +
                "Wins,Races,ConfidenceHistory\n"
            );
    
            for (Horse h : horses) {
                String accCell = "";
                for (int i = 0; i < h.horseAccessories.size(); i++) {
                    accCell += h.horseAccessories.get(i);
                    if (i < h.horseAccessories.size() - 1) {
                        accCell += ";";
                    }
                }
    
                String speedHist = "";
                for (int i = 0; i < h.horseSpeed.size(); i++) {
                    speedHist += h.horseSpeed.get(i);
                    if (i < h.horseSpeed.size() - 1) {
                        speedHist += ";";
                    }
                }
    
                String confHist = "";
                for (int i = 0; i < h.horseConfidenceList.size(); i++) {
                    confHist += h.horseConfidenceList.get(i);
                    if (i < h.horseConfidenceList.size() - 1) {
                        confHist += ";";
                    }
                }
    
                String line =
                    h.horseName + "," +
                    h.horseSymbol + "," +
                    h.horseBreed + "," +
                    h.horseCoatcolor + "," +
                    (h.horseShoe == null ? "" : h.horseShoe) + "," +
                    accCell + "," +
                    h.horseConfidence + "," +
                    h.horsespeed + "," +
                    speedHist + "," +
                    h.wins + "," +
                    h.races + "," +
                    confHist + "\n";
    
                writer.write(line);
            }
    
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void loadHorsesFromCSV() {
        horses.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("horses.csv"));
            reader.readLine(); // skip the header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] f = line.split(",", -1);
    
                String name = f[0];
                char symbol = f[1].isEmpty() ? '?' : f[1].charAt(0);
                String breed = f[2];
                String coatColor = f[3];
                String shoe = f[4];
    
                ArrayList<String> accessories = new ArrayList<>();
                if (!f[5].isEmpty()) {
                    String[] parts = f[5].split(";");
                    for (String s : parts) {
                        accessories.add(s);
                    }
                }
    
                double confidence = Double.parseDouble(f[6]);
                double speed = Double.parseDouble(f[7]);
    
                ArrayList<Double> speedHist = new ArrayList<>();
                if (!f[8].isEmpty()) {
                    String[] sp = f[8].split(";");
                    for (String s : sp) {
                        speedHist.add(Double.parseDouble(s));
                    }
                }
    
                int wins = Integer.parseInt(f[9]);
                int races = Integer.parseInt(f[10]);
    
                ArrayList<Double> confHist = new ArrayList<>();
                if (f.length > 11 && !f[11].isEmpty()) {
                    String[] ch = f[11].split(";");
                    for (String s : ch) {
                        confHist.add(Double.parseDouble(s));
                    }
                }
    
                // create horse and set fields
                Horse h = new Horse(symbol, name, breed, coatColor);
                h.horseShoe = shoe;
                h.horseAccessories = accessories;
                h.horseConfidence = confidence;
                h.horsespeed = speed;
                h.horseSpeed = speedHist;
                h.wins = wins;
                h.races = races;
                h.horseConfidenceList = confHist;
    
                horses.add(h);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
}   
