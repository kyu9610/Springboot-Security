package com.cos.securiy1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // View Return
public class IndexController {

    @GetMapping({"","/"})
    public String index(){
        // 머스테치 기본폴더 src/main/resources/
        // ViewResolver 설정 : templates (prefix) , mustache(suffix) - 생략 가능.
        return "index"; // src/main/resources/templates/index.mustache 를 찾게된다.
    }
}
