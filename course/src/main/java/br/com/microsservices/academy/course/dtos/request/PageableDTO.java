package br.com.microsservices.academy.course.dtos.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
public class PageableDTO {
    @Min(value = 1, message = "validation.invalidValue")
    private Integer pageNumber = 2;
    @Min(value = 1, message = "validation.invalidValue")
    private Integer pageSize = 3;
}
