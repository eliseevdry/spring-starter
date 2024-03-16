package org.example.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("api/v1/messages")
@RequiredArgsConstructor
public class MessagesRestController {

    private final MessageSource messageSource;

    @GetMapping
    public String getMessage(@RequestParam String code,
                             @RequestParam String language) {
        return messageSource.getMessage(code, null, null, new Locale(language));
    }
}
