package br.com.microsservices.academy.course.data.models;


import java.time.LocalDateTime;

public abstract class BaseModel implements AbstractEntity {
    protected LocalDateTime createdDate;
}
