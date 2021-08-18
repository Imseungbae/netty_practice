package com.netty.controller;

import com.netty.echo.EchoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/test")
public class testController {

    @GetMapping( "/v1")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        log.info("testController=============================={}{}", request);
        EchoClient echoClient = new EchoClient();
        log.info("echoClient call================");
        echoClient.send();

        return "ok";
    }
}
