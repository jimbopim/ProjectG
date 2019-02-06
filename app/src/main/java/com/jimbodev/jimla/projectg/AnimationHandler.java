package com.jimbodev.jimla.projectg;

import android.util.Log;

import java.util.HashMap;

public class AnimationHandler {

    private HashMap<String, Animation> animations = new HashMap<>();

    Animation addNewAnimation(String name, int length) {
        if (!contains(name))
            animations.put(name, new Animation(length));
        else
            Log.e("hejsan", "Entry already exists - addNewAnimation method, animation class");
        
        return animations.get(name);
    }
    
    void startAnimation(String name) {
    	animations.get(name).start();
    }
    
    void reset(String name)
	    Animation animation = animations.get(name);
	    animation.timeLeft = animation.animationLength;
	    animation.playing = false;
    }
    
    void restart(String name) {
    	reset(name);
	    startAnimation(name);
    }
    
    boolean isPlaying(String name) {
    	if(animations.get(name).timeLeft > 0)
		    return true;
		else
			return false;
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
        boolean playing;

        Animation(int animationLength) {
            this.animationLength = animationLength;
            this.timeLeft = animationLength;
            playing = false;
        }
        void start() {
        	lastUpdated = System.currentTimeMillis();
	        playing = true;
        }
    }

}
