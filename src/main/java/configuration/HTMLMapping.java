package configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HTMLMapping {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
