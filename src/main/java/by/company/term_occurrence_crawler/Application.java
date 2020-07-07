package by.company.term_occurrence_crawler;

import by.company.term_occurrence_crawler.console.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static by.company.term_occurrence_crawler.console.BannerConfig.STARTING_BANNER;

@SpringBootApplication
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final Menu menu;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println(STARTING_BANNER);
        menu.openMainMenu();
    }

}
