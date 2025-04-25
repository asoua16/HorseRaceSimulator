import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private ArrayList<Horse> allhorses = new ArrayList<>();
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
        long raceStartTime = System.currentTimeMillis(); // start time
    
        Timer timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean finished = false;
                int allfallen = 0;
                String winnerName = ""; // store winner name
                long raceEndTime = 0;   // store end time
    
                for (Horse horse : allhorses) {
                    moveHorse(horse);
    
                    if (raceWonBy(horse)) {
                        winnerName = horse.getName();
                        raceEndTime = System.currentTimeMillis(); // end time
                        double raceTimeSeconds = (raceEndTime - raceStartTime) / 1000.0;
                        horse.addSpeed(raceTimeSeconds, raceLength);
                        finished = true;
                        horse.addwin();
                        break;
                    } else if (horse.hasFallen()) {
                        allfallen++;
                        horse.setSymbol('X');
                        break;
                    }
    
                    if (allfallen == allhorses.size()) {
                        JOptionPane.showMessageDialog(frame, "All horses have fallen!");
                        finished = false;
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
    
                    frame.dispose();
                }
            }
        });
    
        timer.start();
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


