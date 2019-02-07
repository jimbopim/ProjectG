package com.jimbodev.jimla.projectg;

import android.util.Log;

import java.util.HashMap;

public class AnimationHandler {

    private HashMap<TAG, Animation> animations = new HashMap<>();

    void addNewAnimation(TAG name, int length) {
        if (!contains(name))
            animations.put(name, new Animation(name, length));
        else
            errorMessage("Entry already exists - createNewAnimation method, animation class", true);
    }

    void play(TAG name) {
        rewind(name);
        start(name);
    }

    void resume(TAG name) {
        start(name);
    }

    void stop(TAG name) {
        if (contains(name)) {
            animations.get(name).lastUpdated = 0;
            animations.get(name).playing = false;
        }
    }
    
    private void start(TAG name) {
        if (contains(name)) {
            animations.get(name).lastUpdated = System.currentTimeMillis();
            animations.get(name).playing = true;
        }
    }

    private void rewind(TAG name) {
        animations.get(name).timeLeft = animations.get(name).animationLength;
    }
    
    boolean isPlaying(TAG name) {
        if (contains(name)) {
            if (get(name).timeLeft > 0 && animations.get(name).playing)
                return true;
            else
                return false;
        }
        else {
            errorMessage("isPlaying", false);
            return false;
        }
    }

    private Animation get(TAG name) {
        if (contains(name)) {
            return animations.get(name);
        }
        else {
            errorMessage("get", false);
            return null;
        }
    }

    private boolean contains(TAG name) {
        return animations.containsKey(name);
    }

    int getTimeLeft(TAG name) {
        if (contains(name)) {
            Animation animation = get(name);
            updateTimeLeft(animation);
            return animation.timeLeft;
        }
        else {
            errorMessage("getTimeLeft", false);
            return 0;
        }
    }

    private void updateTimeLeft(Animation animation) {
        if(animation.playing) {
            long diff = System.currentTimeMillis() - animation.lastUpdated;
            animation.lastUpdated = System.currentTimeMillis();
            animation.timeLeft -= diff;
            if (animation.timeLeft <= 0) {
                animation.timeLeft = 0;
                stop(animation.name);
            }
        }
    }

    protected class Animation {
        private TAG name;
        private int animationLength, timeLeft;
        private long lastUpdated;
        private boolean playing;

        Animation(TAG name, int animationLength) {
            this.name = name;
            this.animationLength = animationLength;
            this.timeLeft = animationLength;
            playing = false;
        }
    }

    private void errorMessage(String error, boolean custom) {
        if(custom)
            Log.e("hejsan", error);
        else
            Log.e("hejsan","No Such Entry - " + error + " method, animation class");
    }
}
