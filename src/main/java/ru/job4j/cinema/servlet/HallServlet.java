package ru.job4j.cinema.servlet;

import com.google.gson.Gson;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class HallServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Collection<Hall> halls = PsqlStore.instOf().findAllHalls();
        Gson gson = new Gson();
        String result = gson.toJson(halls);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter pw = resp.getWriter();
        pw.write(result);
        pw.flush();
    }
}
