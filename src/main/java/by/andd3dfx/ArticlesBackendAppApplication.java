package by.andd3dfx;

import by.andd3dfx.util.StartupHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ArticlesBackendAppApplication {

    public static void main(String[] args) {
        Environment env = new SpringApplication(ArticlesBackendAppApplication.class)
                .run(args)
                .getEnvironment();
        StartupHelper.logApplicationStartup(env);
    }
}
