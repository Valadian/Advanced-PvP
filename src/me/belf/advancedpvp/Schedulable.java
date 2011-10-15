package me.belf.advancedpvp;

import org.bukkit.Bukkit;

public abstract class Schedulable implements Runnable {

    protected static AdvancedPvP advancedpvp;
    private int id = -1;
    private boolean finished = false;
    
    public static void setAdvancedPvP(AdvancedPvP advancedpvp) {
        Schedulable.advancedpvp = advancedpvp;
    }

    public static int scheduleSyncRepeatingTask(Schedulable task, int delay, long period) throws IllegalStateException {
        if(advancedpvp != null) {
            task.setId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(advancedpvp, task, delay, period));
            return task.getId();
        }
        else {
            throw new java.lang.IllegalStateException("[AdvancedPvP] Impossible de programmer une tache");
        }
    }

    public static int scheduleAsyncDelayedTask(Schedulable task, long delay) throws IllegalStateException {
        if(advancedpvp != null) {
            task.setId(Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(advancedpvp, task, delay));
            return task.getId();
        }
        else {
            throw new java.lang.IllegalStateException("[AdvancedPvP] Impossible de programmer une tache");
        }
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void cancel() {
        if(id != -1) {
            Bukkit.getServer().getScheduler().cancelTask(id);
        }
    }

    public void stop() {
        finished = true;
    }

    public void run() {
        if(!finished) {
            tick();
        }
        if(finished) {
            cancel();
        }
    }

    protected abstract void tick();
}
