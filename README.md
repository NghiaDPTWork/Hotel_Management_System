# Hotel Misuka Management System

A high-performance hotel management and booking system built with Java EE. The platform adheres to the MVC architectural pattern, providing a frictionless real-time booking experience for guests and a secure, comprehensive management dashboard for administrators.

## Tech Stack

- **Back-end:** Java EE (Servlets & JSP), Maven (Dependency Management)
- **Database:** Microsoft SQL Server
- **Front-end:** HTML5, Vanilla CSS (Premium Design System), Bootstrap 5, JavaScript
- **Design Pattern:** MVC (Model-View-Controller) - Ensuring a clean separation of concerns between the UI, control logic, and data access layers.

## Core Features

### For Guests

- **Real-time Search:** Instantly search for available rooms based on dates and preferences.
- **Seamless Booking:** A streamlined 3-step booking process: `Select Room` -> `Fill Information` -> `Payment & Checkout`.
- **Account Management:** Secure Registration/Login and the ability to track personal booking history.

### For Administrators

- **Staff Management:** Full CRUD (Create, Read, Update, Delete) operations and role-based search capabilities for hotel personnel.
- **Room Inventory:** Dynamically manage room statuses, room types, and detailed descriptions.
- **System Configuration:** Configure and manage additional hotel services and operational parameters.

## Architecture & Folder Structure

The project follows a clean and maintainable directory structure:

- **`util/`**: Contains system constants (`IConstant`), database connection configurations (`DBConnection`), and shared utility classes (`ValidationUtil`).
- **`dto/`** _(Data Transfer Objects)_: Defines core data models (e.g., `Room`, `Staff`, `Guest`, `Booking`) to securely transfer data across different application layers.
- **`dao/`** _(Data Access Objects)_: Handles direct database interactions. Utilizes a centralized `BaseDAO` and JDBC for secure, efficient, and optimized SQL queries.
- **`controller/`**: Manages application logic and routes, categorized by user roles:
  - `admin/`: Handles system, staff, and room management logic.
  - `guest/`: Processes the booking flow and guest-related actions.
- **`webapp/WEB-INF/views/`**: Contains all JSP pages. The UI is organized into modular Layouts and Components (Header, Footer, Navbar) to maximize code reusability.
