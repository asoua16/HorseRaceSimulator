public class Main {
    public static void main(String[] args) {
        Horse horse1 = new Horse('A', "Thunder", 0.8);
        Horse horse2 = new Horse('B', "Lightning", 0.6);
        Horse horse3 = new Horse('C', "Blaze", 0.9);

        Race newrace = new Race(10);
        newrace.addHorse(horse1, 1);
        newrace.addHorse(horse2, 2);
        newrace.addHorse(horse3, 3);
        newrace.startRace();
    }
}
