package com.zetcode;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

import org.junit.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

public class BoardArchitectureTest {

    @Test
    public void board_class_should_reside_in_zetcode_package() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.zetcode");

        ArchRule rule = classes()
                .that().haveSimpleName("Board")
                .should().bePublic();

        rule.check(importedClasses);
    }


}
