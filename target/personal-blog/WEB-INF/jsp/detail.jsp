<%@ include file="header.jsp" %>

<div class="row">
    <div class="col-md-8 offset-md-2">
        <article class="mb-5">
            <h1 class="mb-3">${article.title}</h1>
            <div class="text-muted mb-4">
                By ${article.authorName} | ${article.createTime} | Views: ${article.visitCount}
                <c:if test="${sessionScope.user.id == article.userId}">
                    <span class="float-end">
                        <a href="${pageContext.request.contextPath}/article?action=edit&id=${article.id}" class="btn btn-sm btn-outline-secondary">Edit</a>
                        <a href="${pageContext.request.contextPath}/article?action=delete&id=${article.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Are you sure?')">Delete</a>
                    </span>
                </c:if>
            </div>
            <div class="article-content" style="white-space: pre-wrap;">${article.content}</div>
        </article>

        <hr>

        <section>
            <h4>Comments</h4>
            <div class="mb-4">
                <c:forEach items="${comments}" var="comment">
                    <div class="card mb-2">
                        <div class="card-body">
                            <h6 class="card-subtitle mb-2 text-muted">${comment.userName} - ${comment.createTime}</h6>
                            <p class="card-text">${comment.content}</p>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="card bg-light">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <form action="${pageContext.request.contextPath}/comment" method="post">
                                <input type="hidden" name="articleId" value="${article.id}">
                                <div class="mb-3">
                                    <label class="form-label">Leave a comment</label>
                                    <textarea name="content" class="form-control" rows="3" required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary">Submit</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p>Please <a href="${pageContext.request.contextPath}/login">login</a> to comment.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </section>
    </div>
</div>

<%@ include file="footer.jsp" %>
