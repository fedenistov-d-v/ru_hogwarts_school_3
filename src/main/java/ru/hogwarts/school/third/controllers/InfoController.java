package ru.hogwarts.school.third.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.third.services.InfoService;

@RestController
@RequestMapping("info")
public class InfoController {

    @Value("${server.port}")
    private int port;

    private final ServerProperties serverProperties;
    private final InfoService infoService;

    public InfoController(ServerProperties serverProperties, InfoService infoService) {
        this.serverProperties = serverProperties;
        this.infoService = infoService;
    }

    @GetMapping("/port")
    public int getPort() {
        return serverProperties.getPort();
    }

    @GetMapping("/sum-of-sequence")
    public long getSumOfSequence() {
        return infoService.getSumOfSequence();
    }
}
