package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Hall;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class PsqlStore implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("cinema_db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public boolean save(Account account) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps1 = cn.prepareStatement(
                     "select * from hall where row_column = ?");
             PreparedStatement ps2 = cn.prepareStatement(
                     "insert into account(username, phone, hall) values (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement ps3 = cn.prepareStatement(
                     "update hall set account_id=? where row_column=?")
        ) {
            ps1.setInt(1, account.getHall());
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && rs.getInt("account_id") != 0) {
                    return false;
                }
            }
            ps2.setString(1, account.getName());
            ps2.setString(2, account.getPhone());
            ps2.setInt(3, account.getHall());
            ps2.execute();
            try (ResultSet id = ps2.getGeneratedKeys()) {
                if (id.next()) {
                    account.setId(id.getInt(1));
                }
            }
            ps3.setInt(1, account.getId());
            ps3.setInt(2, account.getHall());
            ps3.execute();
        } catch (Exception e) {
            LOG.error("Error", e);
        }
        return true;
    }

    @Override
    public Collection<Hall> findAllHalls() {
        List<Hall> halls = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "select * from hall order by row_column asc")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    halls.add(new Hall(it.getInt("id"),
                            it.getInt("row_column"),
                            it.getInt("account_id"))
                    );
                }
            }
        } catch (Exception e) {
            LOG.error("Error", e);
        }
        return halls;
    }

    @Override
    public Hall findHallByRowCol(int rowCol) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement(
                     "select * from hall where row_column = ?")) {
            st.setInt(1, rowCol);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Hall(
                            rs.getInt("id"),
                            rs.getInt("row_column"),
                            rs.getInt("account_id")
                    );
                }
            }
        } catch (Exception e) {
            LOG.error("Error", e);
        }
        return null;
    }

    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Account("Ivan", "777-77-77", 11));
        System.out.println(store.findAllHalls());
        System.out.println(store.findHallByRowCol(11));
    }
}
