package com.hjrpc.assistspringboot;

import com.hjrpc.assistspringboot.annoation.EnableDict;
import com.hjrpc.assistspringboot.util.ClassAddFieldUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class AutoDictRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        log.info("Dict init ...");
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableDict.class.getCanonicalName());
        Set<String> dtoClassPath = Arrays.stream((String[]) Objects.requireNonNull(attributes).get("value"))
                .flatMap(this::getDtoPaths)
                .collect(Collectors.toSet());
        // 修改字节码生成字符串属性
        Set<Class<?>> containDictClass = ClassAddFieldUtil.compile(dtoClassPath);

        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(ClassSetBean.class);
        definition.addPropertyValue("containDictClass", containDictClass);
        registry.registerBeanDefinition("classSetBean", definition.getBeanDefinition());
    }

    private Stream<String> getDtoPaths(String packagePath) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        packagePath = packagePath.replaceAll("\\.", "/");
        packagePath = "classpath*:" + packagePath + "/**/*.class";
        try {
            Resource[] resources = resolver.getResources(packagePath);
            String[] dtoPathArray = new String[resources.length];
            for (int i = 0; i < resources.length; i++) {
                // 先获取resource的元信息，然后获取class元信息，最后得到 class 全路径
                String clsName = new SimpleMetadataReaderFactory().getMetadataReader(resources[i]).getClassMetadata().getClassName();
                // 通过名称加载
                dtoPathArray[i] = clsName;
            }
            return Arrays.stream(dtoPathArray);
        } catch (IOException e) {
            throw new IllegalArgumentException("parse packagePath error : {}" + e);
        }
    }
}