// SPDX-FileCopyrightText: 2025 Marcus Alexander Dahl (programkode)
// SPDX-License-Identifier: MPL-2.0
package assignment.testing.framework;

import org.opentest4j.AssertionFailedError;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.lang.ScopedValue.where;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.ScopedValue;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class Utilities
{
    //# Standard I/O
    static private final PrintStream stdout = System.out;
    static private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    static public String getStandardOutput() {
        return Utilities.output.toString().replaceAll("\\p{Cntrl}", "");
    }

    static public void setStandardOutput() {
        Utilities.setStandardOutput(new PrintStream(Utilities.output));
    }

    static public void setStandardOutput(PrintStream stream) {
        System.setOut(stream);
    }

    static public void resetStandardOutput() {
        System.setOut(Utilities.stdout);
    }


    //==================================================================================================================
    //# Classes
    static public Optional<Class<?>> findClass(String pkg, String className) {
        return Utilities.findClass(Utilities.FQCN(pkg, className));
    }

    static public Optional<Class<?>> findClass(String fullyQualifiedClassName) {
        try {
            return Optional.of(Class.forName(fullyQualifiedClassName));
        } catch (ClassNotFoundException _) {
            return Optional.empty();
        }
    }


    static public boolean classExists(String pkg, String className) {
        return Utilities.classExists(Utilities.FQCN(pkg, className));
    }

    static public boolean classExists(String fullyQualifiedClassName) {
        return Utilities.findClass(fullyQualifiedClassName).isPresent();
    }


    //==================================================================================================================
    //# Methods
    // TODO: support FQCN#methodName([types]) syntax as method specifier0
    static public Optional<Method> findMethod(
            String pkg, String className,
            String methodName, Class<?>... parameterTypes
    ) {
        return Utilities.findMethod(Utilities.FQCN(pkg, className), methodName, parameterTypes);
    }

    static public Optional<Method> findMethod(
            String fullyQualifiedClassName,
            String methodName, Class<?>... parameterTypes
    ) {
        var classObject = Utilities.findClass(fullyQualifiedClassName);

        if (classObject.isPresent()) {
            return Utilities.findMethod(classObject.get(), methodName, parameterTypes);
        }

        return Optional.empty();
    }

    static public Optional<Method> findMethod(Class<?> classObject, String methodName, List<Class<?>> parameterTypes) {
        return Utilities.findMethod(classObject, methodName, parameterTypes.toArray(new Class[0]));
    }

    static public Optional<Method> findMethod(Class<?> classObject, String methodName, Class<?>... parameterTypes) {
        try {
            return parameterTypes.length == 0
                ? Optional.of(classObject.getMethod(methodName))
                : Optional.of(classObject.getMethod(methodName, parameterTypes))
            ;
        } catch (NoSuchMethodException _) {
            return Optional.empty();
        }
    }


    static public Optional<Method> findDeclaredMethod(
            String pkg, String className,
            String methodName, Class<?>... parameterTypes
    ) {
        return Utilities.findDeclaredMethod(Utilities.FQCN(pkg, className), methodName, parameterTypes);
    }

    static public Optional<Method> findDeclaredMethod(
            String fullyQualifiedClassName,
            String methodName, Class<?>... parameterTypes
    ) {
        var classObject = Utilities.findClass(fullyQualifiedClassName);

        if (classObject.isPresent()) {
            return Utilities.findDeclaredMethod(classObject.get(), methodName, parameterTypes);
        }

        return Optional.empty();
    }

    static public Optional<Method> findDeclaredMethod(
            Class<?> classObject,
            String methodName, List<Class<?>> parameterTypes
    ) {
        return Utilities.findDeclaredMethod(classObject, methodName, parameterTypes.toArray(new Class[0]));
    }

    static public Optional<Method> findDeclaredMethod(
            Class<?> classObject,
            String methodName, Class<?>... parameterTypes
    ) {
        try {
            return parameterTypes.length == 0
                ? Optional.of(classObject.getDeclaredMethod(methodName))
                : Optional.of(classObject.getDeclaredMethod(methodName, parameterTypes))
            ;
        } catch (NoSuchMethodException _) {
            return Optional.empty();
        }
    }


    static public boolean methodExists(String pkg, String className, String methodName, Class<?>... parameterTypes) {
        return Utilities.methodExists(Utilities.FQCN(pkg, className), methodName, parameterTypes);
    }

    static public boolean methodExists(String fullyQualifiedClassName, String methodName, Class<?>... parameterTypes) {
        return Utilities.findMethod(fullyQualifiedClassName, methodName, parameterTypes).isPresent();
    }

    static public boolean methodExists(Class<?> classObject, String methodName, Class<?>... parameterTypes) {
        return Utilities.findMethod(classObject, methodName, parameterTypes).isPresent();
    }


    static public boolean methodReturns(
            String fullyQualifiedClassName,
            String methodName,
            List<Class<?>> parameterTypes,
            Class<?> returnType
    ) {
        var methodObject = Utilities.findMethod(fullyQualifiedClassName, methodName, parameterTypes.toArray(new Class[0]));

        return methodObject.map(method -> method.getReturnType().equals(returnType)).orElse(false);

    }

    static public boolean methodReturns(Method methodObject, Class<?> returnType) {
        return methodObject.getReturnType().equals(returnType);
    }


    //==================================================================================================================
    //# Fields
    // TODO: support FQCN#fieldName syntax as field specifier
    static public Optional<Field> findField(String pkg, String className, String fieldName) {
        return Utilities.findField(FQCN(pkg, className), fieldName);
    }

    static public Optional<Field> findField(String fullyQualifiedClassName, String fieldName) {
        var classObject = Utilities.findClass(fullyQualifiedClassName);

        if (classObject.isPresent()) {
            return Utilities.findField(classObject.get(), fieldName);
        }

        return Optional.empty();
    }

    static public Optional<Field> findField(Class<?> classObject, String fieldName) {
        try {
            return Optional.of(classObject.getField(fieldName));
        } catch (NoSuchFieldException _) {
            return Optional.empty();
        }
    }


    static public Optional<Field> findDeclaredField(String pkg, String className, String fieldName) {
        return Utilities.findDeclaredField(FQCN(pkg, className), fieldName);
    }

    static public Optional<Field> findDeclaredField(String fullyQualifiedClassName, String fieldName) {
        var classObject = Utilities.findClass(fullyQualifiedClassName);

        if (classObject.isPresent()) {
            return Utilities.findDeclaredField(classObject.get(), fieldName);
        }

        return Optional.empty();
    }

    static public Optional<Field> findDeclaredField(Class<?> classObject, String fieldName) {
        try {
            return Optional.of(classObject.getDeclaredField(fieldName));
        } catch (NoSuchFieldException _) {
            return Optional.empty();
        }
    }


    static public boolean fieldExists(String pkg, String className, String fieldName) {
        return Utilities.fieldExists(FQCN(pkg, className), fieldName);
    }

    static public boolean fieldExists(String fullyQualifiedClassName, String fieldName) {
        return Utilities.findField(fullyQualifiedClassName, fieldName).isPresent();
    }

    public String fieldType(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            return fieldParameterizedType(parameterizedType);
        }
        else {
            return stripPackageFromClassName(type.getTypeName());
        }
    }

    public String fieldParameterizedType(ParameterizedType type) {
        StringBuilder output = new StringBuilder();

        var typeArguments = type.getActualTypeArguments();
        var types = new ArrayList<String>();

        output.append(stripPackageFromClassName(
                ((Class<?>) type.getRawType()).getName()
        ));

        for (var typeArgument : typeArguments) {
            if (typeArgument instanceof Class) {
                types.add(stripPackageFromClassName(((Class<?>) typeArgument).getName()));
            }
            else if (typeArgument instanceof ParameterizedType parameterizedType) {
                types.add(fieldParameterizedType(parameterizedType));
            }
            else {
                types.add(stripPackageFromClassName(typeArgument.toString()));
            }
        }

        output.append("<");
        output.append(String.join(", ", types));
        output.append(">");

        return output.toString();
    }


    //==================================================================================================================
    //# Scoped testing
    // TODO: class instance scoping
    static private final ScopedValue<Class<?>> CLASS = ScopedValue.newInstance();
    static private final ScopedValue<Method> METHOD = ScopedValue.newInstance();
    static private final ScopedValue<Field> FIELD = ScopedValue.newInstance();

    static public Class<?> getScopedClass() {
        return CLASS.get();
    }

    static public Method getScopedMethod() {
        return METHOD.get();
    }

    static public Field getScopedField() {
        return FIELD.get();
    }


    //------------------------------------------------------------------------------------------------------------------
    //## Scoped test*-methods
    //__________________________________________________________________________________________________________________
    //### Scoped class
    static public void testClass(String pkg, String className, Runnable fn) {
        Utilities.testClass(Utilities.FQCN(pkg, className), fn);
    }

    static public void testClass(String fullyQualifiedClassName, Runnable fn) {
        var classObject = Utilities.findClass(fullyQualifiedClassName);

        if (classObject.isEmpty()) {
            throw new AssertionFailedError(
                "Class not found: %s".formatted(fullyQualifiedClassName),
                "Class located at src/main/java/%s".formatted(fullyQualifiedClassName.replaceAll("\\.", "/")),
                "Class not found"
            );
        }

        Utilities.testClass(classObject.get(), fn);
    }

    static public void testClass(Class<?> classObject, Runnable fn) {
        where(Utilities.CLASS, classObject).run(fn);
    }


    static public void testClassMethod(
            String pkg, String className,
            String methodName,
            Runnable fn
    ) {
        Utilities.testClassMethod(pkg, className, methodName, List.of(), fn);
    }

    static public void testClassMethod(
            String pkg, String className,
            String methodName, List<Class<?>> parameterTypes,
            Runnable fn
    ) {
        Utilities.testClassMethod(Utilities.FQCN(pkg, className), methodName, parameterTypes, fn);
    }

    static public void testClassMethod(
            String fullyQualifiedClassName,
            String methodName, List<Class<?>> parameterTypes,
            Runnable fn
    ) {
        var classOptional = Utilities.findClass(fullyQualifiedClassName);

        if (classOptional.isEmpty()) {
            throw new AssertionFailedError(
                "Class not found: %s".formatted(fullyQualifiedClassName),
                "Class located at src/main/java/%s.java".formatted(fullyQualifiedClassName.replaceAll("\\.", "/")),
                "Class not found"
            );
        }

        where(Utilities.CLASS, classOptional.get()).run(() -> {
            var parameters = parameterTypes.toArray(new Class[0]);
            var methodOptional = Utilities.findMethod(CLASS.get(), methodName, parameters);

            if (methodOptional.isEmpty()) {
                throw new AssertionFailedError(
                    "Class-method not found: %s.%s(%s)".formatted(
                        fullyQualifiedClassName, methodName, parameterTypes.isEmpty() ? "" : Arrays.toString(parameters)
                    ),
                    "Method to exist within class",
                    "Method within class does not exist"
                );
            }

            where(Utilities.METHOD, methodOptional.get()).run(fn);
        });
    }

    //__________________________________________________________________________________________________________________
    //### Scoped method
    /** SCOPED CLASS */
    static public void testMethod(String methodName, List<Class<?>> parameterTypes, Runnable fn) {
        Utilities.findMethod(CLASS.get(), methodName, parameterTypes.toArray(new Class[0])).ifPresent(
                method -> Utilities.testMethod(method, fn)
        );
    }

    /** SCOPED METHOD */
    static public void testMethod(Method methodObject, Runnable fn) {
        where(Utilities.METHOD, methodObject).run(fn);
    }

    //__________________________________________________________________________________________________________________
    //### Scoped field
    static public void testField(
            String pkg,
            String className,
            String fieldName,
            Runnable fn
    ) {
        Utilities.testField(FQCN(pkg, className), fieldName, fn);
    }

    static public void testField(
            String fullyQualifiedClassName,
            String fieldName,
            Runnable fn
    ) {
        Utilities.findClass(fullyQualifiedClassName).ifPresent(classObject -> {
            Utilities.testField(classObject, fieldName, fn);
        });
    }

    static public void testField(Class<?> classObject, String fieldName, Runnable fn) {
        Utilities.findField(classObject, fieldName).ifPresent(field -> Utilities.testField(field, fn));
    }

    static public void testField(Field fieldObject, Runnable fn) {
        where(Utilities.FIELD, fieldObject).run(fn);
    }


    //------------------------------------------------------------------------------------------------------------------
    //## Classes
    /** SCOPED CLASS */
    static public boolean classInheritsFrom(Class<?> classObject) {
        var superClass = CLASS.get().getSuperclass();

        do {
            if (superClass.equals(classObject)) {
                return true;
            }

            superClass = superClass.getSuperclass();
        } while (superClass != null);

        return false;
    }

    /** SCOPED CLASS */
    static public Object classCreateInstance(Object... parameterValues) {
        return Utilities.classCreateInstance(CLASS.get(), parameterValues);
    }

    static public Object classCreateInstance(Class<?> classObject, Object... parameterValues) {
        var signature = Arrays.stream(parameterValues).map(Object::getClass).toArray();

        try {
            var constructor = signature.length == 0
                    ? classObject.getConstructor()
                    : classObject.getConstructor((Class<?>[]) signature)
            ;

            return constructor.newInstance(parameterValues);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // TODO: better feedback per exception
            throw new RuntimeException(e);
        }
    }

    static public void classInstanceInvokeMethod(Object instance, String methodName, Object... parameterValues) {
        var signature = Arrays.stream(parameterValues).map(Object::getClass).toArray();

        try {
            var method = parameterValues.length == 0
                    ? CLASS.get().getMethod(methodName)
                    : CLASS.get().getMethod(methodName, (Class<?>[]) signature)
            ;

            method.invoke(instance, parameterValues);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            // TODO: better feedback per exception
            throw new RuntimeException(e);
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    //## Methods
    /** SCOPED CLASS+METHOD */
    static public boolean methodReturns(Class<?> returnType) {
        return Utilities.methodReturns(METHOD.get(), returnType);
    }

    /** SCOPED CLASS */
    static public boolean methodExists(String method, Class<?>... parameterTypes) {
        return findMethod(CLASS.get(), method, parameterTypes).isPresent();
    }


    static public boolean methodIsPublic() {
        return (METHOD.get().getModifiers() & Modifier.PUBLIC) != 0;
    }

    static public boolean methodIsProtected() {
        return (METHOD.get().getModifiers() & Modifier.PROTECTED) != 0;
    }

    static public boolean methodIsPrivate() {
        return (METHOD.get().getModifiers() & Modifier.PRIVATE) != 0;
    }

    static public boolean methodIsAbstract() {
        return (METHOD.get().getModifiers() & Modifier.ABSTRACT) != 0;
    }

    static public boolean methodIsStatic() {
        return (METHOD.get().getModifiers() & Modifier.STATIC) != 0;
    }

    static public boolean methodIsFinal() {
        return (METHOD.get().getModifiers() & Modifier.FINAL) != 0;
    }

    static public boolean methodHasModifiers(AccessFlag... flags) {
        return Utilities.methodHasModifiers(METHOD.get(), flags);
    }

    static public boolean methodHasModifiers(Method methodObject, AccessFlag... flags) {
        var modifiers = methodObject.getModifiers();

        for (var flag : flags) {
            if ((modifiers&flag.mask()) == 0) {
                return false;
            }
        }

        return true;
    }


    //------------------------------------------------------------------------------------------------------------------
    //## Fields
    static public boolean fieldIsPublic() {
        return (FIELD.get().getModifiers() & Modifier.PUBLIC) != 0;
    }

    static public boolean fieldIsProtected() {
        return (FIELD.get().getModifiers() & Modifier.PROTECTED) != 0;
    }

    static public boolean fieldIsPrivate() {
        return (FIELD.get().getModifiers() & Modifier.PRIVATE) != 0;
    }

    static public boolean fieldIsStatic() {
        return (FIELD.get().getModifiers() & Modifier.STATIC) != 0;
    }

    static public boolean fieldIsFinal() {
        return (FIELD.get().getModifiers() & Modifier.FINAL) != 0;
    }

    static public boolean fieldHasModifiers(AccessFlag... flags) {
        return Utilities.fieldHasModifiers(FIELD.get(), flags);
    }

    static public boolean fieldHasModifiers(Field fieldObject, AccessFlag... flags) {
        var modifiers = fieldObject.getModifiers();

        for (var flag : flags) {
            if ((modifiers&flag.mask()) == 0) {
                return false;
            }
        }

        return true;
    }


    //==================================================================================================================
    //# Helpers
    static public String stripPackageFromClassName(String fullyQualifiedClassName) {
        return List.of(fullyQualifiedClassName.split("\\.")).getLast();
    }


    static private String FQCN(String pkg, String className) {
        return String.format("%s.%s", pkg, className);
    }


    //------------------------------------------------------------------------------------------------------------------
    //## Assertions
    static public void assertStandardOutputEquals(String input) {
        assertEquals("\"%s\"".formatted(input), "\"%s\"".formatted(Utilities.getStandardOutput()));
    }


    //==================================================================================================================
    //# Private constructor; prevent instantiation of this class as it strictly contains static helper methods
    private Utilities() {}
}
