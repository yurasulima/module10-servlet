package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");

        if (timezone != null) {
            try {
                ZoneId.of(timezone.replace(" ", "+"));
                super.doFilter(req, res, chain);
            } catch (DateTimeException e) {
                res.setStatus(400);
                res.setContentType("text/html; charset=utf-8");
                res.getWriter().write("Invalid timezone");
                res.getWriter().close();
            }
        } else {
            super.doFilter(req, res, chain);
        }
    }
}