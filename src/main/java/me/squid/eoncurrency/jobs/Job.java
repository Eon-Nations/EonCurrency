package me.squid.eoncurrency.managers;

public class Job {
    int maxLevel;
    Jobs job;


    public Job() {

    }

}

enum Jobs {
    ALCHEMIST,
    DIGGER,
    HUNTER,
    MINER,
    BLACKSMITH,
    FISHERMAN,
    WOODCUTTER,
    FARMER
}
