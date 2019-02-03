package com.jimbodev.jimla.projectg;

class Projectile extends Mobile {
    private Attacker parent;
    private int damage;

    Projectile(float x, float y, Vector target, Attacker parent, int damage) {
        super(x, y, 10, 10, 10, 5.0f, target);
        this.parent = parent;
        this.damage = damage;
    }

    void hit() {
        if(parent != null)
            parent.targetHit();
    }

    int getDamage() {
        return damage;
    }
}
