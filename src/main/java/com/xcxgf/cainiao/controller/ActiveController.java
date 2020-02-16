package com.xcxgf.cainiao.controller;

import com.xcxgf.cainiao.POJO.Active;
import com.xcxgf.cainiao.POJO.SouContract;
import com.xcxgf.cainiao.services.ActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("Active")
public class ActiveController {
    @Autowired
    ActiveService activeService;

    @RequestMapping(method = RequestMethod.GET,value = "/getActiveCount")
    public int getContractCount(HttpServletRequest request){
        return activeService.getActiveCount(request);
    }
    @RequestMapping(method = RequestMethod.GET,value = "/getActiveList")
    public List<Active> getContractList(HttpServletRequest request){
        return activeService.getActiveList(request);
    }
}
