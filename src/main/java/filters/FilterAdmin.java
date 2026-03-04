package filters;

import dto.Staff;
import util.IConstant;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author TR_NGHIA
 */

@WebFilter("/admin/*")
public class FilterAdmin implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("FilterAdmin initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // ===============================================
        // ============ SESSION RETRIEVAL ================
        // ===============================================
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        
        // Debug logging
        System.out.println("Request URI: " + requestURI);

        // ===============================================
        // ========== STAFF AUTHENTICATION CHECK =========
        // ===============================================
        Staff admin = (Staff) session.getAttribute("USER_STAFF");

        // ===============================================
        // ========= SESSION VALIDATION HANDLING =========
        // ===============================================
        if (admin == null) {
            System.out.println("No staff found in session - redirecting to home");
            response.sendRedirect("../" + IConstant.ACTION_HOME);
            return;
        }

        // ===============================================
        // ========== ROLE AUTHORIZATION CHECK ===========
        // ===============================================
        if (!"admin".equals(admin.getRole().toLowerCase())) {
            System.out.println("User role: " + admin.getRole() + " - Access denied");
            response.sendRedirect("../" + IConstant.ACTION_HOME);
            return;
        }

        // ===============================================
        // ============ URL PATH VALIDATION ==============
        // ===============================================
        if (!requestURI.contains("/admin/")) {
            System.out.println("Invalid admin path - forwarding to admin home");
            request.getRequestDispatcher("./admin").forward(request, response);
            return;
        }

        // ===============================================
        // ========== AUTHORIZATION SUCCESSFUL ===========
        // ===============================================
        System.out.println("Access granted for: " + admin.getFullName());
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("FilterAdmin destroyed");
    }
}