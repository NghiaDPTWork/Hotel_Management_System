package controller.admin;

import util.IConstant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/admin/pre_addStaff")
public class PrefixAddStaffController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // ===============================================
        // ======== FORWARD TO ADD STAFF FORM ============
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_STAFF_ADD).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Prefix Add Staff Controller - Forwards to staff addition form";
    }
}