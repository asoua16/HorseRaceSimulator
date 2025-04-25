import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

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
                mainFrame.setVisible(false);
                MainMenu.main(null);  
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
            });
            listPanel.add(horseButton, gbc);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    public static void saveHorsesToCSV() {
        try (FileWriter writer = new FileWriter("horses.csv")) {
            writer.append("Name,Symbol,Breed,Color,Horseshoe,Accessories\n");
            
            for (Horse horse : horses) {
                writer.append(horse.getName()).append(",")
                    .append(String.valueOf(horse.getSymbol())).append(",")
                    .append(horse.getBreed()).append(",")
                    .append(horse.getColor()).append(",")
                    .append(horse.getHorseshoe()).append(",")
                    .append(String.join(";", horse.getAccessories())) 
                    .append("\n");
            }
            System.out.println("Horses saved to CSV file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadHorsesFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("horses.csv"))) {
            String line;
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String name = data[0];
                    char symbol = data[1].charAt(0);
                    String breed = data[2];
                    String color = data[3];
                    String horseshoe = data[4];
                    String[] accessories = data[5].split(";");

                    Horse horse = new Horse(symbol, name, breed, color, horseshoe, new ArrayList<>(List.of(accessories)));
                    horses.add(horse);
                }
            }
            System.out.println("Horses loaded from CSV file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
