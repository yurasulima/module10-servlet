package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timezone = req.getParameter("timezone");
        ZoneId zoneId;


        if (timezone != null) {
            zoneId = ZoneId.of(timezone.replace(" ", "+"));
        } else {
            zoneId = ZoneId.of("UTC");
        }


        LocalDateTime currentTime = LocalDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        String html = """
                <h2>Current Time: %s %s</h2>
                """;

        resp.setContentType("text/html");
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write(html.formatted(formattedTime, zoneId));
        resp.getWriter().close();

    }
}
