package br.com.microsservices.academy.core.models;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User {
    @EqualsAndHashCode.Include
    private Long id;
    private String username;
    private String password;
    private String role = "USER"; // Como é demonstrativo, podemos setar apenas uma role por usuário. Como esta como string, basta adicionarmos separando por comma

    public User(@NotNull User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }
}
