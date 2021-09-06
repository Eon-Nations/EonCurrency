package me.squid.eoncurrency.jobs;

public interface Event {
    String[] actions = {"break", "place", "fish", "kill", "breed", "milk", "sheer", "tame", "enchant", "brew",
            "repair", "craft"};

    

    static Events[] getEventsFromJob(Jobs job) {

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
                Events[] events = new Events[2];
                events[0] = Events.BREAK;
                events[1] = Events.PLACE;
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

