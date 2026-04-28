package ru.hogwarts.school.third.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("info")
public class InfoController {

    @Value("${server.port}")
    private int port;

    private final ServerProperties serverProperties;

    public InfoController(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @GetMapping("/port")
    public int getPort() {
        return serverProperties.getPort();
    }
}
