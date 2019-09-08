package com.xcxgf.cainiao.controller;

import com.xcxgf.cainiao.POJO.AddDomitory;
import com.xcxgf.cainiao.POJO.Dormitory;
import com.xcxgf.cainiao.services.DormService;
import com.xcxgf.cainiao.services.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("Dormitory")
public class DormitoryController {
    @Autowired
    DormitoryService dormitoryService;

    @Autowired
    DormService dormService;
    @RequestMapping(method = RequestMethod.GET,value="/getDormitoryList")
    public List<Dormitory> getAccountList(){return dormitoryService.getDormitoryList();}

    @RequestMapping(method = RequestMethod.POST,value = "/addNewDormitory")
    public int addNewDormitory(@RequestBody AddDomitory addDomitory){
         String dormitoryName=addDomitory.getDormitoryName();
        String money=addDomitory.getMoney();

        return dormitoryService.addNewDormitory(dormitoryName,money);
    }
    @RequestMapping(method = RequestMethod.POST,value = "/SelectDormitoryId")
    public int Selectid(@RequestBody AddDomitory addDomitory){
        return dormitoryService.Selectid(addDomitory);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/insertDorms")
    public int insertDorms(@RequestBody AddDomitory addDomitory){
        int domitoryId= dormitoryService.Selectid(addDomitory);
        int Fcount=Integer.parseInt(addDomitory.getFloorCount());
        int Dcount=Integer.parseInt(addDomitory.getDormCount());
        for(int i=1;i<=Fcount;i++){
            for(int j=1;j<=Dcount;j++){
                int s=i*100+j;
                String dormNum=s+"";
                dormService.AddDorms(domitoryId,dormNum);
            }
        }
        return 1;
    }
}