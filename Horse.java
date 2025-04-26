
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
    double horsespeed;
    ArrayList<Double> horseSpeed = new ArrayList<Double>();
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
        this.horsespeed = rand.nextInt(10)+1;
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
        this.horsespeed = rand.nextInt(10)+1;
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

    public double getwinratio(){
        double ratio = (double) wins / (double) races;
        return ratio;
    }

    public double getaverageSpeed(){
        double sum = 0;
        for (double speed : horseSpeed) {
            sum += speed;
        }
        System.out.println(sum/horseSpeed.size());
        return sum / horseSpeed.size();
    }

    public double getaverageConfidence(){
        double sum = 0;
        for (double confidence : horseConfidenceList) {
            sum += confidence;
        }
        return sum / horseConfidenceList.size();
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
        }
        else if(horseBreed.equals("Arabian")){
            horseConfidence += 0.2;
            horsespeed += 1;
        }
        else if(horseBreed.equals("Thoroughbred")){
            horsespeed += 1;
            horseConfidence += 0.3;
        }

        for(String accessory : horseAccessories) {
            if (accessory.equals("Saddle")) {
                horsespeed += 1;
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
        }
        else if(horseShoe.equals("Lightweight")){
            horsespeed += 2;
            horseConfidence += 0.1;
        }
        else if(horseShoe.equals("Iron")){
            horsespeed += 3;
            horseConfidence += 0.2;
        }
    }

    public void addSpeed(double time, int tracklength){
        double speed = (tracklength / time) * 0.1;
        horseSpeed.add(speed);
    }

    public void addrace(){
        this.races++;
    }

    public void addwin(){
        this.wins++;
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
        this.horseConfidenceList.add(this.horseConfidence);
    }

    public void setSpeed(double newspeed){
        this.horsespeed = newspeed;
    }

    public void setSymbol(char newSymbol)
    {
        this.horseSymbol = newSymbol;
    }

}
