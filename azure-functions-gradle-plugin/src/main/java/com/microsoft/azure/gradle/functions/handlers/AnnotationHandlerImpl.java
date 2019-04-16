/*
 * MIT License
 *
 * Copyright (c) 2017-2019 Elena Lakhno
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.microsoft.azure.gradle.functions.handlers;

import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.StorageAccount;
import com.microsoft.azure.gradle.functions.bindings.BaseBinding;
import com.microsoft.azure.gradle.functions.bindings.BindingFactory;
import com.microsoft.azure.gradle.functions.bindings.HttpBinding;
import com.microsoft.azure.gradle.functions.bindings.StorageBaseBinding;
import com.microsoft.azure.gradle.functions.configuration.FunctionConfiguration;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.ScanResult;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.logging.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class AnnotationHandlerImpl implements AnnotationHandler {
    private Logger logger;

    public AnnotationHandlerImpl(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public Set<Method> findFunctions(final URL url) {
        Set<Method> methods = new LinkedHashSet<>();
        
        try (ScanResult scanResult = new ClassGraph()
            .addClassLoader(getClassLoader(url))
            .enableAnnotationInfo()
            .enableMethodInfo()
            .scan()) {
            String annotationName = FunctionName.class.getName();
            for (ClassInfo classInfo : scanResult.getClassesWithMethodAnnotation(annotationName)) {
                for (MethodInfo methodInfo : classInfo.getMethodInfo()) {
                    if (methodInfo.hasAnnotation(annotationName)) {
                        methods.add(methodInfo.loadClassAndGetMethod());
                    }
                }
            }
        }

        return methods;
    }

    private ClassLoader getClassLoader(final URL url) {
        return new URLClassLoader(new URL[]{url}, this.getClass().getClassLoader());
    }

    @Override
    public Map<String, FunctionConfiguration> generateConfigurations(final Set<Method> methods) throws Exception {
        final Map<String, FunctionConfiguration> configMap = new HashMap<>();
        for (final Method method : methods) {
            final FunctionName functionAnnotation = method.getAnnotation(FunctionName.class);
            final String functionName = functionAnnotation.value();
            validateFunctionName(configMap.keySet(), functionName);
            logger.quiet("Starting processing function : " + functionName);
            configMap.put(functionName, generateConfiguration(method));
        }
        return configMap;
    }

    private void validateFunctionName(final Set<String> nameSet, final String functionName) throws Exception {
        if (StringUtils.isEmpty(functionName)) {
            throw new Exception("Azure Function name cannot be empty.");
        }
        if (nameSet.stream().anyMatch(n -> StringUtils.equalsIgnoreCase(n, functionName))) {
            throw new Exception("Found duplicate Azure Function: " + functionName);
        }
    }

    @Override
    public FunctionConfiguration generateConfiguration(final Method method) {
        final FunctionConfiguration config = new FunctionConfiguration();
        final List<BaseBinding> bindings = config.getBindings();

        processParameterAnnotations(method, bindings);

        processMethodAnnotations(method, bindings);

        patchStorageBinding(method, bindings);

        config.setEntryPoint(method.getDeclaringClass().getCanonicalName() + "." + method.getName());
        return config;
    }

    private void processParameterAnnotations(final Method method, final List<BaseBinding> bindings) {
        for (final Parameter param : method.getParameters()) {
            bindings.addAll(parseAnnotations(param::getAnnotations, this::parseParameterAnnotation));
        }
    }

    private void processMethodAnnotations(final Method method, final List<BaseBinding> bindings) {
        if (!method.getReturnType().equals(Void.TYPE)) {
            bindings.addAll(parseAnnotations(method::getAnnotations, this::parseMethodAnnotation));

            if (bindings.stream().anyMatch(b -> b.getType().equalsIgnoreCase(HttpTrigger.class.getSimpleName())) &&
                    bindings.stream().noneMatch(b -> b.getName().equalsIgnoreCase("$return"))) {
                bindings.add(new HttpBinding());
            }
        }
    }

    private List<BaseBinding> parseAnnotations(Supplier<Annotation[]> annotationProvider,
                                               Function<Annotation, BaseBinding> annotationParser) {
        final List<BaseBinding> bindings = new ArrayList<>();

        for (final Annotation annotation : annotationProvider.get()) {
            final BaseBinding binding = annotationParser.apply(annotation);
            if (binding != null) {
                logger.quiet("Adding binding: " + binding.toString());
                bindings.add(binding);
            }
        }
        return bindings;
    }

    private BaseBinding parseParameterAnnotation(final Annotation annotation) {
        return BindingFactory.getBinding(annotation);
    }

    private BaseBinding parseMethodAnnotation(final Annotation annotation) {
        final BaseBinding ret = parseParameterAnnotation(annotation);
        if (ret != null) {
            ret.setName("$return");
        }
        return ret;
    }

    private void patchStorageBinding(final Method method, final List<BaseBinding> bindings) {
        final Optional<Annotation> storageAccount = Arrays.stream(method.getAnnotations())
                .filter(a -> a instanceof StorageAccount)
                .findFirst();

        if (storageAccount.isPresent()) {
            logger.quiet("StorageAccount annotation found.");
            final String connectionString = ((StorageAccount) storageAccount.get()).value();
            bindings.stream().forEach(b -> {
                if (b instanceof StorageBaseBinding) {
                    final StorageBaseBinding sb = (StorageBaseBinding) b;
                    // Override storage bindings with empty connection
                    if (StringUtils.isEmpty(sb.getConnection())) {
                        sb.setConnection(connectionString);
                    }
                }
            });
        } else {
            logger.quiet("No StorageAccount annotation found.");
        }
    }
}
