package com.tosin.enl.springcloud.es.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
@Api(value="测试 Api")
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/hello", method = {RequestMethod.GET, RequestMethod.POST})
    public String hello(){
        System.out.println("hello!");
        return "hello!";
    }

    @ApiOperation(value = "测试信息 ApiOperation value",notes = "测试信息 ApiOperation notes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "info",value = "信息 ApiImplicitParam value",required = true,dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/hello-info", method = {RequestMethod.GET, RequestMethod.POST})
    public String helloInfo(@RequestParam(name="info") String info){
        System.out.println("hello "+info);
        return "hello "+info;
    }
}
