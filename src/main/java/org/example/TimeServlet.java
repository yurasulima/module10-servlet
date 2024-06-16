package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    private TemplateEngine engine;

    @Override
    public void init() {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("D:/coding/java/module10/src/main/webapp/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timezone = req.getParameter("timezone");
        ZoneId zoneId;


        if (timezone != null) {
            zoneId = ZoneId.of(timezone.replace(" ", "+"));
        } else {
            Cookie[] cookies = req.getCookies();
            if (cookies[0] != null) {
                String lastTimezone = cookies[0].getValue();
                zoneId = ZoneId.of(lastTimezone);
            } else {
                zoneId = ZoneId.of("UTC");
            }
        }

        resp.addCookie(new Cookie("lastTimezone", zoneId.toString()));
        LocalDateTime currentTime = LocalDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

//        String html = """
//                <h2>Current Time: %s %s</h2>
//                """;
//
//        resp.setContentType("text/html");
//        resp.setContentType("text/html; charset=utf-8");
//        resp.getWriter().write(html.formatted(formattedTime, zoneId));
//        resp.getWriter().close();
//


        resp.setContentType("text/html");

        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("time", formattedTime, "utc", zoneId)
        );

        engine.process("time", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
