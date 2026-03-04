package util;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,11}$");

    public static String validateStaff(String fullName, String role, String username, String password, String phone, String email) {
        if (isEmpty(fullName)) return "Full name cannot be empty!";
        if (isEmpty(role)) return "Role cannot be empty!";
        if (isEmpty(username)) return "Username cannot be empty!";
        if (isEmpty(password)) return "Password cannot be empty!";
        if (isEmpty(phone)) return "Phone number cannot be empty!";
        if (isEmpty(email)) return "Email cannot be empty!";
        if (password.length() < 6) return "Password must be at least 6 characters long!";
        if (!PHONE_PATTERN.matcher(phone).matches()) return "Invalid phone number! Must be 10-11 digits.";
        if (!EMAIL_PATTERN.matcher(email).matches()) return "Invalid email format!";
        return null; // Valid
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
