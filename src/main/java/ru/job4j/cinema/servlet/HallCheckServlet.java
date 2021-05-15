package ru.job4j.cinema.servlet;

import com.google.gson.Gson;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HallCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Hall hall = PsqlStore.instOf().findHallByRowCol(
                Integer.parseInt(req.getParameter("rowCol")));
        String result = "";
        if (hall != null) {
            Gson gson = new Gson();
            result = gson.toJson(hall);
        }
        resp.getWriter().print(result);
    }
}
