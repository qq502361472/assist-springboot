package com.hjrpc.assistspringboot.aspect;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hjrpc.assistspringboot.ClassSetBean;
import com.hjrpc.assistspringboot.DictService;
import com.hjrpc.assistspringboot.annoation.DictValue;
import com.hjrpc.assistspringboot.util.UpperCaseUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
public class DictAspect {
    private final DictService dictService;
    private final ClassSetBean classSetBean;

    @Around("@annotation(dictValue)")
    public Object translation(final ProceedingJoinPoint point, DictValue dictValue) throws Throwable {
        Object result = point.proceed();
        if (result instanceof List) {
            ((List<?>) result).forEach(this::translate);
        } else if (result instanceof IPage) {
            ((IPage<?>) result).getRecords().forEach(this::translate);
        } else if (result instanceof PageInfo) {
            ((PageInfo<?>) result).getList().forEach(this::translate);
        } else {
            translate(result);
        }
        return result;
    }

    public void translate(Object obj) {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object fieldValue = ReflectionUtils.getField(field, obj);
            if (fieldValue == null) {
                continue;
            }
            if (fieldValue instanceof List) {
                ((List<?>) fieldValue).forEach(this::translate);
            } else if (fieldValue instanceof Set) {
                ((Set<?>) fieldValue).forEach(this::translate);
            } else {
                DictValue dictValue = field.getAnnotation(DictValue.class);
                if (dictValue == null) {
                    if (classSetBean.getContainDictClass().contains(fieldValue.getClass())) {
                        translate(fieldValue);
                    }
                }

                if (dictValue != null) {
                    String dictCode = dictValue.value();
                    dictCode = StringUtils.isEmpty(dictCode) ? dictValue.dictCode() : dictCode;
                    if (!StringUtils.isEmpty(dictCode)) {
                        String targetFieldName = UpperCaseUtil.getTargetFieldName(dictValue.targetFieldName(), field.getName());
                        Field declaredField;
                        try {
                            declaredField = clazz.getDeclaredField(targetFieldName);
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException();
                        }
                        declaredField.setAccessible(true);
                        ReflectionUtils.setField(declaredField, obj, dictService.getDictValue(fieldValue, dictCode));
                    }
                }
            }
        }
    }
}
