package com.ssafy.enjoytrip.global.enumvalidator;

import com.ssafy.enjoytrip.global.enumvalidator.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidator.class) // Validator 클래스 연결
@Target({ElementType.FIELD})                  // 필드에서 사용
@Retention(RetentionPolicy.RUNTIME)           // 런타임에 유지
public @interface EnumValue {
    String message() default "Invalid value. Must be one of the defined enum values.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass(); // Enum 클래스를 지정
}

