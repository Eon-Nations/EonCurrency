package me.squid.eoncurrency.jobs;

public class Job {
    Jobs job;
    int level;
    double exp;
    Events[] eventsToListen;

    public Job(Jobs job, int level, double exp, Events[] eventsToListen) {
        this.job = job;
        this.level = level;
        this.exp = exp;
        this.eventsToListen = eventsToListen;
    }

    public Jobs getEnumJob() {
        return job;
    }

    public Events[] getEventsToListen() {
        return eventsToListen;
    }

    public void addExp(double experience) {
        exp += experience;
    }

    public double getExp() {
        return exp;
    }

    public long getLevel() {
        return Math.round(0.5 * Math.sqrt(exp));
    }

}

