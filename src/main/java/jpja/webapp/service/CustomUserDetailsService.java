package jpja.webapp.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jpja.webapp.exceptions.EmailNotVerifiedException;
import jpja.webapp.exceptions.EmailTakenException;
import jpja.webapp.exceptions.UsernameTakenException;
import jpja.webapp.exceptions.database.ConflictingDataException;
import jpja.webapp.factories.UserDTOFactory;
import jpja.webapp.model.dto.ModifierDTO;
import jpja.webapp.model.dto.UserIncomingDTO;
import jpja.webapp.model.dto.UserOutgoingDTO;
import jpja.webapp.model.entities.Admin;
import jpja.webapp.model.entities.LoginRecord;
import jpja.webapp.model.entities.Role;
import jpja.webapp.model.entities.User;
import jpja.webapp.repositories.AdminRepository;
import jpja.webapp.repositories.RoleRepository;
import jpja.webapp.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing user-related operations and integration with
 * Spring Security.
 * Implements {@link UserDetailsService} to provide custom authentication logic.
 * 
 * @author James Prial
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code CustomUserDetailsService} with the specified
     * dependencies.
     * 
     * @param userRepository  Repository for {@link User} entities.
     * @param adminRepository Repository for {@link Admin} entities.
     * @param roleRepository  Repository for {@link Role} entities.
     * @param passwordEncoder Encoder for encrypting passwords.
     */
    public CustomUserDetailsService(UserRepository userRepository, AdminRepository adminRepository,
            RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads a {@link UserDetails} object for authentication based on the given
     * username or email.
     * 
     * @param identifier The username or email used for authentication.
     * @return A {@link UserDetails} object containing user credentials and roles.
     * @throws AuthenticationException If the user cannot be authenticated (e.g.,
     *                                 email not verified).
     */
    @Override
    public UserDetails loadUserByUsername(String identifier) throws AuthenticationException {
        User user = findUser(identifier);
        if (user.getVerificationLevel() < 1) {
            throw new EmailNotVerifiedException("Please verify your email to log in");
        }
        Set<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority())) // Map Role to GrantedAuthority
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    /**
     * Finds a {@link User} entity by username.
     * 
     * @param username The username of the user to find.
     * @return The {@link User} associated with the given username.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username: " + username + " not found in database"));
    }

    /**
     * Finds a {@link User} entity by email address.
     * 
     * @param email The email of the user to find.
     * @return The {@link User} associated with the given email.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with email: " + email + " not found in database"));
    }

    /**
     * Encrypts a plaintext password using the {@link BCryptPasswordEncoder}.
     * 
     * @param password The plaintext password to encrypt.
     * @return The hashed password.
     */
    private String encrypt(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Saves a {@link User} entity to the database.
     * 
     * @param user The user to save.
     */
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Registers a new {@link User} by encrypting their password and saving them to
     * the database.
     * 
     * @param user The user to register.
     */
    public void registerUser(User user) {
        user.setPassword(this.encrypt(user.getPassword()));
        save(user);
    }

    /**
     * Verifies a user's email address by increasing their verification level.
     * 
     * @param email The email of the user to verify.
     */
    public void verifyUser(String email) {
        verifyUser(this.findUser(email));
    }

    /**
     * Verifies a {@link User} by increasing their verification level.
     * 
     * @param user The user to verify.
     */
    public void verifyUser(User user) {
        user.setVerificationLevel(user.getVerificationLevel() + 1);
        save(user);
    }

    /**
     * Finds a {@link User} entity by their username or email address.
     * 
     * @param identifier The username or email of the user to find.
     * @return The {@link User} associated with the given identifier.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public User findUser(String identifier) {
        return userRepository.findByUsernameOrEmail(identifier)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username/email: " + identifier + " not found"));
    }

    /**
     * Finds a {@link User} entity by their unique ID.
     * 
     * @param id The ID of the user to find.
     * @return The {@link User} associated with the given ID.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found in database"));
    }

    /**
     * Retrieves the total number of users in the system.
     * 
     * @return The total count of users.
     */
    public long userCount() {
        return userRepository.count();
    }

    /**
     * Checks if a given email address exists in the database.
     * 
     * @param email The email address to check.
     * @return {@code true} if the email exists, {@code false} otherwise.
     */
    public boolean doesEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Checks if a given username exists in the database.
     * 
     * @param username The username to check.
     * @return {@code true} if the username exists, {@code false} otherwise.
     */
    public boolean doesUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Retrieves all users in the database.
     * 
     * @return A list of all {@link User} entities.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a {@link Role} by its name.
     * 
     * @param role The name of the role.
     * @return The {@link Role} associated with the given name.
     * @throws IllegalArgumentException If the role cannot be found.
     */
    public Role findRoleByName(String role) {
        return roleRepository.findByName(role).orElseThrow(
                () -> new IllegalArgumentException("Role with name: " + role + " does not exist or cannot be found"));
    }

    /**
     * Finds a {@link Role} by its ID.
     * 
     * @param id The ID of the role.
     * @return The {@link Role} associated with the given ID.
     * @throws IllegalArgumentException If the role cannot be found.
     */
    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Role with id: " + id + " does not exist or cannot be found"));
    }

    /**
     * Retrieves all roles in the database.
     * 
     * @return A list of all {@link Role} entities.
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Deletes a user from the database by their ID.
     * 
     * @param id The ID of the user to delete.
     */
    public void deleteUser(long id) {
        userRepository.delete(this.findUserById(id));
    }

    /**
     * Modifies a user based on new user information provided via a
     * {@link UserIncomingDTO}.
     * 
     * @param id          The ID of the user to modify.
     * @param newUserInfo The updated user information.
     * @throws EmailTakenException      If the new email is already taken.
     * @throws ConflictingDataException If the provided data conflicts with existing
     *                                  data.
     */
    public void modifyUser(Long id, UserIncomingDTO newUserInfo) throws EmailTakenException, ConflictingDataException {
        modifyUser(findUserById(id).getEmail(), newUserInfo);
    }

    /**
     * Modifies a user based on new user information provided via a
     * {@link UserIncomingDTO}.
     * 
     * @param identifier  The identifier (email or username) of the user to modify.
     * @param newUserInfo The updated user information.
     * @throws EmailTakenException      If the new email is already taken.
     * @throws ConflictingDataException If the provided data conflicts with existing
     *                                  data.
     * @throws IllegalArgumentException If no user info is provided.
     */
    public void modifyUser(String identifier, UserIncomingDTO newUserInfo)
            throws EmailTakenException, ConflictingDataException {
        if (newUserInfo == null) {
            throw new IllegalArgumentException("No user info provided");
        }
        if (newUserInfo.getEmail() != null || newUserInfo.getUsername() != null || newUserInfo.getPassword() != null
                || !newUserInfo.getRoles().isEmpty()) {
            User existingUser = findUser(identifier);
            if (newUserInfo.getEmail() != null && !existingUser.getEmail().equals(newUserInfo.getEmail())) {
                if (userRepository.existsByEmail(newUserInfo.getEmail())) {
                    throw new EmailTakenException();
                }
                existingUser.setEmail(newUserInfo.getEmail());
            }
            if (newUserInfo.getUsername() != null && !existingUser.getUsername().equals(newUserInfo.getUsername())) {
                if (userRepository.existsByUsername(newUserInfo.getUsername())) {
                    throw new UsernameTakenException();
                }
                existingUser.setUsername(newUserInfo.getUsername());
            }
            if (newUserInfo.getPassword() != null) {
                existingUser.setPassword(newUserInfo.getPassword());
            }
            if (!newUserInfo.getRoles().isEmpty()) {
                existingUser.setRoles(new HashSet<Role>());
                for (ModifierDTO roleName : newUserInfo.getRoles()) {
                    existingUser.addRole(unwrapRoleDTO(roleName));
                }
            }
            this.save(existingUser);
        }
    }

    /**
     * Converts a {@link ModifierDTO} to a {@link Role}.
     * 
     * @param dto The {@link ModifierDTO} containing role data.
     * @return The corresponding {@link Role}.
     * @throws ConflictingDataException If the DTO's ID and name do not match.
     * @throws IllegalArgumentException If the DTO is null or all fields are null.
     */
    public Role unwrapRoleDTO(ModifierDTO dto) throws ConflictingDataException {
        if (dto == null || (dto.getId() == null && dto.getName() == null)) {
            throw new IllegalArgumentException("No DTO given or all fields null");
        }
        Role role;
        if (dto.getId() == null) {
            role = findRoleByName(dto.getName());
        } else if (dto.getName() == null) {
            role = findRoleById(dto.getId());
        } else {
            role = findRoleById(dto.getId());
            if (!role.getName().equals(dto.getName())) {
                throw new ConflictingDataException("Role id and name don't match!");
            }
        }
        return role;
    }

    /**
     * Creates a list of {@link UserOutgoingDTO} objects based on a list of
     * {@link User} entities.
     * 
     * @param users The list of users.
     * @return A list of corresponding {@link UserOutgoingDTO} objects.
     */
    public List<UserOutgoingDTO> createUserOutputDTOList(List<User> users) {
        List<UserOutgoingDTO> ret = new ArrayList<>();
        for (User user : users) {
            ret.add(UserDTOFactory.copyToUserOutgoingDTO(user));
        }
        return ret;
    }

    /**
     * Saves the admin-specific information, such as the last login record.
     * 
     * @param user        The user to save admin information for.
     * @param loginRecord The last login record to associate with the admin.
     * @throws IllegalArgumentException If the user is not an admin or is null.
     */
    public void saveAdminInfo(User user, LoginRecord loginRecord) {
        if (user != null && hasRole(user, "ROLE_ADMIN")) {
            Admin admin = (Admin) user;
            admin.setLastLogin(loginRecord);
            adminRepository.save(admin);
        } else {
            throw new IllegalArgumentException("User is not an admin, or no user given");
        }
    }

    /**
     * Checks if a user has a specific role by name.
     * 
     * @param user     The user to check.
     * @param roleName The name of the role to check.
     * @return {@code true} if the user has the role, {@code false} otherwise.
     */
    public boolean hasRole(User user, String roleName) {
        return hasRole(user, findRoleByName(roleName));
    }

    /**
     * Checks if a user has a specific role.
     * 
     * @param user The user to check.
     * @param role The role to check.
     * @return {@code true} if the user has the role, {@code false} otherwise.
     */
    public boolean hasRole(User user, Role role) {
        if (user == null) {
            return false;
        }
        for (Role currentRole : user.getRoles()) {
            if (role.equals(currentRole)) {
                return true;
            }
        }
        return false;
    }
}
