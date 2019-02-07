package com.jimbodev.jimla.projectg;

public class AnimationHandlerV2 {

    Animation createNewAnimation(int length) {
        return new Animation(length);
    }

    void play(Animation animation) {
        rewind(animation);
        start(animation);
    }

    void resume(Animation name) {
        start(name);
    }

    void stop(Animation animation) {
        animation.lastUpdated = 0;
        animation.playing = false;
    }

    private void start(Animation animation) {
        animation.lastUpdated = System.currentTimeMillis();
        animation.playing = true;
    }

    private void rewind(Animation animation) {
        animation.timeLeft = animation.animationLength;
    }

    boolean isPlaying(Animation animation) {
        if (animation.timeLeft > 0 && animation.playing)
            return true;
        else
            return false;
    }

    int getTimeLeft(Animation animation) {
        updateTimeLeft(animation);
        return animation.timeLeft;
    }

    private void updateTimeLeft(Animation animation) {
        if (animation.playing) {
            long diff = System.currentTimeMillis() - animation.lastUpdated;
            animation.lastUpdated = System.currentTimeMillis();
            animation.timeLeft -= diff;
            if (animation.timeLeft <= 0) {
                animation.timeLeft = 0;
                stop(animation);
            }
        }
    }

    protected class Animation {
        private int animationLength, timeLeft;
        private long lastUpdated;
        private boolean playing;

        Animation(int animationLength) {
            this.animationLength = animationLength;
            this.timeLeft = animationLength;
            playing = false;
        }
    }
}
