package census;

// A solution to Problem 1.9 in Java Structures, by Duane Bailey
/**
 * A simple stopwatch class
 * @author Rett Bull
 * @date January 23, 2008
 */
public class StopWatch {

    protected long startTime;        // when we started
    protected long accumulatedTime;  // the time accumulated by previous runs
    protected boolean running;       // true, if we are timing
    
    /**
     * @post creates a functioning Stopwatch
     */
    public StopWatch() {
        running = false;
        startTime = 0;
        accumulatedTime = 0;
    }

    /**
     * @post sets the time to zero
     */
    public void reset() {
        accumulatedTime = 0;
    }
    
    /**
     * @post starts timing
     */
    public void start() {
        if (!running) {
           startTime = System.nanoTime ();
           running = true;
        }
    }
    
    /**
     * @post stops (perhaps temporarily) the timing
     */
    public void stop() {
        if (running) {
           accumulatedTime += System.nanoTime () - startTime;
           running = false;
        }
    }
    
    /**
     * @post returns the elapsed time
     */
    public long getTime() {
        if (running)
           return accumulatedTime + System.nanoTime () - startTime;
        else
           return accumulatedTime;
    }
}
