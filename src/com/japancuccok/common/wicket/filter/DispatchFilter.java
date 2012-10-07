package com.japancuccok.common.wicket.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.04.26.
 * Time: 19:18
 */
public class DispatchFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Intentionally left blank
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        path = (req.getContextPath().equals(""))?"":path;

        if (path.startsWith("/japancuccok")) {
            servletRequest.getRequestDispatcher("/japancuccok" + path).forward(servletRequest, servletResponse);
        } else if (path.startsWith("/admin")) {
            servletRequest.getRequestDispatcher("/admin" + path).forward(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse); // Goes to default servlet.
        }
    }

    @Override
    public void destroy() {
        // Intentionally left blank
    }
}
