import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HorseManage {

    private static JComboBox<String> breedtypesBox;
    private static JComboBox<String> colorsBox;
    private static JComboBox<String> horseshoeBox;
    private static ArrayList<Horse> horses = new ArrayList<>();

    // Static references to the main frame and the panel holding horse buttons
    private static JFrame mainFrame;
    private static JPanel listPanel;

    // Combo box factories
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
        gbc.gridwidth = 2; // span two columns
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

        // Create accessory components outside the listener so we can remove them later
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

            // Check for duplicates BEFORE adding
            if (horses.contains(newHorse)) {
                JOptionPane.showMessageDialog(frame, "Horse already exists!");
            } else {
                horses.add(newHorse);
                JOptionPane.showMessageDialog(frame, "Horse added successfully!");
            }
            frame.dispose(); // Close the add-horse window

            // Update the main Horse Management list
            updateHorseList();
        });
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        panel.add(submitButton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    // The main Horse Management window
    public static void HorseMain(String[] args) {
        if (mainFrame == null) {
            mainFrame = new JFrame("Horse Management System");
            mainFrame.setSize(600, 600);
            mainFrame.setLocationRelativeTo(null); // Center on screen

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

            // Panel to hold horse buttons (list)
            listPanel = new JPanel(new GridBagLayout());
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            mainPanel.add(listPanel, gbc);

            mainFrame.add(new JScrollPane(mainPanel));
            mainFrame.setVisible(true);

            JButton backButton = new JButton("Back to Main Menu");
            backButton.addActionListener(e -> {
                mainFrame.dispose(); // Close the Horse Management window
                MainMenu.main(null); // Open the main menu again
            });
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            mainPanel.add(backButton, gbc);


        }
        // Call update to reflect the latest horse list
        updateHorseList();
    }

    // Rebuilds the panel that displays the buttons for each horse
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
                // Handle button click (e.g. show details, edit, etc.)
                System.out.println("Clicked: " + horse.getName());
            });
            listPanel.add(horseButton, gbc);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }
}
