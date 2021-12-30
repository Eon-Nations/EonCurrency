package me.squid.eoncurrency.utils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CurrencyPlayer implements Comparable<CurrencyPlayer> {

    public UUID uuid;
    public double balance;

    CurrencyPlayer(UUID uuid, double balance) {
        this.uuid = uuid;
        this.balance = balance;
    }

    @Override
    public int compareTo(@NotNull CurrencyPlayer o) {
        if (uuid == o.uuid) return 0;
        return Double.compare(o.balance, balance);
    }
}
