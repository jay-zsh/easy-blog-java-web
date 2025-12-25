package com.blog.dao;

import com.blog.model.Article;
import com.blog.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    public List<Article> getArticles(int page, int pageSize) {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT a.*, u.nickname as author_name FROM articles a " +
                "JOIN users u ON a.user_id = u.id " +
                "ORDER BY a.create_time DESC LIMIT ?, ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, (page - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapArticle(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM articles";
        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Article getArticleById(int id) {
        String sql = "SELECT a.*, u.nickname as author_name FROM articles a " +
                "JOIN users u ON a.user_id = u.id WHERE a.id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapArticle(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addArticle(Article article) {
        String sql = "INSERT INTO articles (title, content, user_id) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setInt(3, article.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateArticle(Article article) {
        String sql = "UPDATE articles SET title = ?, content = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setInt(3, article.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteArticle(int id) {
        String sql = "DELETE FROM articles WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementVisitCount(int id) {
        String sql = "UPDATE articles SET visit_count = visit_count + 1 WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Article mapArticle(ResultSet rs) throws SQLException {
        Article article = new Article();
        article.setId(rs.getInt("id"));
        article.setTitle(rs.getString("title"));
        article.setContent(rs.getString("content"));
        article.setUserId(rs.getInt("user_id"));
        article.setAuthorName(rs.getString("author_name"));
        article.setVisitCount(rs.getInt("visit_count"));
        article.setCreateTime(rs.getTimestamp("create_time"));
        article.setUpdateTime(rs.getTimestamp("update_time"));
        return article;
    }
}
