/**
 * Does Race simulation
 *
 * @AssiaOuaoua 
 * @27/04/2025
 */
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 *
 * @AssiaOuaoua
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private int laneCount;
    private static ArrayList<Horse> allhorses = new ArrayList<>();
    private JFrame frame;
    private static Racetrack racetrack;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     *
     * @param raceLength the length of the racetrack (in metres/yards...)
     * @param lc = number of lanes
     */
    public Race(int raceLength, int lc) {
        this.raceLength = raceLength;
        this.laneCount = lc;
        frame = new JFrame("Horse Race Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        String lanetype = MainMenu.getlanetype();
        racetrack = new Racetrack(laneCount, allhorses, lanetype);


        frame.add(racetrack);
        frame.setVisible(true);
    }

    /**
     * Adds a horse to the race in a given lane
     *
     * @param theHorse the horse to be added to the race
     */
    public void addHorses(ArrayList<Horse> horses)
    {
        allhorses.clear();
        allhorses.addAll(horses);
    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     */
    public void startRace() {
        for (Horse horse : allhorses) {
            horse.resetSpeed();
        }
        weather();
        long raceStartTime = System.currentTimeMillis();
    
        Timer timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean finished = false;
                int allfallen = 0;
                Horse winner = null;
                long raceEndTime = 0;
                double raceTimeSeconds = 0;  
    
                for (Horse horse : allhorses) {
                    moveHorse(horse);
    
                    if (raceWonBy(horse)) {
                        winner = horse;
                        raceEndTime = System.currentTimeMillis();
                        raceTimeSeconds = (raceEndTime - raceStartTime) / 1000.0;
                        horse.addwin();
                        horse.changeConfidence(true);
                        finished = true;
                        break;

                    } else if (horse.hasFallen()) {
                        allfallen++;
                    }
    
                    if (allfallen == allhorses.size()) {
                        JOptionPane.showMessageDialog(frame, "All horses have fallen!");
                        finished = true;
                        horse.changeConfidence(false);
                        break;
                    }
                }
    
                racetrack.repaint();
    
                if (finished) {
                    ((Timer) e.getSource()).stop();
    
                    double totalTime = (System.currentTimeMillis() - raceStartTime) / 1000.0;
                    if(winner ==null){
                        JOptionPane.showMessageDialog(
                        frame,
                        "Race Over!\nWinner: None" + "\nTime: " + totalTime + " seconds");
                    }
                    else{
                    JOptionPane.showMessageDialog(
                        frame,
                        "Race Over!\nWinner: " + winner.getName() + "\nTime: " + totalTime + " seconds"
                    );
                    }

                    Betting.resolveBets(winner);

                    for(Horse horse : allhorses) {
                        horse.addrace();
                        horse.addSpeed(raceTimeSeconds, raceLength);
                        horse.addconfidence();
                    }

                    Stats.racetracksort(raceTimeSeconds);
                    saveHorsesToCSV();
                    frame.dispose();
                }
            }
        });
    
        timer.start();
    }

    /// Method to save horse data to a CSV file
    /// The file will be named "horses.csv" and will be created in the current working directory.
    public static void saveHorsesToCSV() {
        try {
            FileWriter writer = new FileWriter("horses.csv");
            writer.write(
                "Name,Symbol,Breed,CoatColor,Horseshoe,Accessories," +
                "Confidence,BaseSpeed,SpeedHistory," +
                "Wins,Races,ConfidenceHistory\n"
            );
    
            for (Horse h : allhorses) {
                String accCell = "";
                for (int i = 0; i < h.horseAccessories.size(); i++) {
                    accCell += h.horseAccessories.get(i);
                    if (i < h.horseAccessories.size() - 1) {
                        accCell += ";";
                    }
                }
    
                String speedHist = "";
                for (int i = 0; i < h.horseSpeedList.size(); i++) {
                    speedHist += h.horseSpeedList.get(i);
                    if (i < h.horseSpeedList.size() - 1) {
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
                    h.basespeed + "," +
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

    /// Method to set the weather conditions
    private void weather(){
        String weather = MainMenu.getweather();
        for(Horse horse : allhorses){
            double speed = horse.getSpeed();
            double confidence = horse.getConfidence();
            if(weather.equals("Muddy")){
                if (speed>1){
                    horse.setSpeed(speed/2);
                }
            }
            else if(weather.equals("Icy")){
                horse.setSpeed(speed*2);
                horse.setConfidence(confidence/2);
            }
            else if(weather.equals("Dry")){
                horse.setSpeed(speed*2);
            }
        }

    }

    /// Method to move a horse forward
    /// If the horse has fallen, it will not move forward
    private void moveHorse(Horse theHorse) {
        if (!theHorse.hasFallen()) {
            if (Math.random() < (0.1 * theHorse.getConfidence())) {
                theHorse.fall();
                return;
            }
            
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }
        }
    }

    /**
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        return theHorse.getDistanceTravelled() >= raceLength;
    }
}


