package aero.tav.tams.exporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@Import(ReflectionConfiguration.class)
@ImportRuntimeHints({
        KubernetesClientRuntimeHintsRegistrar.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
