package com.japancuccok.common.wicket.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.04.12.
 * Time: 20:25
 */
public class DefaultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher japanCuccokDispather = getServletContext().getRequestDispatcher("/japancuccok/");
        japanCuccokDispather.forward(req, resp);
    }
}
