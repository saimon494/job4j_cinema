package ru.job4j.cinema.servlet;

import com.google.gson.Gson;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Account account = new Gson().fromJson(req.getReader().readLine(), Account.class);
        String response = PsqlStore.instOf().save(account) ? "success" : "fail";
        resp.getWriter().print(response);
    }
}
