import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        Object[] sources = new Object[4];
        
        sources[0] = Directory_Hider.class;
        sources[1] = apps.Admin_Change_User_Status.class;
		sources[2] = apps.Admin_Change_Users.class;
		sources[3] = apps.Admin_Chat_Interface.class;
		
        SpringApplication.run(sources, args);
    }
}
