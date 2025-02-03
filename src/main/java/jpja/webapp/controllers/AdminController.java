package jpja.webapp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import jpja.webapp.exceptions.database.ConflictingDataException;
import jpja.webapp.factories.UserDTOFactory;
import jpja.webapp.logging.AppLog;
import jpja.webapp.logging.Level;
import jpja.webapp.logging.Log;
import jpja.webapp.model.dto.ModifierDTO;
import jpja.webapp.model.dto.UserIncomingDTO;
import jpja.webapp.model.dto.UserOutgoingDTO;
import jpja.webapp.model.entities.Role;
import jpja.webapp.service.BookingService;
import jpja.webapp.service.CustomUserDetailsService;
import jpja.webapp.service.LogParserService;

/**
 * Controller responsible for handling administration-related requests,
 * including user management and dashboard statistics.
 * 
 * @author James Prial
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final CustomUserDetailsService userService;
    private final BookingService bookingService;
    private final LogParserService logService;

    public AdminController(CustomUserDetailsService userService, BookingService bookingService,
            LogParserService logService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.logService = logService;
    }

    /**
     * Displays the admin dashboard with user and booking counts.
     *
     * @param model the model object to populate view attributes
     * @return the name of the dashboard view template
     */
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("userCount", userService.userCount());
        model.addAttribute("bookingCount", bookingService.totalBookings());
        return "admin/dashboard";
    }

    /**
     * Displays a list of all users in the system along with their roles.
     *
     * @param model the model object to populate with user and role information
     * @return the name of the users view template
     */
    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userService.createUserOutputDTOList(userService.getAllUsers()));
        model.addAttribute("roles", userService.getAllRoles());
        return "admin/users";
    }

    /**
     * Shows the details of a specific user identified by their ID,
     * including the ability to modify user roles.
     *
     * @param id    the ID of the user
     * @param model the model object to populate with user and role information
     * @return the name of the edit user view template
     */
    @GetMapping("/user/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        UserOutgoingDTO user = UserDTOFactory.copyToUserOutgoingDTO(userService.findUserById(id));
        List<Role> roles = userService.getAllRoles(); // Fetch all roles

        model.addAttribute("user", user);
        model.addAttribute("roles", roles); // Add roles to the model
        return "admin/edit-user";
    }

    /**
     * Updates a user's information after validating the input.
     * This includes modifying the user's roles based on the selected roles.
     *
     * @param id     the ID of the user to update
     * @param user   the updated user data transfer object
     * @param result the binding result containing validation errors, if any
     * @param roles  the list of role IDs to assign to the user
     * @param model  the model object to populate with attributes
     * @return a redirect URL to the user list
     */
    @PostMapping("/user/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute @Valid UserIncomingDTO user, BindingResult result,
            @RequestParam List<Long> roles, Model model) {
        Set<ModifierDTO> roleNames = new HashSet<ModifierDTO>();
        for (Long role : roles) {
            roleNames.add(UserDTOFactory.copyToModifierDTOWithId(userService.findRoleById(role)));
        }
        user.setRoles(roleNames);
        try {
            userService.modifyUser(id, user);
        } catch (ConflictingDataException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    /**
     * Deletes a user identified by their ID from the system.
     *
     * @param id the ID of the user to be deleted
     * @return a redirect URL to the user list
     */
    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/logs")
    public String getLogs(
            @RequestParam(value = "logFile", required = false) String selectedLog,
            @RequestParam(name = "levelFilter", defaultValue = "ALL") String levelFilter, Model model)
            throws IOException {
        List<String> logFiles = logService.listAvailableLogFiles();
        model.addAttribute("availableLogs", logFiles);
        List<Level> allLevels = List.of(Level.values());
        List<String> levelsStr = new ArrayList<String>();
        for(Level level : allLevels){
            levelsStr.add(level.toString());
        }
        model.addAttribute("allLevels", levelsStr);

        model.addAttribute("selectedLog", selectedLog);
        model.addAttribute("levelFilter", levelFilter);

        if (selectedLog != null && !selectedLog.isEmpty()) {
            boolean isActivity = selectedLog.contains("activity");
            List<Log> logs = logService.parseLogFile(selectedLog, isActivity, levelFilter);
            List<Log> appLogs = isActivity ? null : logs;
            List<Log> activityLogs = isActivity ? logs : null;
        
            model.addAttribute("appLogs", appLogs);
            model.addAttribute("activityLogs", activityLogs);
        }
        return "admin/logs";
    }

}