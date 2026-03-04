<%--
    Document   : login
    Created on : Oct 5, 2025, 10:40:06 AM
    Author     : TR_NGHIA
--%>

<%@page import="util.IConstant"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg">
            
            <div class="modal-header">
                <h4 class="modal-title w-100 text-center" id="loginModalLabel">Welcome Back</h4>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p class="text-muted text-center small mb-4">Sign in to continue to Hotel Misuka.</p>
                <form action="<%= IConstant.ACTION_LOGIN %>" method="POST">
                    
                    <%-- Hiển thị thông báo lỗi nếu có --%>
                    <%
                        if (request.getAttribute("ERROR_MSG") != null && !request.getAttribute("ERROR_MSG").toString().isEmpty()) {
                            String errorMessage = (String) request.getAttribute("ERROR_MSG");
                    %>
                            <div class="alert alert-danger text-center small py-2 mb-3">
                                <%= errorMessage %>
                            </div>
                    <%
                        }
                    %>
                    
                    <%-- Hiển thị thông báo lỗi nếu có --%>
                    <%
                        if (request.getAttribute("SUCCESS_MSG") != null && !request.getAttribute("SUCCESS_MSG").toString().isEmpty()) {
                            String successMessage = (String) request.getAttribute("SUCCESS_MSG");
                    %>
                            <div class="alert alert-success text-center small py-2 mb-3">
                                <%= successMessage %>
                            </div>
                    <%
                        }
                    %>
                    
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="username" name="username" required placeholder="Enter your username">
                    </div>
                    <div class="mb-4">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required placeholder="Enter your password">
                    </div>
                    
                    <div class="d-grid mt-4">
                        <input type="submit" value="<%=IConstant.LOGIN%>" name="action" class="btn btn-warning btn-lg">
                    </div>
                    
                </form>
            </div>
            <div class="modal-footer justify-content-center">
                <p class="small text-muted">Don't have an account? 
                    <a href="#" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#registerModal" class="text-warning fw-bold" >
                        Sign up
                    </a>
                </p>
            </div>
        </div>
    </div>
</div>
                    
                    

<%-- SCRIPT TỰ ĐỘNG MỞ POP-UP KHI CÓ LỖI  --%>
<%
    if (request.getAttribute("ERROR_MSG") != null && !request.getAttribute("ERROR_MSG").toString().isEmpty()) {
%>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var loginModalElement = document.getElementById('loginModal');
            if (loginModalElement) {
                var loginModal = new bootstrap.Modal(loginModalElement);
                loginModal.show();
            }
        });
    </script>
<%
    }
%>


<%-- SCRIPT TỰ ĐỘNG MỞ POP-UP KHI THÀNH CÔNG  --%>
<%
    if (request.getAttribute("SUCCESS_MSG") != null && !request.getAttribute("SUCCESS_MSG").toString().isEmpty()) {
%>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var loginModalElement = document.getElementById('loginModal');
            if (loginModalElement) {
                var loginModal = new bootstrap.Modal(loginModalElement);
                loginModal.show();
            }
        });
    </script>
<%
    }
%>