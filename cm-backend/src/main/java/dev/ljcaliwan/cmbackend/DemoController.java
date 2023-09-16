package dev.ljcaliwan.cmbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private static int COUNTER = 0;
    @GetMapping("/demos")
    public String demoMessage(){
        return ("Test Continuous Integration is Working. The value of counter is: ").formatted(++COUNTER);
    }
}
