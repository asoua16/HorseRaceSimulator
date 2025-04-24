
/**
 * Write a description of class Horse here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Horse
    char horseSymbol;
    String horseName;
    double horseConfidence;
    int distanceTravelled;
    boolean hasFallen;
    
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
       this.horseSymbol = horseSymbol;
       this.horseName = horseName;
       this.horseConfidence = horseConfidence;
       this.distanceTravelled = 0;
    }
    
    
    
    //Other methods of class Horse
    public void fall()
    {
        hasFallen = true;
    }
    
    public double getConfidence()
    {
        return horseConfidence;
    }
    
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    
    public String getName()
    {
        return horseName;
    }
    
    public char getSymbol()
    {
        return horseSymbol;
    }
    
    public void goBackToStart()
    {
        distanceTravelled = 0;
    }
    
    public boolean hasFallen()
    {
        return hasFallen;
    }

    public void moveForward()
    {
        distanceTravelled += 1;
    }

    public void setConfidence(double newConfidence)
    {
        horseConfidence = newConfidence;
    }
    
    public void setSymbol(char newSymbol)
    {
        horseSymbol = newSymbol;
    }
    
}
