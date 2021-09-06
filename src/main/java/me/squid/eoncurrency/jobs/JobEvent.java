package me.squid.eoncurrency.jobs;

import org.bukkit.Material;

public class JobEvent {

    Material material;
    Events event;
    double price;

    public JobEvent(Material material, Events event, double price) {
        this.material = material;
        this.event = event;
        this.price = price;
    }

    public Material getMaterial() {
        return material;
    }

    public Events getEvent() {
        return event;
    }

    public double getPrice() {
        return price;
    }

    public static Events[] getEventsFromJob(Jobs job) {

        switch (job) {
            case MINER, DIGGER, WOODCUTTER -> {
                Events[] events = new Events[1];
                events[0] = Events.BREAK;
                return events;
            }
            case FISHERMAN -> {
                Events[] events = new Events[1];
                events[0] = Events.FISH;
                return events;
            }
            case FARMER -> {
                Events[] events = new Events[5];
                events[0] = Events.BREAK;
                events[1] = Events.PLACE;
                events[2] = Events.BREED;
                events[3] = Events.MILK;
                events[4] = Events.SHEER;
                return events;
            }
            case HUNTER -> {
                Events[] events = new Events[1];
                events[0] = Events.KILL;
                return events;
            }

            case ALCHEMIST -> {
                Events[] events = new Events[2];
                events[0] = Events.BREW;
                events[1] = Events.ENCHANT;
                return events;
            }
            case BLACKSMITH -> {
                Events[] events = new Events[2];
                events[0] = Events.REPAIR;
                events[1] = Events.CRAFT;
                return events;
            }
        }
        return null;
    }
}

