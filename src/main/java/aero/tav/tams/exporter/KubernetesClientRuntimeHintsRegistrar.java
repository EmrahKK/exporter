package aero.tav.tams.exporter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.fabric8.kubernetes.api.builder.BaseFluent;
import io.fabric8.kubernetes.api.model.AnyType;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.client.Client;
import io.fabric8.kubernetes.client.VersionInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mehmetkorkut
 * created 8.11.2023 08:52
 * package - aero.tav.tams.exporter
 * project - tams-exporter
 */
@Slf4j
public class KubernetesClientRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

    private static final String IO_FABRIC_8 = "io.fabric8";

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.resources().registerPattern("META-INF/services/io.fabric8.kubernetes.api.model.KubernetesResource");
        hints.resources().registerPattern("META-INF/services/io.fabric8.kubernetes.client.http.HttpClient$Factory");
        hints.resources().registerPattern("META-INF/services/io.fabric8.kubernetes.client.extension.ExtensionAdapter");
        hints.reflection().registerType(VersionInfo.class, MemberCategory.values());

        registerClients(hints, Client.class);
        registerJacksonModels(hints, KubernetesResource.class);
        registerJacksonModels(hints, AnyType.class);
        registerJacksonModels(hints, BaseFluent.class);
    }

    private void registerClients(RuntimeHints hints, Class<?> clazz) {
        Reflections reflections = getReflections(clazz);
        Set<Class<?>> clients = getSubTypesOfReflection(clazz, reflections);
        clients.add(clazz);
        register(hints, clients, "registerClients()", clazz);
    }

    private void registerJacksonModels(RuntimeHints hints, Class<?> clazz) {
        Reflections reflections = getReflections(clazz);
        Set<Class<?>> combined = new HashSet<>();
        combined.addAll(getSubTypesOfReflection(clazz, reflections));
        combined.addAll(resolveSerializationClasses(JsonSerialize.class, reflections));
        combined.addAll(resolveSerializationClasses(JsonDeserialize.class, reflections));
        register(hints, combined, "registerJacksonKubernetesModels()", clazz);
    }

    private Reflections getReflections(Class<?> clazz) {
        //search for all reflections within the package of io.fabric8 recursively
        Reflections reflections = new Reflections(IO_FABRIC_8, clazz);
        log.info("Found reflections {}, for class {}, and package: {}", reflections, clazz, IO_FABRIC_8);
        return reflections;
    }

    private HashSet<Class<?>> getSubTypesOfReflection(Class<?> clazz, Reflections reflections) {
        HashSet<Class<?>> classes = new HashSet<>(reflections.getSubTypesOf(clazz));
        log.info("Found # of sub-classes {}, for class {}, for reflection: {}, and package: {}", classes.size(), clazz, reflections, IO_FABRIC_8);
        return classes;
    }

    private void register(RuntimeHints hints, Set<Class<?>> clients, String caller, Class<?> aClass) {
        for (Class<?> client : clients) {
            hints.reflection().registerType(client, MemberCategory.values());
            log.info("{}, for class: {}, for packageName: {}, registering: {} for reflection", caller, aClass, IO_FABRIC_8, client.getName());
        }
    }

    @SneakyThrows
    private <R extends Annotation> Set<Class<?>> resolveSerializationClasses(Class<R> annotationClazz, Reflections reflections) {
        Method method = annotationClazz.getMethod("using");
        return reflections
                .getTypesAnnotatedWith(annotationClazz)
                .stream()
                .map(clazzWithAnnotation -> {
                    R annotation = clazzWithAnnotation.getAnnotation(annotationClazz);
                    try {
                        if (annotation != null) {
                            return (Class<?>) method.invoke(annotation);
                        }
                    } catch (Exception e) {
                        ReflectionUtils.rethrowRuntimeException(e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
