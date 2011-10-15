package me.belf.advancedpvp;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.util.config.Configuration;


public class Abilities {
               
    public static void knockback(Entity player, Entity target, double power) {

        int force = 50;
        int yForce = 20;
        int maxYForce = 80;

        Vector p = target.getLocation().toVector();
        Vector e, v;

        if (target instanceof LivingEntity) {
            e = player.getLocation().toVector();
            v = e.subtract(p).normalize().multiply(force/10.0*power);
            if (force != 0) {
                    v.setY(v.getY() * (yForce/10.0*power));
            } else {
                    v.setY(yForce/10.0*power);
            }
            if (v.getY() > (maxYForce/10.0)) {
                    v.setY(maxYForce/10.0);
            }
            player.setVelocity(v);
        }
    }

}
