package com.blog.listener;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class OnlineUserListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        Integer onlineCount = (Integer) ctx.getAttribute("onlineCount");
        if (onlineCount == null) {
            onlineCount = 0;
        }
        ctx.setAttribute("onlineCount", onlineCount + 1);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        Integer onlineCount = (Integer) ctx.getAttribute("onlineCount");
        if (onlineCount != null && onlineCount > 0) {
            ctx.setAttribute("onlineCount", onlineCount - 1);
        }
    }
}
