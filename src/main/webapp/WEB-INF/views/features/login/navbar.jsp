<%--
    Document   : login
    Created on : Oct 5, 2025, 10:40:06 AM
    Author     : TR_NGHIA
--%>


<%@page import="util.IConstant"%>
<%@page import="dto.Guest"%>
<%@page import="dto.Staff"%>

<nav class="navbar navbar-expand-lg fixed-top" id="mainNavbar">
    <div class="container">
        <div class="d-flex align-items-center justify-content-between w-100">

            <div class="d-none d-lg-flex gap-4">
                <a class="nav-link" href="#about">About Us</a>
                <a class="nav-link" href="#rooms">Rooms</a>
                <a class="nav-link" href="#testimonials">Feedback</a>
                <a class="nav-link" href="#contact">Contact</a>
            </div>

            <a class="navbar-brand d-flex align-items-center" href="HomeController">
                <div class="logo-icon bg-warning d-flex align-items-center justify-content-center rounded me-2">
                    <span class="text-white fw-bold">H</span>
                </div>
                <div>
                    <div class="fw-semibold" style="font-size: 0.9rem; line-height: 1;">HOTEL</div>
                    <div class="text-muted" style="font-size: 0.7rem; line-height: 1;">MISUKA</div>
                </div>
            </a>

            <div class="d-none d-lg-flex align-items-center gap-4">
                <a href="tel:+250962712101" class="info-link">
                    <i class="bi bi-telephone me-2"></i>
                    <span>+84 906 123 279</span>
                </a>

                <%-- =========== LOGIC SCRIPTLET HI?N TH? ??NG NH?P HO?C TÊN USER =========== --%>
                <%
                    if (session.getAttribute("USER_GUEST") != null) {
                        Guest user = (Guest) session.getAttribute("USER_GUEST");
                        String fullName = user.getFullName();

                %>
                <div class="nav-item dropdown">
                    <a class="info-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-fill me-2"></i>
                        <%= fullName%>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                        <li>
                            <a class="dropdown-item" href="<%= IConstant.ACTION_VIEW_BOOKINGS %>">My Profile</a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <form action="<%=IConstant.ACTION_LOGOUT%>" class="d-inline w-100">
                                <button type="submit" class="dropdown-item">Logout</button>
                            </form>
                        </li>
                    </ul>
                </div>
                <%
                } else {
                %>
                <a href="<%=IConstant.ACTION_LOGIN%>" class="info-link" data-bs-toggle="modal" data-bs-target="#loginModal">
                    <i class="bi bi-person-circle me-2"></i>
                    <span>Login</span>
                </a>
                <%
                    }
                %>
                <%-- ============================ K?T THÚC LOGIC SCRIPTLET ============================ --%>
            </div>

            <button class="navbar-toggler d-lg-none" type="button" data-bs-toggle="collapse" data-bs-target="#mobileNavbar">
                <span class="navbar-toggler-icon"></span>
            </button>
        </div>
    </div>
</nav>




<script>
    /*========== NAVBAR SCROLL EFFECT ==========*/
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 50) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });
    }
    /*========== SCROLL ANIMATION ==========*/
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('is-visible');
            }
        });
    }, {
        threshold: 0.1
    });
    const elementsToAnimate = document.querySelectorAll('.scroll-animate');
    elementsToAnimate.forEach(el => observer.observe(el));
</script>