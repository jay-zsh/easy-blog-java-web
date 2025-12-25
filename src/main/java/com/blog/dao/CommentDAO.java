package com.blog.dao;

import com.blog.model.Comment;
import com.blog.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public List<Comment> getCommentsByArticleId(int articleId) {
        List<Comment> list = new ArrayList<>();
        String sql = "SELECT c.*, u.nickname as user_name FROM comments c " +
                "JOIN users u ON c.user_id = u.id " +
                "WHERE c.article_id = ? ORDER BY c.create_time ASC";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setId(rs.getInt("id"));
                    comment.setArticleId(rs.getInt("article_id"));
                    comment.setUserId(rs.getInt("user_id"));
                    comment.setUserName(rs.getString("user_name"));
                    comment.setContent(rs.getString("content"));
                    comment.setCreateTime(rs.getTimestamp("create_time"));
                    list.add(comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addComment(Comment comment) {
        String sql = "INSERT INTO comments (article_id, user_id, content) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, comment.getArticleId());
            pstmt.setInt(2, comment.getUserId());
            pstmt.setString(3, comment.getContent());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
