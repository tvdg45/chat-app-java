import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/")
public class Example {

	@RequestMapping(method = RequestMethod.GET)
    String home(
		@RequestParam(value = "person", defaultValue = "") String personName;
		@RequestParam(value = "age", defaultValue = "") String age) {
			   ) {
        return "<h1>Hello " + personName + "! You are " + age + ".</h1>" ;
    }
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }

}
