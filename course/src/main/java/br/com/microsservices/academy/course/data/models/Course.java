package br.com.microsservices.academy.course.data.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Course extends BaseModel{
    @EqualsAndHashCode.Include
    private Long id;
    private String title;
}
