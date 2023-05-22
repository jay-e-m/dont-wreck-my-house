package admin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import admin.ui.Controller;

@ComponentScan
@PropertySource("app.properties")
public class App {
    public static void main(String[] args) {
        //TODO Add tests and validations to the reservation classes
        //TODO Implement functionalities to view reservations by queries other than the unique reservation ID (host ID)
        //TODO Implement more specific exception messages for reservation (especially service)

        ApplicationContext container = new AnnotationConfigApplicationContext(App.class);

        Controller controller = container.getBean(Controller.class);

        controller.run();
    }
}
