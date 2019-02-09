package com.jimbodev.jimla.projectg;

public class TimeTrackerHandler {

    TimeTracker createNewTimeTracker(int length) {
        return new TimeTracker(length);
    }

    void play(TimeTracker timeTracker) {
        rewind(timeTracker);
        start(timeTracker);
    }

    void resume(TimeTracker name) {
        start(name);
    }

    void stop(TimeTracker timeTracker) {
        timeTracker.lastUpdated = 0;
        timeTracker.playing = false;
    }

    private void start(TimeTracker timeTracker) {
        timeTracker.lastUpdated = System.currentTimeMillis();
        timeTracker.playing = true;
    }

    private void rewind(TimeTracker timeTracker) {
        timeTracker.timeLeft = timeTracker.length;
    }

    boolean isPlaying(TimeTracker timeTracker) {
        return timeTracker.timeLeft > 0 && timeTracker.playing;
    }

    int getTimeLeft(TimeTracker timeTracker) {
        updateTimeLeft(timeTracker);
        return timeTracker.timeLeft;
    }

    private void updateTimeLeft(TimeTracker timeTracker) {
        if (timeTracker.playing) {
            long diff = System.currentTimeMillis() - timeTracker.lastUpdated;
            timeTracker.lastUpdated = System.currentTimeMillis();
            timeTracker.timeLeft -= diff;
            if (timeTracker.timeLeft <= 0) {
                timeTracker.timeLeft = 0;
                stop(timeTracker);
            }
        }
    }

    class TimeTracker {
        private int length, timeLeft;
        private long lastUpdated;
        private boolean playing;

        TimeTracker(int length) {
            this.length = length;
            this.timeLeft = length;
            playing = false;
        }
    }
}
