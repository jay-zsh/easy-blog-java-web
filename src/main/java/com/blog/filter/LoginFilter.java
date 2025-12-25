package com.blog.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        String action = req.getParameter("action");

        // Simple check for protected actions
        boolean isProtected = false;

        if (uri.endsWith("/comment")) {
            isProtected = true;
        } else if (uri.endsWith("/article")) {
            if ("create".equals(action) || "edit".equals(action) || "delete".equals(action)) {
                isProtected = true;
            }
        } else if (uri.contains("/write.jsp")) {
            isProtected = true;
        }

        if (isProtected) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
