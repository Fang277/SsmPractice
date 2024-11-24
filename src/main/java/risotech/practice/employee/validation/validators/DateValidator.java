package risotech.practice.employee.validation.validators;


import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import risotech.practice.employee.validation.annotations.ValidDate;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    //private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$"; // 正则表达式

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
        	context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("birthday項目は空欄にできません").addConstraintViolation();
            return false; // 如果允许空值，通过校验
        }
        
        // 1. 检查格式是否符合正则表达式
        if (!value.matches(DATE_REGEX)) {
            // 设置错误信息为格式不正确
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("birthday項目の形式はYYYYMMDDで").addConstraintViolation();
            return false;
        }
        
        try {
            // 校验日期格式
        	//LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT));
        	//System.out.println("LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT));" + LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT)));
        	
        	LocalDate.of(Integer.parseInt(value.substring(0,4)), Integer.parseInt(value.substring(5,7)), Integer.parseInt(value.substring(8,10)));
            return true; //
        } catch (Exception e) {
        	context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("birthday項目の日付が無効です").addConstraintViolation();
            return false;
        }
    }
}