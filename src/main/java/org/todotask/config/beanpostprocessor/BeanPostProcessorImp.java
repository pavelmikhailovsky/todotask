package org.todotask.config.beanpostprocessor;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.todotask.util.asm.ApiResponseContentSchema;
import org.todotask.util.asm.ClassForContentSchema;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *Not deleted because the plans were to substitute the created class in
 *runtime for auto-documentation into the annotation @Schema
 **/
public class BeanPostProcessorImp implements BeanPostProcessor {
    @Override
    @SneakyThrows
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        setFieldInBean(bean);
        return bean;
    }

    @SneakyThrows
    private void setFieldInBean(Object bean) {
        Set<Field> fields = Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ClassForContentSchema.class))
                .collect(Collectors.toSet());

        for (Field field : fields) {
            field.setAccessible(true);
            String value = field.getAnnotation(ClassForContentSchema.class).value();
            field.set(bean, getClassContentSchema(value));
        }

    }

    private Class<?> getClassContentSchema(String value) {
        return ApiResponseContentSchema.getNewClassWithField(value);
    }
}
