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
        allhorses.addAll(horses);
    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     */
    public void startRace() {
        weather();
        long raceStartTime = System.currentTimeMillis();
    
        Timer timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean finished = false;
                int allfallen = 0;
                String winnerName = "None"; 
                long raceEndTime = 0;
                double raceTimeSeconds =0;  
    
                for (Horse horse : allhorses) {
                    moveHorse(horse);
    
                    if (raceWonBy(horse)) {
                        winnerName = horse.getName();
                        raceEndTime = System.currentTimeMillis();
                        raceTimeSeconds = (raceEndTime - raceStartTime) / 1000.0;
                        horse.addSpeed(raceTimeSeconds, raceLength);
                        finished = true;
                        horse.addwin();
                        horse.addrace();
                        break;

                    } else if (horse.hasFallen()) {
                        allfallen++;
                        horse.setSymbol('X');
                        break;
                    }
    
                    if (allfallen == allhorses.size()) {
                        JOptionPane.showMessageDialog(frame, "All horses have fallen!");
                        finished = true;
                        horse.addrace();
                        break;
                    }
                }
    
                racetrack.repaint();
                allfallen = 0;
    
                if (finished) {
                    ((Timer) e.getSource()).stop();
    
                    double totalTime = (System.currentTimeMillis() - raceStartTime) / 1000.0;    
                    JOptionPane.showMessageDialog(
                        frame,
                        "Race Over!\nWinner: " + winnerName + "\nTime: " + totalTime + " seconds"
                    );

                    Stats.racetracksort(raceTimeSeconds);
                    frame.dispose();
                }
            }
        });
    
        saveHorsesToCSV();
        timer.start();
    }

    public static void saveHorsesToCSV() {
        try {
            FileWriter writer = new FileWriter("horses.csv", true);
            writer.write(
                "Name,Symbol,Breed,CoatColor,Horseshoe,Accessories," +
                "Confidence,HorseSpeed,SpeedHistory," +
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


