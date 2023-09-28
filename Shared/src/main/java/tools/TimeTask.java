package tools;

public class TimeTask {
    private final Runnable task;
    protected Thread thread;
    private int time;
    private volatile boolean running = false;

    public TimeTask(int time, Runnable task) {
        this.time = time;
        this.task = task;
        thread = new Thread(this::run);
    }

    public void update() {
        if (task != null)
            task.run();
    }

    public void addTime(int time) {
        this.time += time;
    }

    private void run() {
        while (running) {
            if (0 < time) {
                sleep(1000000000L);
                time--;
            }
            while (running && 0 >= time) {
                try {
                    running = false;
                    update();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    public void sleep(long time) {
        int milliseconds = (int) (time) / 1000000;
        int nanoseconds = (int) (time) % 1000000;
        try {
            Thread.sleep(milliseconds, nanoseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        if (Thread.currentThread().equals(thread))
            return;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
