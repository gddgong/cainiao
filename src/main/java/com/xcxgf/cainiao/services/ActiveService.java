package com.xcxgf.cainiao.services;

import com.xcxgf.cainiao.POJO.Active;
import com.xcxgf.cainiao.POJO.SouContract;
import com.xcxgf.cainiao.mapper.ActiveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ActiveService {
    @Autowired
    ActiveMapper activeMapper;
    /**
     * 获取活动条数
     * @param request
     * @return
     */
    public int getActiveCount(HttpServletRequest request){
        String name=request.getParameter("name");
        return activeMapper.getActiveCount(name);
    }

    /**
     * 获取嗖嗖鸟活动参与信息
     * @param request
     * @return
     */
    public List<Active> getActiveList(HttpServletRequest request){
        int pageSize=Integer.parseInt(request.getParameter("pageSize"));
        int startPage=(Integer.parseInt(request.getParameter("startPage"))-1)*pageSize;
        String name=request.getParameter("name");
        return activeMapper.getActiveList(startPage,pageSize,name);
    }
}
