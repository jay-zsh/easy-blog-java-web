<%@ include file="header.jsp" %>

    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">Login</div>
                <div class="card-body">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>
                    <c:if test="${param.register == 'success'}">
                        <div class="alert alert-success">Registration successful! Please login.</div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-3">
                            <label class="form-label">Username</label>
                            <input type="text" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Password</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Login</button>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-link">No account?
                            Register</a>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%@ include file="footer.jsp" %>