/**
 * Deals with Betting System
 *
 * @AssiaOuaoua 
 * @27/04/2025
 */
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Betting {

    private static JFrame mainFrame;
    private static double virtualBalance = 1000.0;
    private static ArrayList<Bet> betHistory = new ArrayList<>();

    public static class Bet {
        String horseName;
        double amount;
        double odds;
        boolean win;       
        double payout;     
        boolean resolved;  

        public Bet(String horseName, double amount, double odds) {
            this.horseName = horseName;
            this.amount = amount;
            this.odds = odds;
            this.win = false;       
            this.payout = 0.0;      
            this.resolved = false;  
        }

        // Method to resolve the bet based on horse outcome
        @Override
        public String toString() {
            // If not resolved, show "Pending"
            String result = resolved ? (win ? "Won" : "Lost") : "Pending";
            return "Horse: " + horseName + " | Bet: " + amount +
                   " | Odds: " + odds + " | " + result +
                   " | Payout: " + payout;
        }
    }


    // Method to calculate odds based on horse performance and track condition
    public static double calculateOdds(Horse h, String trackCondition) {
        double baseOdds = 5.0;
        double winRatio = h.getwinRatio();               
        double avgSpeed = h.getaverageSpeed();             
        double avgConfidence = h.getaverageConfidence();

        double performanceFactor = (winRatio * 2) + (avgSpeed / 20) + (avgConfidence);
        double odds = baseOdds - performanceFactor;

        if (trackCondition.equals("Muddy")) {
            odds -= 0.5;
        }
        else if (trackCondition.equals("Icy")) {
            odds -= 1.0;
        } else if (trackCondition.equals("Dry")) {
            odds += 1.5;
        }
        if (odds < 1.0) {
            odds = 1.0;
        }
        odds = Math.round(odds * 100.0) / 100.0;
        return odds;
    }

    // Method to load horses from CSV file
    public static ArrayList<Horse> loadHorsesFromCSV() {
        ArrayList<Horse> horses = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("horses.csv"));
            reader.readLine(); 
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
                double basespeed = Double.parseDouble(f[7]);
    
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
                h.basespeed = basespeed;
                h.horseSpeedList = speedHist;
                h.wins = wins;
                h.races = races;
                h.horseConfidenceList = confHist;
    
                horses.add(h);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return horses;
    }

    // Main Betting method to create the betting interface
    // This method is called from the MainMenu class when the user selects the betting option
    public static void BettingMain(String[] args) {
        if (mainFrame == null) {
            mainFrame = new JFrame("Horse Management System - Betting");
            mainFrame.setSize(600, 600);
            mainFrame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.CENTER;

            JLabel titleLabel = new JLabel("Betting", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            mainPanel.add(titleLabel, gbc);

            JButton newBetButton = new JButton("New Bet");
            newBetButton.addActionListener(e -> {
                newbet();
            });
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            mainPanel.add(newBetButton, gbc);

            JLabel balanceLabel = new JLabel("Balance: " + virtualBalance, SwingConstants.CENTER);
            balanceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            mainPanel.add(balanceLabel, gbc);

            JButton historyButton = new JButton("View Bet History");
            historyButton.addActionListener(e -> {
                StringBuilder historyText = new StringBuilder();
                for (Bet b : betHistory) {
                    historyText.append(b.toString()).append("\n");
                }
                if (historyText.length() == 0) {
                    historyText.append("No bets placed yet.");
                }
                JOptionPane.showMessageDialog(mainFrame, historyText.toString(), "Bet History", JOptionPane.INFORMATION_MESSAGE);
            });
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            mainPanel.add(historyButton, gbc);

            JButton backButton = new JButton("Done");
            backButton.addActionListener(e -> {
                mainFrame.dispose();
                mainFrame = null;
            });
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            mainPanel.add(backButton, gbc);

            mainFrame.add(new JScrollPane(mainPanel));
            mainFrame.setVisible(true);
        }
    }

    // Method to create a new betting window
    // This method is called when the user clicks the "New Bet" button in the betting interface 
    public static void newbet() {
        JFrame betFrame = new JFrame("Place a New Bet");
        betFrame.setSize(500, 400);
        betFrame.setLocationRelativeTo(null);

        JPanel betPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel betTitleLabel = new JLabel("Place Your Bet", SwingConstants.CENTER);
        betTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        betPanel.add(betTitleLabel, gbc);

        ArrayList<Horse> horses = loadHorsesFromCSV();
        if (horses.isEmpty()) {
            JOptionPane.showMessageDialog(betFrame, "No horses loaded from CSV!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JLabel selectHorseLabel = new JLabel("Select Horse:");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        betPanel.add(selectHorseLabel, gbc);

        JComboBox<Horse> horseComboBox = new JComboBox<>(horses.toArray(new Horse[0]));
        horseComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Horse) {
                    Horse h = (Horse) value;
                    setText(h.getName());
                }
                return this;
            }
        });
        gbc.gridx = 1;
        betPanel.add(horseComboBox, gbc);

        JLabel oddsLabel = new JLabel("Current Odds: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        betPanel.add(oddsLabel, gbc);

        horseComboBox.addActionListener(e -> {
            Horse selectedHorse = (Horse) horseComboBox.getSelectedItem();
            double odds = calculateOdds(selectedHorse, MainMenu.getweather());
            oddsLabel.setText("Current Odds: " + odds);
        });

        Horse initialHorse = (Horse) horseComboBox.getSelectedItem();
        double initialOdds = calculateOdds(initialHorse, MainMenu.getweather());
        oddsLabel.setText("Current Odds: " + initialOdds);

        JLabel betAmountLabel = new JLabel("Bet Amount:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        betPanel.add(betAmountLabel, gbc);

        JTextField betAmountField = new JTextField();
        gbc.gridx = 1;
        betPanel.add(betAmountField, gbc);

        JButton confirmBetButton = new JButton("Confirm Bet");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        betPanel.add(confirmBetButton, gbc);

        confirmBetButton.addActionListener(e -> {
            Horse selectedHorse = (Horse) horseComboBox.getSelectedItem();
            double odds = calculateOdds(selectedHorse, MainMenu.getweather()); 
            double betAmount;
            try {
                betAmount = Double.parseDouble(betAmountField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(betFrame, "Please enter a valid number.");
                return;
            }
            if (betAmount <= 0) {
                JOptionPane.showMessageDialog(betFrame, "Bet amount must be above 0.");
                return;
            }
            if (betAmount > virtualBalance) {
                JOptionPane.showMessageDialog(betFrame, "Insufficient balance (" + virtualBalance + ").");
                return;
            }
            virtualBalance -= betAmount;
            Bet newBet = new Bet(selectedHorse.getName(), betAmount, odds);
            betHistory.add(newBet);
            JOptionPane.showMessageDialog(betFrame, "Bet on " + selectedHorse.getName() + " placed. It will be resolved when the race outcome is determined.");
            betAmountField.setText("");
        });

        betFrame.add(betPanel);
        betFrame.setVisible(true);
    }

    // Method to resolve bets after the race
    public static void resolveBets(Horse winningHorse) {
        for (Bet bet : betHistory) {
            if (!bet.resolved) {
                if(winningHorse == null) {
                    bet.win = false;
                    bet.payout = 0;
                } else{
                    if (bet.horseName.equalsIgnoreCase(winningHorse.getName())) {
                        bet.win = true;
                        bet.payout = bet.amount * bet.odds;
                        virtualBalance += bet.payout;  
                    } else {
                        bet.win = false;
                        bet.payout = 0;
                    }
                    bet.resolved = true;
                }
            }
        }
        JOptionPane.showMessageDialog(mainFrame,
            "Bets resolved for winning horse: " + winningHorse.getName() + "\nNew Balance: " + virtualBalance);
    }
}