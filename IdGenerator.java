package parking;
public class IdGenerator {
    private long counter = System.currentTimeMillis() % 100000;
    public synchronized String next() {
        counter++;
        return Long.toString(counter);
    }
}
