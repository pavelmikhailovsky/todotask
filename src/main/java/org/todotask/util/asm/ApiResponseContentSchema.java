package org.todotask.util.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V17;

public class ApiResponseContentSchema {

    public static Class<?> getNewClassWithField(String fieldName) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        String className = "ClassWithField" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        classWriter.visit(
                V17,
                ACC_PUBLIC,
                "testing/" + className,
                null,
                "java/lang/Object",
                null
        );

        FieldVisitor fieldVisitor = classWriter.visitField(
                ACC_PUBLIC,
                fieldName,
                "Ljava/lang/String;",
                null,
                null
        );
        fieldVisitor.visitEnd();

        CustomClassLoad loader = new CustomClassLoad();
        return loader.defineClass("testing." + className, classWriter.toByteArray());
    }
}
