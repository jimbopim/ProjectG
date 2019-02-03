package com.jimbodev.jimla.projectg;

import java.util.ArrayList;

public interface Attacker {
    void shoot(Vector target, ArrayList<Projectile> projectiles);
    void targetHit();
}
