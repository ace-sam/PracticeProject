package com.practice.redo.practice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    private static int counter=0;
    record PingPong(String result){}

    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new PingPong("Pong Pong: %s".formatted(++counter));
    }
}
