package employeeEnrollment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    // Serve Login Page (ROOT)
    @GetMapping("/")
    public String index() {
        return "index";  // index.html
    }

    // Serve Admin Dashboard
    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard";  // admin-dashboard.html
    }

    // Serve Employee Dashboard
    @GetMapping("/employee-dashboard")
    public String employeeDashboard() {
        return "employee-dashboard";  // employee-dashboard.html
    }
}

