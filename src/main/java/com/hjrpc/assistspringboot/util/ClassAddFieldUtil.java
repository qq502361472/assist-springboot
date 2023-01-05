package com.hjrpc.assistspringboot.util;

import com.hjrpc.assistspringboot.annoation.DictValue;
import javassist.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassAddFieldUtil {

    private static void editToStringMethod(CtClass cc) {
        CtMethod ctMethod;
        try {
            ctMethod = cc.getDeclaredMethod("toString");
        } catch (NotFoundException e) {
            return;
        }
        String name = cc.getName();
        CtField[] declaredFields = cc.getDeclaredFields();
        StringBuilder toStringBody = new StringBuilder("{ return \"" + name + "(");
        for (int i = 0; i < declaredFields.length; i++) {
            CtField field = declaredFields[i];
            String fieldName = field.getName();
            String getMethodString = UpperCaseUtil.getMethodString(field.getName());
            if (i == 0) {
                toStringBody.append(fieldName);
            } else {
                toStringBody.append(" + \", ").append(fieldName);
            }
            toStringBody.append("=\" + this.").append(getMethodString).append("()");
        }
        toStringBody.append(" + \")\";").append(" }");

        try {
            ctMethod.setBody(toStringBody.toString());
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }


    private static void generatorField(CtClass cc, CtField field) {
        DictValue annotation;
        try {
            annotation = (DictValue) field.getAnnotation(DictValue.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
        String targetFieldName = UpperCaseUtil.getTargetFieldName(annotation.targetFieldName(), field.getName());
        try {
            CtField targetField = CtField.make("private String " + targetFieldName + ";", cc);
            cc.addField(targetField);
            String upCaseTargetFieldName = UpperCaseUtil.toFirstUpperCase(targetFieldName);
            //增加相应的set和get方法
            cc.addMethod(CtNewMethod.getter("get" + upCaseTargetFieldName, targetField));
            cc.addMethod(CtNewMethod.setter("set" + upCaseTargetFieldName, targetField));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<Class<?>> compile(Set<String> containDictClass) {
        Set<Class<?>> containsDictSet = new HashSet<>();
        String[] dtoPaths = containDictClass.toArray(new String[]{});
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass[] ctClasses = new CtClass[0];
        try {
            ctClasses = pool.get(dtoPaths);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        for (CtClass cc : ctClasses) {
            CtField[] fields = cc.getDeclaredFields();
            List<CtField> collect = Arrays.stream(fields).filter(x -> x.hasAnnotation(DictValue.class)).collect(Collectors.toList());
            if (collect.isEmpty()) {
                continue;
            }
            collect.forEach(x -> generatorField(cc, x));
            // 更新toString方法
            editToStringMethod(cc);
            try {
                Class<?> aClass = cc.toClass();
                containsDictSet.add(aClass);
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
            cc.detach();
        }
        return containsDictSet;
    }

    public static boolean containsDictValue(String dtoPath) {
        return false;
    }
}
