package br.com.microsservices.academy.core.models;

import lombok.*;

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
}
