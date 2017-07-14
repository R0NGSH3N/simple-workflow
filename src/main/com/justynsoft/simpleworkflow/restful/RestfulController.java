package com.justynsoft.simpleworkflow.restful;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestfulController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Simple Workflow!";
    }

}
