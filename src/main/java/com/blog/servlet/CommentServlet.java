package com.blog.servlet;

import com.blog.dao.CommentDAO;
import com.blog.model.Comment;
import com.blog.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    private CommentDAO commentDAO = new CommentDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int articleId = Integer.parseInt(req.getParameter("articleId"));
        String content = req.getParameter("content");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            Comment comment = new Comment();
            comment.setArticleId(articleId);
            comment.setUserId(user.getId());
            comment.setContent(content);
            commentDAO.addComment(comment);
        }

        resp.sendRedirect(req.getContextPath() + "/article?action=view&id=" + articleId);
    }
}
