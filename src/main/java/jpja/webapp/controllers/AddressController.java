
package jpja.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import jpja.webapp.factories.DTOMapper;
import jpja.webapp.model.dto.AddressDTO;
import jpja.webapp.model.entities.Customer;
import jpja.webapp.service.AddressService;
import jpja.webapp.service.AuthenticationService;
import jpja.webapp.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for managing customer addresses.
 * This controller handles requests related to displaying and adding customer
 * addresses.
 * 
 * @author James Prial
 */

@Controller
@RequestMapping("/customer/address")
public class AddressController {
    private final AddressService addressService;
    private final CustomerService customerService;
    private final AuthenticationService authenticationService;

    /**
     * Constructs an AddressController with the specified services.
     *
     * @param addressService        the service used for address operations
     * @param customerService       the service used for customer operations
     * @param authenticationService the service used for authentication
     */

    public AddressController(AddressService addressService, CustomerService customerService,
            AuthenticationService authenticationService) {
        this.addressService = addressService;
        this.customerService = customerService;
        this.authenticationService = authenticationService;
    }

    /**
     * Displays a list of addresses associated with the current authenticated
     * customer.
     * This method populates the model with the necessary data to render the address
     * view.
     *
     * @param model the model object to which attributes can be added for use in the
     *              view
     * @return the name of the view template to be rendered
     */

    @GetMapping
    public String showAddresses(Model model) {
        model.addAttribute("addresses",
                DTOMapper.toDTOSet(customerService.getAddresses((Customer) authenticationService.getCurrentUser())));
        model.addAttribute("newAddress", addressService.getAddressDTO());
        model.addAttribute("types", addressService.getTypesExcludingDefaultAndRemoved());
        return "customer/address.html";
    }

    /**
     * Handles the submission of a new address from the form.
     * Validates the input data and, if valid, adds the new address for the current
     * authenticated customer.
     *
     * @param newAddress the address data transfer object containing the new address
     *                   details
     * @param result     the binding result that holds any validation errors
     * @param model      the model object that can be populated with attributes for
     *                   the view in case of errors
     * @return a redirect URL to the address listing if successful, or the same view
     *         with error messages if validation fails
     */
    @PostMapping
    public String addAddress(@ModelAttribute @Valid AddressDTO newAddress, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            customerService.addAddress((Customer) authenticationService.getCurrentUser(),
                    addressService.createAndSaveNewAddress(newAddress));
        }
        return "redirect:/customer/address";

    }
}
