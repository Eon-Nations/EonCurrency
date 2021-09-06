package me.squid.eoncurrency.managers;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.jobs.JobEvent;
import me.squid.eoncurrency.jobs.Jobs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.UUID;

public class SQLManager {

    Eoncurrency plugin;
    private static MySQL sql;

    public SQLManager(Eoncurrency plugin) {
        sql = new MySQL(plugin);
        try {
            sql.connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createTables();
    }

    public void createTables() {
        String[] sqlQueries = {"CREATE TABLE IF NOT EXISTS currency " + "(UUID VARCHAR(100),MONEY FLOAT(10),PRIMARY KEY(UUID))"
        , "CREATE TABLE IF NOT EXISTS jobs " + "(UUID VARCHAR(100),JOB VARCHAR(100),EXPLEVEL FLOAT(10),LVL INT(100),PRIMARY KEY(UUID))"};
        for (String query : sqlQueries) {
            try {
                PreparedStatement ps = sql.getConnection().prepareStatement(query);
                ps.executeUpdate();
            } catch (SQLException e) {
                reconnectToDatabase(e);
            }
        }
    }

    public static void addNewCurencyPlayer(UUID uuid) {
        try {
            if (!currencyPlayerExists(uuid)) {
                PreparedStatement ps = sql.getConnection().prepareStatement("INSERT IGNORE INTO currency" + " (UUID, MONEY) VALUES (?, ?)");
                ps.setString(1, uuid.toString());
                ps.setDouble(2, 0.0);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            reconnectToDatabase(e);
        }
    }

    public static void updateBalance(UUID uuid) {
        try {
            if (!currencyPlayerExists(uuid)) addNewCurencyPlayer(uuid);
            else {
                PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE currency SET MONEY=? WHERE UUID=?");
                ps.setDouble(1, EconomyManager.getBalance(uuid));
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            reconnectToDatabase(e);
        }
    }

    public static double getBalance(UUID uuid) {
        try {
            if (currencyPlayerExists(uuid)) {
                PreparedStatement ps = sql.getConnection().prepareStatement("SELECT MONEY FROM currency WHERE UUID=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getInt("MONEY");
            } else addNewCurencyPlayer(uuid);
        } catch (SQLException e) {
            reconnectToDatabase(e);
        }
        return 0;
    }

    private static boolean currencyPlayerExists(UUID uuid) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM currency WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            return results.next();
        } catch (SQLException e) {
            reconnectToDatabase(e);
        }
        return false;
    }

    public static void uploadPlayerJob(UUID uuid, Job job) {
        if (!jobPlayerExists(uuid)) {
            try {
                PreparedStatement ps = sql.getConnection().prepareStatement("INSERT IGNORE INTO jobs" + " (UUID, JOB, EXPLEVEL, LVL) VALUES (?, ?, ?, ?)");
                ps.setString(1, uuid.toString());
                ps.setString(2, job.getEnumJob().name().toLowerCase());
                ps.setDouble(3, job.getExp());
                ps.setInt(4, job.getLevel());
                ps.executeUpdate();
            } catch (SQLException e) {
                reconnectToDatabase(e);
            }
        }
    }

    public static void updatePlayerJob(UUID uuid, Job job) {
        if (JobsManager.playerExists(uuid)) {
            try {
                PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE jobs SET JOB=?, EXPLEVEL=?, LVL=? WHERE UUID=?");
                ps.setString(1, job.getEnumJob().name().toLowerCase());
                ps.setDouble(2, job.getExp());
                ps.setInt(3, job.getLevel());
                ps.setString(4, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                reconnectToDatabase(e);
            }
        } else uploadPlayerJob(uuid, job);
    }

    public static Job getPlayerJob(UUID uuid) {
        if (jobPlayerExists(uuid)) {
            try {
                PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM jobs WHERE UUID=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Jobs job = Jobs.valueOf(rs.getString("JOB").toUpperCase());
                    double exp = rs.getDouble("EXPLEVEL");
                    int level = rs.getInt("LVL");
                    return new Job(job, level, exp, JobEvent.getEventsFromJob(job));
                }
            } catch (SQLException e) {
                reconnectToDatabase(e);
            }
        }
        return null;
    }

    public static boolean jobPlayerExists(UUID uuid) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM jobs WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            reconnectToDatabase(e);
        }
        return false;
    }

    private static void reconnectToDatabase(SQLException e) {
        if (e instanceof SQLNonTransientConnectionException) {
            sql.disconnect();
            try {
                sql.connectToDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else e.printStackTrace();
    }
}
