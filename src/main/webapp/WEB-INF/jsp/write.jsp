<%@ include file="header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">${not empty article ? 'Edit Article' : 'New Article'}</div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/article" method="post">
                    <c:if test="${not empty article}">
                        <input type="hidden" name="id" value="${article.id}">
                    </c:if>
                    
                    <div class="mb-3">
                        <label class="form-label">Title</label>
                        <input type="text" name="title" class="form-control" value="${article.title}" required>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label">Content</label>
                        <textarea name="content" class="form-control" rows="10" required>${article.content}</textarea>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">Save</button>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Cancel</a>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
