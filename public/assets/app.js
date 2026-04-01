(function () {
    const isFileProtocol = window.location.protocol === "file:";
    const railwayApiBaseUrl = "https://employee-enrolment-production.up.railway.app";
    const frontendBaseUrl = window.location.origin;
    const apiBaseUrl = isFileProtocol ? "http://localhost:8080" : railwayApiBaseUrl;

    const storageKeys = {
        token: "employee_enrollment_token",
        role: "employee_enrollment_role",
        username: "employee_enrollment_username"
    };

    const routes = {
        admin: frontendBaseUrl + "/admin-dashboard.html",
        employee: frontendBaseUrl + "/employee-dashboard.html",
        login: frontendBaseUrl + "/"
    };

    function getToday() {
        return new Date().toISOString().split("T")[0];
    }

    function getAuth() {
        return {
            token: localStorage.getItem(storageKeys.token),
            role: localStorage.getItem(storageKeys.role),
            username: localStorage.getItem(storageKeys.username)
        };
    }

    function setAuth(data) {
        localStorage.setItem(storageKeys.token, data.token);
        localStorage.setItem(storageKeys.role, data.role);
        localStorage.setItem(storageKeys.username, data.username);
    }

    function clearAuth() {
        localStorage.removeItem(storageKeys.token);
        localStorage.removeItem(storageKeys.role);
        localStorage.removeItem(storageKeys.username);
    }

    function navigateByRole(role) {
        window.location.href = role === "ADMIN" ? routes.admin : routes.employee;
    }

    function showMessage(element, text, type) {
        if (!element) {
            return;
        }
        element.textContent = text;
        element.className = "message " + type;
    }

    function hideMessage(element) {
        if (!element) {
            return;
        }
        element.textContent = "";
        element.className = "message hidden";
    }

    async function apiFetch(url, options) {
        const auth = getAuth();
        const headers = Object.assign(
            { "Content-Type": "application/json" },
            options && options.headers ? options.headers : {}
        );

        if (auth.token) {
            headers.Authorization = "Bearer " + auth.token;
        }

        let response;

        try {
            response = await fetch(apiBaseUrl + url, Object.assign({}, options, { headers }));
        } catch (error) {
            throw new Error("Cannot connect to backend server on " + apiBaseUrl);
        }

        const text = await response.text();
        let payload = {};

        try {
            payload = text ? JSON.parse(text) : {};
        } catch (error) {
            payload = {};
        }

        if (!response.ok) {
            const message = Array.isArray(payload.data)
                ? payload.data.join(", ")
                : payload.message || "Request failed";
            throw new Error(message);
        }

        return payload;
    }

    function buildEmployeePayload(form) {
        const formData = new FormData(form);
        const salaryValue = formData.get("salary");
        return {
            firstName: formData.get("firstName") || "",
            lastName: formData.get("lastName") || "",
            email: formData.get("email") || "",
            phone: formData.get("phone") || "",
            department: formData.get("department") || "",
            role: formData.get("role") || "EMPLOYEE",
            salary: salaryValue === null || salaryValue === "" ? null : Number(salaryValue),
            joiningDate: formData.get("joiningDate") || "",
            address: formData.get("address") || "",
            username: formData.get("username") || "",
            password: formData.get("password") || ""
        };
    }

    function applyDateConstraints() {
        const today = getToday();
        document.querySelectorAll('input[name="joiningDate"]').forEach(function (input) {
            input.max = today;
        });
    }

    function ensureAuthorized(requiredRole) {
        const auth = getAuth();
        if (!auth.token || !auth.role) {
            clearAuth();
            window.location.href = routes.login;
            return false;
        }

        if (requiredRole && auth.role !== requiredRole) {
            navigateByRole(auth.role);
            return false;
        }

        return true;
    }

    function attachLogout() {
        const logoutButton = document.getElementById("logoutButton");
        if (!logoutButton) {
            return;
        }

        logoutButton.addEventListener("click", function () {
            clearAuth();
            window.location.href = routes.login;
        });
    }

    function renderMetrics(container, items) {
        if (!container) {
            return;
        }
        container.innerHTML = items.map(function (item) {
            return [
                '<article class="metric-card">',
                "<strong>" + item.label + "</strong>",
                '<div class="metric-value">' + item.value + "</div>",
                "</article>"
            ].join("");
        }).join("");
    }

    function initializeLoginPage() {
        const loginForm = document.getElementById("loginForm");
        if (!loginForm) {
            return;
        }

        const auth = getAuth();
        if (auth.token && auth.role) {
            navigateByRole(auth.role);
            return;
        }

        const authMessage = document.getElementById("authMessage");
        const adminRegisterForm = document.getElementById("adminRegisterForm");
        const toggleButton = document.getElementById("toggleAdminRegister");

        toggleButton.addEventListener("click", function () {
            adminRegisterForm.classList.toggle("hidden");
        });

        loginForm.addEventListener("submit", async function (event) {
            event.preventDefault();
            hideMessage(authMessage);

            try {
                const formData = new FormData(loginForm);
                const result = await apiFetch("/auth/login", {
                    method: "POST",
                    body: JSON.stringify({
                        usernameOrEmail: formData.get("usernameOrEmail"),
                        password: formData.get("password")
                    }),
                    headers: {}
                });

                setAuth(result.data);
                showMessage(authMessage, result.message || "Login successful", "success");
                setTimeout(function () {
                    navigateByRole(result.data.role);
                }, 400);
            } catch (error) {
                showMessage(authMessage, error.message, "error");
            }
        });

        adminRegisterForm.addEventListener("submit", async function (event) {
            event.preventDefault();
            hideMessage(authMessage);

            try {
                const result = await apiFetch("/auth/register-admin", {
                    method: "POST",
                    body: JSON.stringify(buildEmployeePayload(adminRegisterForm)),
                    headers: {}
                });

                adminRegisterForm.reset();
                adminRegisterForm.classList.add("hidden");
                showMessage(authMessage, result.message || "Admin created successfully", "success");
            } catch (error) {
                showMessage(authMessage, error.message, "error");
            }
        });
    }

    function renderEmployeeTable(employees) {
        const wrap = document.getElementById("employeeTableWrap");

        if (!employees.length) {
            wrap.innerHTML = '<div class="empty-state">No employees found yet.</div>';
            return;
        }

        wrap.innerHTML = [
            "<table>",
            "<thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Department</th><th>Role</th><th>Status</th><th>Actions</th></tr></thead>",
            "<tbody>",
            employees.map(function (employee) {
                const nextStatus = employee.status === "ACTIVE" ? "INACTIVE" : "ACTIVE";
                return [
                    "<tr>",
                    "<td>" + employee.empId + "</td>",
                    "<td>" + employee.firstName + " " + employee.lastName + "<br><small>" + employee.username + "</small></td>",
                    "<td>" + employee.email + "<br><small>" + employee.phone + "</small></td>",
                    "<td>" + employee.department + "</td>",
                    "<td>" + employee.role + "</td>",
                    "<td>" + employee.status + "</td>",
                    '<td><div class="row-actions">',
                    '<button class="btn btn-secondary btn-small" data-action="edit" data-id="' + employee.empId + '">Edit</button>',
                    '<button class="btn btn-secondary btn-small" data-action="toggle-status" data-status="' + nextStatus + '" data-id="' + employee.empId + '">' + nextStatus + "</button>",
                    "</div></td>",
                    "</tr>"
                ].join("");
            }).join(""),
            "</tbody></table>"
        ].join("");
    }

    async function initializeAdminPage() {
        if (!document.getElementById("employeeForm")) {
            return;
        }

        if (!ensureAuthorized("ADMIN")) {
            return;
        }

        attachLogout();
        const auth = getAuth();
        document.getElementById("adminIdentity").textContent = auth.username + " | " + auth.role;

        const form = document.getElementById("employeeForm");
        const message = document.getElementById("adminMessage");
        const resetButton = document.getElementById("resetEmployeeForm");
        const refreshButton = document.getElementById("refreshEmployees");
        const summary = document.getElementById("employeeSummary");
        const salaryFieldGroup = document.getElementById("salaryFieldGroup");
        let employees = [];

        function setCreateMode(isCreateMode) {
            if (!salaryFieldGroup) {
                return;
            }
            salaryFieldGroup.classList.toggle("hidden", isCreateMode);
            form.elements.salary.required = !isCreateMode;
            if (isCreateMode) {
                form.elements.salary.value = "";
            }
        }

        function fillForm(employee) {
            form.elements.empId.value = employee.empId;
            form.elements.firstName.value = employee.firstName;
            form.elements.lastName.value = employee.lastName;
            form.elements.email.value = employee.email;
            form.elements.phone.value = employee.phone;
            form.elements.department.value = employee.department;
            form.elements.role.value = employee.role;
            form.elements.salary.value = employee.salary;
            form.elements.joiningDate.value = employee.joiningDate;
            form.elements.address.value = employee.address;
            form.elements.username.value = employee.username;
            form.elements.password.value = "";
            setCreateMode(false);
            showMessage(message, "Editing employee #" + employee.empId + ". Enter a password to save changes.", "success");
            window.location.hash = "editor";
        }

        function resetForm() {
            form.reset();
            form.elements.empId.value = "";
            form.elements.role.value = "EMPLOYEE";
            setCreateMode(true);
            hideMessage(message);
        }

        function updateSummary(data) {
            const activeCount = data.filter(function (employee) {
                return employee.status === "ACTIVE";
            }).length;
            const adminCount = data.filter(function (employee) {
                return employee.role === "ADMIN";
            }).length;

            renderMetrics(summary, [
                { label: "Total Employees", value: data.length },
                { label: "Active", value: activeCount },
                { label: "Admins", value: adminCount }
            ]);
        }

        async function loadEmployees() {
            try {
                const result = await apiFetch("/employees", { method: "GET" });
                employees = result.data || [];
                updateSummary(employees);
                renderEmployeeTable(employees);
            } catch (error) {
                showMessage(message, error.message, "error");
            }
        }

        form.addEventListener("submit", async function (event) {
            event.preventDefault();
            hideMessage(message);

            try {
                const id = form.elements.empId.value;
                const result = await apiFetch(id ? "/employees/" + id : "/employees/enroll", {
                    method: id ? "PUT" : "POST",
                    body: JSON.stringify(buildEmployeePayload(form))
                });

                showMessage(message, result.message || "Employee saved successfully", "success");
                resetForm();
                await loadEmployees();
            } catch (error) {
                showMessage(message, error.message, "error");
            }
        });

        resetButton.addEventListener("click", resetForm);
        refreshButton.addEventListener("click", loadEmployees);

        document.getElementById("employeeTableWrap").addEventListener("click", async function (event) {
            const button = event.target.closest("button[data-action]");
            if (!button) {
                return;
            }

            const action = button.getAttribute("data-action");
            const id = button.getAttribute("data-id");

            if (action === "edit") {
                const employee = employees.find(function (item) {
                    return String(item.empId) === String(id);
                });
                if (employee) {
                    fillForm(employee);
                }
                return;
            }

            if (action === "toggle-status") {
                try {
                    const status = button.getAttribute("data-status");
                    const result = await apiFetch("/employees/" + id + "/status", {
                        method: "PUT",
                        body: JSON.stringify({ status: status })
                    });
                    showMessage(message, result.message || "Status updated", "success");
                    await loadEmployees();
                } catch (error) {
                    showMessage(message, error.message, "error");
                }
            }
        });

        resetForm();
        await loadEmployees();
    }

    function renderProfileCard(employee) {
        const card = document.getElementById("employeeProfileCard");
        card.innerHTML = [
            '<div class="profile-item"><strong>Employee ID</strong>' + employee.empId + "</div>",
            '<div class="profile-item"><strong>Name</strong>' + employee.firstName + " " + employee.lastName + "</div>",
            '<div class="profile-item"><strong>Email</strong>' + employee.email + "</div>",
            '<div class="profile-item"><strong>Phone</strong>' + employee.phone + "</div>",
            '<div class="profile-item"><strong>Department</strong>' + employee.department + "</div>",
            '<div class="profile-item"><strong>Role</strong>' + employee.role + "</div>",
            '<div class="profile-item"><strong>Salary</strong>' + employee.salary + "</div>",
            '<div class="profile-item"><strong>Joining Date</strong>' + employee.joiningDate + "</div>",
            '<div class="profile-item"><strong>Username</strong>' + employee.username + "</div>",
            '<div class="profile-item"><strong>Status</strong>' + employee.status + "</div>",
            '<div class="profile-item" style="grid-column: 1 / -1;"><strong>Address</strong>' + employee.address + "</div>"
        ].join("");
    }

    async function initializeEmployeePage() {
        const form = document.getElementById("employeeProfileForm");
        if (!form) {
            return;
        }

        if (!ensureAuthorized("EMPLOYEE")) {
            return;
        }

        attachLogout();
        const auth = getAuth();
        document.getElementById("employeeIdentity").textContent = auth.username + " | " + auth.role;
        const message = document.getElementById("employeeMessage");
        const quickStats = document.getElementById("employeeQuickStats");

        async function loadProfile() {
            try {
                const result = await apiFetch("/employees/me", { method: "GET" });
                const employee = result.data;
                renderProfileCard(employee);
                renderMetrics(quickStats, [
                    { label: "Role", value: employee.role },
                    { label: "Status", value: employee.status },
                    { label: "Department", value: employee.department }
                ]);

                form.elements.firstName.value = employee.firstName;
                form.elements.lastName.value = employee.lastName;
                form.elements.email.value = employee.email;
                form.elements.phone.value = employee.phone;
                form.elements.department.value = employee.department;
                form.elements.role.value = employee.role;
                form.elements.joiningDate.value = employee.joiningDate;
                form.elements.address.value = employee.address;
                form.elements.username.value = employee.username;
                form.elements.password.value = "";
            } catch (error) {
                showMessage(message, error.message, "error");
            }
        }

        form.addEventListener("submit", async function (event) {
            event.preventDefault();
            hideMessage(message);

            try {
                const payload = buildEmployeePayload(form);
                payload.role = form.elements.role.value;
                const result = await apiFetch("/employees/me", {
                    method: "PUT",
                    body: JSON.stringify(payload)
                });

                showMessage(message, result.message || "Profile updated", "success");
                await loadProfile();
            } catch (error) {
                showMessage(message, error.message, "error");
            }
        });

        await loadProfile();
    }

    applyDateConstraints();
    initializeLoginPage();
    initializeAdminPage();
    initializeEmployeePage();
})();
