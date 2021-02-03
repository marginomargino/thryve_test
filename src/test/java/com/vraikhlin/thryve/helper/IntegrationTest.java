package com.vraikhlin.thryve.helper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest
@Transactional
@TestPropertySource
@ActiveProfiles
public @interface IntegrationTest {

    @AliasFor(annotation = TestPropertySource.class, attribute = "properties")
    String[] properties() default {"spring.datasource.url=jdbc:tc:postgresql:10.5://localhost:5432/default"
            , "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"};

    @AliasFor(annotation = ActiveProfiles.class, attribute = "profiles")
    String[] profiles() default {"local"};

}
