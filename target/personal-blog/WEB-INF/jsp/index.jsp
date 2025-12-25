<%@ include file="header.jsp" %>

    <div class="row">
        <div class="col-md-8 offset-md-2">
            <h2 class="mb-4">Latest Posts</h2>

            <c:forEach items="${articles}" var="article">
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">
                            <a href="${pageContext.request.contextPath}/article?action=view&id=${article.id}"
                                class="text-decoration-none">
                                ${article.title}
                            </a>
                        </h5>
                        <p class="card-text text-muted">
                            <small>By ${article.authorName} | ${article.createTime} | Views:
                                ${article.visitCount}</small>
                        </p>
                        <p class="card-text">
                            ${article.content.length() > 200 ? article.content.substring(0, 200).concat("...") :
                            article.content}
                        </p>
                        <a href="${pageContext.request.contextPath}/article?action=view&id=${article.id}"
                            class="btn btn-primary btn-sm">Read More</a>
                    </div>
                </div>
            </c:forEach>

            <!-- Pagination -->
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage - 1}">Previous</a>
                        </li>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="?page=${i}">${i}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage + 1}">Next</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>

    <%@ include file="footer.jsp" %>