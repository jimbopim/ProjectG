package com.jimbodev.jimla.projectg;

import android.util.Log;

import java.util.HashMap;

public class AnimationHandler {

    private HashMap<String, Animation> animations = new HashMap<>();

    void addNewAnimation(String name, int length) {
        if (!contains(name))
            animations.put(name, new Animation(length));
        else
            Log.e("hejsan", "Entry already exists - addNewAnimation method, animation class");
    }

    private Animation get(String name) {
        if (contains(name)) {
            return animations.get(name);
        }
        else {
            Log.e("hejsan", "No Such Entry - get method, animation class");
            return null;
        }
    }

    boolean contains(String name) {
        return animations.containsKey(name);
    }

    int getTimeLeft(String name) {
        Animation animation = get(name);
        updateTimeLeft(animation);
        return animation.timeLeft;
    }

    private void updateTimeLeft(Animation animation) {
        long diff = System.currentTimeMillis() - animation.lastUpdated;
        animation.lastUpdated = System.currentTimeMillis();
        animation.timeLeft -= diff;
    }

    private class Animation {
        int animationLength, timeLeft;
        long lastUpdated;


        Animation(int animationLength) {
            this.animationLength = animationLength;
            this.timeLeft = animationLength;
            lastUpdated = System.currentTimeMillis();
        }
    }

}
