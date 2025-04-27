
/**
 * Represents individual race participants.
 *
 * @AssiaOuaoua (your name)
 * @v1.0 (a version number or a date)
 */
import java.util.ArrayList;
import java.util.Random;

public class Horse
{
    //Fields of class Horse
    String horseName;
    String horseBreed;
    String horseCoatcolor;
    ArrayList<String> horseAccessories = new ArrayList<String>();
    String horseShoe;
    char horseSymbol;
    int horseDistance;
    boolean fallen;
    double horseConfidence;
    double basespeed;
    double horsespeed;
    ArrayList<Double> horseSpeedList = new ArrayList<Double>();
    int wins;
    int races;
    int falls;
    ArrayList<Double> horseConfidenceList = new ArrayList<Double>();

    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, String breed, String coatcolor)
    {
        Random rand = new Random();
        this.horseSymbol = horseSymbol;
        this.horseName = horseName;
        this.horseBreed = breed;
        this.horseCoatcolor = coatcolor;
        this.horseConfidence = rand.nextDouble();
        this.basespeed = rand.nextInt(10) + 1;
        this.horsespeed = basespeed; 
    }

    public Horse(char horseSymbol, String horseName, String breed, String coatcolor, String horseshoe, ArrayList<String> accessories)
    {
        Random rand = new Random();
        this.horseSymbol = horseSymbol;
        this.horseName = horseName;
        this.horseCoatcolor = coatcolor;
        this.horseBreed = breed;
        this.horseShoe = horseshoe;
        this.horseAccessories = accessories;
        this.horseConfidence = rand.nextDouble();
        this.basespeed = rand.nextInt(10) + 1;
        this.horsespeed = basespeed; 
    }


    public void fall()
    {
        this.fallen = true;
    }

    //Getter Methods
    public double getConfidence()
    {
        return this.horseConfidence;
    }

    public int getDistanceTravelled()
    {
        return this.horseDistance;
    }

    public String getName()
    {
        return this.horseName;
    }

    public char getSymbol()
    {
        return this.horseSymbol;
    }

    public String getBreed()
    {
        return this.horseBreed;
    }

    public String getColor()
    {
        return this.horseCoatcolor;
    }

    public String getHorseshoe()
    {
        return this.horseShoe;
    }

    public ArrayList<String> getAccessories()
    {
        return this.horseAccessories;
    }

    public double getSpeed(){
        return this.horsespeed;
    }

    public void goBackToStart()
    {
        horseDistance = 0;
    }

    public double getaverageSpeed() {
        if (horseSpeedList.isEmpty()){
            return 0.0;
        } 
        double sum = 0.0;
        for (double s : horseSpeedList) {
            sum += s;
        }
        return sum / horseSpeedList.size();
    }

    public double getaverageConfidence() {
        if (horseConfidenceList.isEmpty()) {
            return horseConfidence;
        }
        double sum = 0.0;
        for (double c : horseConfidenceList){
            sum += c;
        }
        return sum / horseConfidenceList.size();
    }

    public double getwinRatio(){
        double ratio = (double) wins / (double) races;
        return ratio;
    }

    public boolean hasFallen()
    {
        if(fallen == true){
            this.falls++;
            return true;
        }
        else{
            return false;
        }
    }

    public void moveForward()
    {
        horseDistance += (int)horsespeed;
    }

    

    public void AttributeImpacts(){
        if(horseBreed.equals("Quarter Horse")){
            horsespeed += 2;
            horseConfidence += 0.1;
            SpeedLimit();
        }
        else if(horseBreed.equals("Arabian")){
            horseConfidence += 0.2;
            horsespeed += 1;
            SpeedLimit();
        }
        else if(horseBreed.equals("Thoroughbred")){
            horsespeed += 1;
            SpeedLimit();
            horseConfidence += 0.3;
        }

        for(String accessory : horseAccessories) {
            if (accessory.equals("Saddle")) {
                horsespeed += 1;
                SpeedLimit();
            } 
            else if (accessory.equals("Bridle")) {
                horseConfidence += 0.2;
            } 
            else if(accessory.equals("Hat")){
                horseConfidence += 0.1;
            }
        }

        if(horseShoe.equals("Regular")){
            horsespeed += 1;
            SpeedLimit();
        }
        else if(horseShoe.equals("Lightweight")){
            horsespeed += 2;
            SpeedLimit();
            horseConfidence += 0.1;
        }
        else if(horseShoe.equals("Iron")){
            horsespeed += 3;
            SpeedLimit();
            horseConfidence += 0.2;
        }


    }

    public void resetSpeed() {
        this.horsespeed = this.basespeed;
    }

    public void addSpeed(double time, int trackLength) {
        if (time <= 0){
            return; 
        }
        double speed = (trackLength / time) * 0.1;
        horseSpeedList.add(speed);
    }

    public void addrace(){
        this.races++;
    }

    public void addwin(){
        this.wins++;
    }

    public void addconfidence(){
        horseConfidenceList.add(horseConfidence);
    }

    public void changeConfidence(boolean increase) {
        if (increase) {
            horseConfidence += 0.1;
        } else {
            horseConfidence -= 0.1;
        }
        // if out of [0,1], reset randomly
        if (horseConfidence < 0.0 || horseConfidence > 1.0) {
            horseConfidence = new Random().nextDouble();
        }
        addconfidence();
    }

    private void SpeedLimit() {
        if (this.horsespeed > 10) {
            setSpeed(new Random().nextInt(10) + 1);
        }
        if (this.horsespeed < 1) {
            setSpeed(1);
        }
    }
    
    //Setter Methods
    public void setName(String newName)
    {
        this.horseName = newName;
    }

    public void setBreed(String newBreed)
    {
        this.horseBreed = newBreed;
    }

    public void setCoatColor(String newCoatColor)
    {
        this.horseCoatcolor = newCoatColor;
    }

    public void setConfidence(double newConfidence)
    {
        this.horseConfidence = newConfidence;
    }

    public void setSpeed(double newspeed){
        this.horsespeed = newspeed;
        SpeedLimit();
    }

    public void setSymbol(char newSymbol)
    {
        this.horseSymbol = newSymbol;
    }

}
