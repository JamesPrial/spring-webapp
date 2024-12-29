package jpja.webapp.factories;

import jpja.webapp.model.entities.Admin;
import jpja.webapp.model.entities.Customer;
import jpja.webapp.model.entities.Role;
import jpja.webapp.model.entities.User;
import jpja.webapp.model.entities.Vendor;

public class UserFactory {

    /**
     * Creates a User instance based on the provided role.
     *
     * @param email    The email address of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param role     The role assigned to the user. Must be one of ROLE_CUSTOMER,
     *                 ROLE_VENDOR, or ROLE_ADMIN.
     * @return A User instance, which could be a Customer, Vendor, or Admin
     *         depending on the role.
     * @throws IllegalArgumentException if the provided role is unknown.
     */
    public static User createUser(String email, String username, String password, Role role) {
        if (role.getName().equals("ROLE_CUSTOMER")) {
            return new Customer(email, username, password, role);
        } else if (role.getName().equals("ROLE_VENDOR")) {
            return new Vendor(email, username, password, role);
        } else if (role.getName().equals("ROLE_ADMIN")) {
            return new Admin(email, username, password, role);
        } else {
            throw new IllegalArgumentException("Unknown role: " + role.getName());
        }
    }
}
