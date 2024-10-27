package jpja.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jpja.webapp.entities.Role;
import jpja.webapp.entities.User;
import jpja.webapp.repositories.RoleRepository;
import jpja.webapp.repositories.UserRepository;
import jpja.webapp.service.CustomUserDetailsService;

@Controller
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthController(CustomUserDetailsService userDetailsService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

    }

    // Display login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Display registration page
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());  // For the registration form
        return "register";
    }

    // Handle user registration
    @PostMapping("/register")
    public String registerUser(@RequestParam("email") String username,
                               @RequestParam("password") String password,
                               @RequestParam("role") String roleName,  // "VENDOR" or "CUSTOMER"
                               RedirectAttributes redirectAttributes) {

        if (userRepository.findByEmail(username) != null) {
            redirectAttributes.addFlashAttribute("error", "Username already exists.");
            return "redirect:/register";
        }
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid role name");
            return "redirect:/register";
        }

        // Create and save new user
        User newUser = new User();
        newUser.setEmail(username);
        newUser.setPassword(password);
        newUser.addRole(role);  // Assign the role (VENDOR/CUSTOMER)

        userDetailsService.saveUser(newUser);

        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    // Post-login handler, redirects based on role
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();  // Get user's role

        if (role.equals("ROLE_VENDOR")) {
            return "redirect:/vendor/dashboard";
        } else if (role.equals("ROLE_CUSTOMER")) {
            return "redirect:/customer/dashboard";
        }
        return "redirect:/";  // Fallback to home
    }
}
