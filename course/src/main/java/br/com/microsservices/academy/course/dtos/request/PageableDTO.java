package br.com.microsservices.academy.course.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageableDTO {
    @Min(value = 1, message = "validation.invalidValue")
    private Integer pageNumber = 2;
    @Min(value = 1, message = "validation.invalidValue")
    private Integer pageSize = 3;
}
