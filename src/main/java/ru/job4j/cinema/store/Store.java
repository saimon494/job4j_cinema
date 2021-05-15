package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;

public interface Store {

    boolean save(Account account);

    Collection<Hall> findAllHalls();

    Hall findHallByRowCol(int rowCol);
}
