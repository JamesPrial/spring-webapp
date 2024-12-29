package jpja.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling the homepage and landing page requests.
 * 
 * <p>This controller defines a single endpoint to serve the landing page of the application.</p>
 * 
 * <p>Annotations Used:</p>
 * <ul>
 *   <li>{@link Controller} - Marks this class as a Spring MVC controller.</li>
 *   <li>{@link GetMapping} - Maps HTTP GET requests to specific handler methods.</li>
 * </ul>
 * 
 * @author James Prial
 */
@Controller
public class HomepageController {

    /**
     * Handles the root ("/") endpoint and returns the landing page.
     * 
     * @return the name of the HTML file for the landing page ("landing.html")
     */
    @GetMapping("/")
    public String showLanding() {
        return "landing.html";
    }
}
