package employeeEnrollment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "forward:/admin-dashboard.html";
    }

    @GetMapping("/employee-dashboard")
    public String employeeDashboard() {
        return "forward:/employee-dashboard.html";
    }
}
