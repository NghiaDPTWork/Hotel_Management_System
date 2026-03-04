package controller.guest;

import dao.GuestDAO;
import dao.StaffDAO;
import dto.Guest;
import dto.Staff;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import util.IConstant;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private StaffDAO staffDAO;
    private GuestDAO guestDAO;

    @Override
    public void init() throws ServletException {
        staffDAO = new StaffDAO();
        guestDAO = new GuestDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // ===============================================
        // ============ CREDENTIAL RETRIEVAL =============
        // ===============================================
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // ===============================================
        // ========== USER AUTHENTICATION CHECK ==========
        // ===============================================
        Staff staff = staffDAO.getStaffByUsernameAndPassword(username, password);
        Guest guest = guestDAO.getGuestByUsernameAndPassword(username, password);

        // ===============================================
        // =========== INITIALIZE LOGIN STATUS ===========
        // ===============================================
        request.getSession().setAttribute("IS_LOGIN", false);

        // ===============================================
        // ============ STAFF LOGIN HANDLING =============
        // ===============================================
        if (staff != null) {
            request.getSession().setAttribute("IS_LOGIN", true);
            request.getSession().setAttribute("USER_STAFF", staff);

            if (staff.isStatus()) {
                String role = staff.getRole().toLowerCase();
                System.out.println(staff.getFullName() + " " + staff.getRole());
                switch (role) {
                    case "admin":
                        response.sendRedirect(IConstant.ACTION_ADMIN_HOME + IConstant.ACTION_ADMIN_FILTER);
                        return;
                    case "receptionist":
                        response.sendRedirect(IConstant.PAGE_RECEPTIONIST);
                        return;
                    case "manager":
                        response.sendRedirect(IConstant.PAGE_MANAGER);
                        return;
                    case "housekeeping":
                        response.sendRedirect(IConstant.PAGE_HOUSEKEEPING);
                        return;
                    case "service_staff":
                        response.sendRedirect(IConstant.PAGE_SERVICE_STAFF);
                        return;
                }
            } else {
                request.setAttribute("ERROR_MSG", "Your account is banned.");
                request.getRequestDispatcher(IConstant.PAGE_HOME).forward(request, response);
            }
        }
        
        // ===============================================
        // ============ GUEST LOGIN HANDLING =============
        // ===============================================
        if (guest != null) {
            request.getSession().setAttribute("IS_LOGIN", true);
            request.getSession().setAttribute("USER_GUEST", guest);
            response.sendRedirect(IConstant.ACTION_HOME);
            return;
        }

        // ===============================================
        // =========== LOGIN FAILURE HANDLING ============
        // ===============================================
        request.setAttribute("ERROR_MSG", "Invalid username or password");
        request.getRequestDispatcher(IConstant.PAGE_HOME).forward(request, response);
    }
}
