package jpja.webapp.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    public Integer getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    
    // Getters and setters
}