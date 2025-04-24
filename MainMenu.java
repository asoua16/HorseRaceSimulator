import javax.swing.*;
import java.awt.*;

public class MainMenu {
    private static JSlider lancounterSlider;
    private static JComboBox<String> lanetypesBox;
    private static JSlider tracklengthSlider;
    private static JComboBox<String> weatherconditionsBox;

    public static JSlider lanecounter() {
        lancounterSlider = new JSlider(1, 10);
        lancounterSlider.setPaintTrack(true);
        lancounterSlider.setPaintTicks(true);
        lancounterSlider.setPaintLabels(true);
        lancounterSlider.setMajorTickSpacing(9);
        lancounterSlider.setMinorTickSpacing(1);
        lancounterSlider.setSnapToTicks(true);
        return lancounterSlider;
    }

    public static JComboBox<String> lanetypes() {
        String[] laneStrings = {"Circle", "Figure-Eight", "Oval"};
        lanetypesBox = new JComboBox<>(laneStrings);
        return lanetypesBox;
    }

    public static JSlider tracklength() {
        tracklengthSlider = new JSlider(0, 100);
        tracklengthSlider.setPaintTrack(true);
        tracklengthSlider.setPaintTicks(true);
        tracklengthSlider.setPaintLabels(true);
        tracklengthSlider.setMajorTickSpacing(20);
        tracklengthSlider.setMinorTickSpacing(5);
        tracklengthSlider.setSnapToTicks(true);
        return tracklengthSlider;
    }

    public static JComboBox<String> weatherconditions() {
        String[] weatherStrings = {"Muddy", "Dry", "Icy"};
        weatherconditionsBox = new JComboBox<>(weatherStrings);
        return weatherconditionsBox;
    }

    public static int getlanecount() {
        return lancounterSlider.getValue();
    }

    public static String getlanetype() {
        return (String) lanetypesBox.getSelectedItem();
    }

    public static int gettracklength() {
        return tracklengthSlider.getValue();
    }

    public static String getweather() {
        return (String) weatherconditionsBox.getSelectedItem();
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Horse Race Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); // Center on screen

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        //Main Menu Title
        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Optional: make it big and bold

        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(titleLabel, gbc);

        // Lane Counter
        gbc.gridy = 1;
        panel.add(new JLabel("Lane Counter:"), gbc);
        gbc.gridy = 2;
        panel.add(lanecounter(), gbc);

        // Track Length
        gbc.gridy = 3;
        panel.add(new JLabel("Track Length:"), gbc);
        gbc.gridy = 4;
        panel.add(tracklength(), gbc);

        // Lane Types
        gbc.gridy = 5;
        panel.add(new JLabel("Lane Type:"), gbc);
        gbc.gridy = 6;
        panel.add(lanetypes(), gbc);

        // Weather Conditions
        gbc.gridy = 7;
        panel.add(new JLabel("Weather Condition:"), gbc);
        gbc.gridy = 8;
        panel.add(weatherconditions(), gbc);

        // Start Button
        JButton STARTbutton = new JButton("Start");
        STARTbutton.addActionListener(e -> {
            int distance = gettracklength();
            int lanes = getlanecount();
            Race newrace = new Race(distance, lanes);
            newrace.startRace();
        });

        JButton HorseManagement = new JButton("Horse Management");
        HorseManagement.addActionListener(e -> {
            HorseManage.HorseMain(null);
        });

        gbc.gridy = 9;
        panel.add(HorseManagement, gbc);
        gbc.gridy = 10;
        panel.add(STARTbutton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }
}
