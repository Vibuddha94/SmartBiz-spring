package lk.acpt.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false)
    String name;
    //email cannot be duplicate
    @Column(unique = true, nullable = false)
    String email;
    //password cannot be duplicate
    @Column(unique = true, nullable = false)
    String password;
    @Column(nullable = false)
    String role;
    String status;
}
