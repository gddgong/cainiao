package com.xcxgf.cainiao.services;

import com.xcxgf.cainiao.POJO.SouContract;
import com.xcxgf.cainiao.mapper.SouContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author gdd
 */
@Service
public class SouContractServices {
    @Autowired
    SouContractMapper souContractMapper;

    /**
     * 获取嗖嗖鸟合同条数
     * @param request
     * @return
     */
    public int getContractCount(HttpServletRequest request){
        String name=request.getParameter("name");
        Boolean isDelete=false;
        if(request.getParameter("isDelete").equals("true")){
            isDelete=true;
        }
        return souContractMapper.getContractCount(name,isDelete);
    }

    /**
     * 获取嗖嗖鸟合同信息
     * @param request
     * @return
     */
    public List<SouContract> getContractList(HttpServletRequest request){
        int pageSize=Integer.parseInt(request.getParameter("pageSize"));
        int startPage=(Integer.parseInt(request.getParameter("startPage"))-1)*pageSize;
        String name=request.getParameter("name");
        Boolean isDelete=false;
        if(request.getParameter("isDelete").equals("true")){
            isDelete=true;
        }
        return souContractMapper.getContractList(startPage,pageSize,name,isDelete);
    }

    /**
     * 新增客户逻辑处理
     * @param souContract
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int insertContract(SouContract souContract){
        int States=1;
        String sno=souContract.getSno();
        String companyName=souContract.getCompanyName();
        String cSno=souContractMapper.checkSno(companyName);
        if(cSno!=null && cSno.length() != 0){
            if(!cSno.equals(sno)){
                States=-3;
                return States;
            }
            else {
                if(souContractMapper.checkContract(companyName)>0){
                    States=-2;
                    return States;
                }
            }
        }
        else{
            if(souContractMapper.checkCompanyAndSno(sno,companyName)>0){
                States=-1;
                return States;
            }
        }

        if(States==1){
            Boolean isContract=true;
            souContractMapper.updateContractState(isContract,companyName);
            souContractMapper.updateClientSno(sno,companyName);
            souContractMapper.insertContract(souContract);
        }

        return States;
    }

    /**
     * 更新合同优势备注
     * @param souContract
     * @return
     */
    public int updateAdvantage(SouContract souContract){
        String advantage=souContract.getAdvantage();
        String sno=souContract.getSno();

        return souContractMapper.updateAdvantage(advantage,sno);
    }

    /**
     * 插入续约信息
     * @param souContract
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int insertXuYueContract(SouContract souContract){
        int id=souContract.getId();
        Boolean isDelete=true;
        souContractMapper.updateDeleteState(isDelete,id);
        return souContractMapper.insertContract(souContract);
    }

    /**
     * 获取该学号下全部合同
     * @param sno
     * @return
     */
    public List<SouContract> getContractBySno(String sno){
        return souContractMapper.getContractBySno(sno);
    }

    public int deleteContract(SouContract souContract){
        int id=souContract.getId();
        Boolean isDelete=true;
        Boolean isContract=false;
        String companyName=souContract.getCompanyName();
        souContractMapper.updateContractState(isContract,companyName);
        return souContractMapper.updateDeleteState(isDelete,id);
    }
}
