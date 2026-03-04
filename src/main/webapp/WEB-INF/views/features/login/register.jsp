<%--
    Document   : register
    Created on : Oct 5, 2025, 3:45:10 PM
    Author     : TR_NGHIA
--%>

<%@page import="util.IConstant"%>

<div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="registerModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
        <div class="modal-content shadow-lg">
            <div class="modal-header">
                <h4 class="modal-title w-100 text-center" id="registerModalLabel">Create Account</h4>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p class="text-muted text-center small mb-4">Join us and start your journey with Hotel Misauka.</p>

                <form action="<%= IConstant.ACTION_REGISTER%>" method="POST" id="registerForm" class="needs-validation" novalidate>

                    <%-- Display error --%>
                    <% if (request.getAttribute("ERROR_MSG_REGISTER") != null) {%>
                    <div class="alert alert-danger text-center small py-2 mb-3">
                        <%= request.getAttribute("ERROR_MSG_REGISTER")%>
                    </div>
                    <% }%>

                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="signup-fullname" class="form-label">Full Name</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
                                <input type="text" class="form-control" id="signup-fullname" name="fullName" required>
                                <div class="invalid-feedback">
                                    Please enter your full name.
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <label for="signup-idnumber" class="form-label">ID Number (CCCD/CMND)</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text"><i class="bi bi-person-vcard"></i></span>
                                <input type="text" class="form-control" id="signup-idnumber" name="idNumber" required pattern="[0-9]{9,12}">
                                <div class="invalid-feedback">
                                    Please enter a valid 9 to 12-digit ID number.
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="signup-dob" class="form-label">Date of Birth</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text"><i class="bi bi-calendar-event"></i></span>
                                <input type="date" class="form-control" id="signup-dob" name="dateOfBirth" required>
                                <div class="invalid-feedback">
                                    Please select your date of birth.
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <label for="signup-phone" class="form-label">Phone</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text"><i class="bi bi-telephone-fill"></i></span>
                                <input type="tel" class="form-control" id="signup-phone" name="phone" required pattern="(0[3|5|7|8|9])+([0-9]{8})\b">
                                <div class="invalid-feedback">
                                    Please enter a valid Vietnamese phone number.
                                </div>
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="signup-email" class="form-label">Email Address</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text"><i class="bi bi-envelope-fill"></i></span>
                                <input type="email" class="form-control" id="signup-email" name="email" required>
                                <div class="invalid-feedback">
                                    Please enter a valid email address.
                                </div>
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="signup-address" class="form-label">Address</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text"><i class="bi bi-geo-alt-fill"></i></span>
                                <textarea class="form-control" id="signup-address" name="address" rows="2" required></textarea>
                                <div class="invalid-feedback">
                                    Please enter your address.
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="signup-password" class="form-label">Password</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                                <input type="password" class="form-control" id="signup-password" name="password" required minlength="8">
                                <div class="invalid-feedback">
                                    Password must be at least 8 characters long.
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <label for="signup-confirm-password" class="form-label">Confirm Password</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text"><i class="bi bi-shield-lock-fill"></i></span>
                                <input type="password" class="form-control" id="signup-confirm-password" name="confirmPassword" required>
                                <div class="invalid-feedback" id="confirm-password-feedback">
                                    Passwords do not match.
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="d-grid mt-4">
                        <input type="hidden" name="action" value="<%=IConstant.REGISTER%>">
                        <button type="submit" class="btn btn-warning btn-lg">Register</button>
                    </div>

                </form>
            </div>
            <div class="modal-footer justify-content-center">
                <p class="small text-muted">Already have an account?
                    <a href="#" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#loginModal" class="text-warning fw-bold">Login</a>
                </p>
            </div>
        </div>
    </div>
</div>

<script>
    (function () {
        'use strict';

        // ===============================================
        // ========== GET MODAL AND FORM ELEMENTS =======
        // ===============================================
        var registerModal = document.getElementById('registerModal');
        var registerForm = document.getElementById('registerForm');
        var password = document.getElementById('signup-password');
        var confirmPassword = document.getElementById('signup-confirm-password');
        var confirmPasswordFeedback = document.getElementById('confirm-password-feedback');

        // ===============================================
        // ========== PASSWORD VALIDATION FUNCTION =======
        // ===============================================
        function validatePassword() {
            if (password.value !== confirmPassword.value) {
                confirmPassword.setCustomValidity("Passwords do not match.");
                confirmPasswordFeedback.textContent = "Passwords do not match.";
            } else {
                confirmPassword.setCustomValidity("");
                confirmPasswordFeedback.textContent = "Passwords do not match.";
            }
        }

        // ===============================================
        // ========== EVENT LISTENERS FOR PASSWORD =======
        // ===============================================
        password.addEventListener('input', validatePassword);
        confirmPassword.addEventListener('input', validatePassword);

        // ===============================================
        // ========== FORM SUBMIT VALIDATION =============
        // ===============================================
        registerForm.addEventListener('submit', function (event) {
            validatePassword();

            if (!registerForm.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }

            registerForm.classList.add('was-validated');
        }, false);

        // ===============================================
        // ========== RESET FORM ON MODAL CLOSE ==========
        // ===============================================
        registerModal.addEventListener('hidden.bs.modal', function () {
            registerForm.classList.remove('was-validated');
            registerForm.reset();
            confirmPassword.setCustomValidity("");
        });

    })();
    
    // ===============================================
    // ========== OPEN SCRIPT AUTO ERROR MSG =========
    // ===============================================
    <%
    if (request.getAttribute("ERROR_MSG_REGISTER") != null && !request.getAttribute("ERROR_MSG_REGISTER").toString().isEmpty()) {
    %>
    document.addEventListener('DOMContentLoaded', function () {
        var loginModalElement = document.getElementById('registerModal');
        if (loginModalElement) {
            var loginModal = new bootstrap.Modal(loginModalElement);
            loginModal.show();
        }
    });
    <%
    }
    %>
</script>