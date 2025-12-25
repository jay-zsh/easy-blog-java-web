package com.blog.servlet;

import com.blog.dao.ArticleDAO;
import com.blog.dao.CommentDAO;
import com.blog.model.Article;
import com.blog.model.Comment;
import com.blog.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/article")
public class ArticleServlet extends HttpServlet {
    private ArticleDAO articleDAO = new ArticleDAO();
    private CommentDAO commentDAO = new CommentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("view".equals(action)) {
            viewArticle(req, resp);
        } else if ("create".equals(action)) {
            req.getRequestDispatcher("/WEB-INF/jsp/write.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            editArticle(req, resp);
        } else if ("delete".equals(action)) {
            deleteArticle(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idStr = req.getParameter("id");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setUserId(user.getId());

        if (idStr != null && !idStr.isEmpty()) {
            // Update
            article.setId(Integer.parseInt(idStr));
            articleDAO.updateArticle(article);
        } else {
            // Create
            articleDAO.addArticle(article);
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }

    private void viewArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        articleDAO.incrementVisitCount(id);
        Article article = articleDAO.getArticleById(id);
        List<Comment> comments = commentDAO.getCommentsByArticleId(id);

        req.setAttribute("article", article);
        req.setAttribute("comments", comments);
        req.getRequestDispatcher("/WEB-INF/jsp/detail.jsp").forward(req, resp);
    }

    private void editArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Article article = articleDAO.getArticleById(id);
        // Check ownership logic could be here
        req.setAttribute("article", article);
        req.getRequestDispatcher("/WEB-INF/jsp/write.jsp").forward(req, resp);
    }

    private void deleteArticle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        // Ideally check if current user owns this article
        articleDAO.deleteArticle(id);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
