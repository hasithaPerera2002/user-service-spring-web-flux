package com.ijse.gdse.cw.hasitha.userService.validators;

import com.ijse.gdse.cw.hasitha.userService.exceptions.NotValidObjectException;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectValidator<T> {
private final ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
private final Validator validator=validatorFactory.getValidator();

public void validate(T object){
    Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
    if(!constraintViolations.isEmpty()){
       var violations= constraintViolations.stream().
                map(ConstraintViolation::getMessage).
               collect(Collectors.toSet());
       throw new NotValidObjectException(violations);
    }


}


}
