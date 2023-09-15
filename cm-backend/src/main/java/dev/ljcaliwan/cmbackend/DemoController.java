package dev.ljcaliwan.cmbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private static int COUNTER = 0;
    record DemoMessage(String message){}

    @GetMapping("/demo")
    public DemoMessage demoMessage(){
        return new DemoMessage(("" +
                "Test Continuous Integration is Working. The value of counter is: ").formatted(++COUNTER)
        );
    }
}
