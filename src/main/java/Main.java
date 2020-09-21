import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        Object[] sources = new Object[15];
        
        sources[0] = Directory_Hider.class;
        sources[1] = apps.Admin_Change_User_Status.class;
		sources[2] = apps.Admin_Change_Users.class;
		sources[3] = apps.Admin_Chat_Interface.class;
		sources[4] = apps.Admin_Create_Message.class;
		sources[5] = apps.Admin_Delete_My_Messages.class;
		sources[6] = apps.Admin_Extract_Messages.class;
		sources[7] = apps.Admin_Search_Messages.class;
		sources[8] = apps.Change_User_Status.class;
		sources[9] = apps.Chat_Interface.class;
		sources[10] = apps.Check_User_Status.class;
		sources[11] = apps.Create_Message.class;
		sources[12] = apps.Log_In.class;
		sources[13] = apps.Log_Out.class;
		sources[14] = apps.Search_Messages.class;
		
        SpringApplication.run(sources, args);
    }
}
