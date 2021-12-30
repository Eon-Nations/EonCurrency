package me.squid.eoncurrency.jobs;

import me.squid.eoncurrency.managers.SQLManager;
import me.squid.eoncurrency.utils.Utils;

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

    public Job(Jobs job) {
        this.job = job;
        this.level = 1;
        this.exp = 1.0;
        this.eventsToListen = SQLManager.getEventsFromJob(job);
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
        return Math.round(Utils.getDoubleLevel(exp));
    }

}

