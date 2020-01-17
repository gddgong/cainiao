package com.xcxgf.cainiao.services;


import com.xcxgf.cainiao.POJO.*;
import com.xcxgf.cainiao.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
@Service
public class AccountService {
    @Autowired
    AccountMapper accountMapper;

    /**
     * /业主信息查询
     * @param request
     * @return
     */
    public List<Enterprise> getOwnerList(HttpServletRequest request){
        String enterpriseName=request.getParameter("owner");
        return accountMapper.getOwnerList(enterpriseName);
    }

    /**
     * 查询第一页
     * @return
     */
    public List<Account> getAccountList0(){ return accountMapper.getAccountList0();}

    /**
     * 获取合同信息
     * @param request
     * @return
     * @throws ParseException
     */
    public List<Account> getAccountList(HttpServletRequest request) throws ParseException {
        funhelper funh=new funhelper();


        int pagesize=Integer.parseInt(request.getParameter("pagecount"));
        int start=(Integer.parseInt(request.getParameter("startnum"))-1)*pagesize;
        Boolean isDelete=false;
        if(request.getParameter("isDelete").equals("true")){
            isDelete=true;
        }
        List<Account> accounts=accountMapper.getAccountList(isDelete,start,pagesize);
        for(int i=0;i<accounts.toArray().length;i++){
            String startdate=accounts.get(i).getStartRentTime();
            int totalLeasePeriod=accounts.get(i).getTotalPeriod();
            accounts.get(i).setEndRentTime(funh.addMounth(startdate,totalLeasePeriod));
        }
        return accounts;
    }

    /**
     * 获取搜索的信息
     * @param request
     * @return
     */
    public List<Account> getAccountNameList(HttpServletRequest request){
        String name=request.getParameter("name");
        int spg1=Integer.parseInt(request.getParameter("spg"));
        Boolean isDelete=false;
        if(request.getParameter("isDelete").equals("true")){
            isDelete=true;
        }
        int spgsize=Integer.parseInt(request.getParameter("spgsize"));
        int spg=(spg1-1)*spgsize;
        return accountMapper.getAccountNameList(isDelete,name,spg,spgsize);

    }

    /**
     * 获取搜索总条数
     * @param request
     * @return
     */
    public int getAccountNameCount(HttpServletRequest request){
        String name=request.getParameter("name");
        Boolean isDelete=false;
        if(request.getParameter("isDelete").equals("true")){
            isDelete=true;
        }
        return accountMapper.getAccountNameCount(name,isDelete);
    }


    /**
     * 获取过期合同总条数
     * @return
     */
    public int getDeleteCount(){return accountMapper.getDeleteCount();}

    /**
     * 获取总条数
     * @return
     */
    public int getAccountCount(){return accountMapper.getAccountCount();}

    /**
     * 数据更新
     * @param account
     * @return
     */

    public int updateAccount(Account account) {
        return accountMapper.updateAccount(account);
    }

    /**
     * 新增数据
     * @param account
     * @return
     * @throws ParseException
     */
    public int insertAccount(Account account) throws ParseException {
        funhelper funh=new funhelper();
        account.setEndRentTime(funh.addMounth(account.getStartRentTime(),account.getRentPeriod()));
        return accountMapper.insertAccount(account);
    }

    /**
     * 新增详细信息
     * @param renewal
     * @return
     * @throws ParseException
     */
    public int insertRenewals(Renewal renewal) throws ParseException {

        funhelper funh=new funhelper();
        renewal.setContractType("首租");
        renewal.setContinueEndTime(funh.addMounth(renewal.getContinueStartTime(),
                renewal.getContinuePeriod()));
        return accountMapper.insertRenewals(renewal);
    }

    /**
     * 合同删除
     * @param account
     * @return
     */
    public int deleteAccount(Account account){
        Room room=new Room();
        String contractId=account.getContractId();
        String buildingName=account.getBuildingName();
        String owner=account.getOwner();
        room.setRoomNumber(account.getRoomNumber());
        room.setOwner(owner);
        room.setBuildingName(buildingName);
        accountMapper.updateRoom2(room);
        return accountMapper.deleteAccount(contractId);
    }

    /**
     * 上传数据
     * @param excelDataList
     * @return
     * @throws ParseException
     */
    public int uploadAccount(List<ExcelData> excelDataList) throws ParseException {
        funhelper fhp=new funhelper();
        int status=1;

        for(int i=0;i<excelDataList.toArray().length;i++){
            Account entityAccount=new Account();
            String [] strarr=excelDataList.get(i).getRoomNumber().split(",");
            String buildingName=excelDataList.get(i).getBuildingName();
            int c=accountMapper.getBuildingListCount(buildingName);
            if(c==0){
                return -4;
            }
            for(int j=0;j<strarr.length;j++){

                int count=accountMapper.getRoomListCount3(strarr[j],buildingName);
                if(count==0){
                    status=-1;
                    return status;
                }
                if(count>1){
                    status=-3;
                    return status;
                }

            }
            int lsp=Integer.parseInt(excelDataList.get(i).getLeasePeriod());
            entityAccount.setOwner(excelDataList.get(i).getCompanyName());

            if(excelDataList.get(i).getStartDate().split("/").length>1){
                entityAccount.setStartRentTime(dateFormat(excelDataList.get(i).getStartDate()));
                entityAccount.setEndRentTime(fhp.addMounth(dateFormat(excelDataList.get(i).getStartDate()),lsp));
            }
            else{
                entityAccount.setStartRentTime(excelDataList.get(i).getStartDate());
                entityAccount.setEndRentTime(fhp.addMounth(excelDataList.get(i).getStartDate(),lsp));
            }
            entityAccount.setRentPeriod(Integer.parseInt(excelDataList.get(i).getLeasePeriod()));

            entityAccount.setBuildingName(buildingName);

            entityAccount.setTotalCost(Float.parseFloat(excelDataList.get(i).getRemark()));
            entityAccount.setInsertTime(excelDataList.get(i).getInsertTime());
            if(status==1){
                accountMapper.insertAccount(entityAccount);
                for(int n=0;n<strarr.length;n++){
                    Room room=new Room();
                    room.setOwner(excelDataList.get(i).getCompanyName());
                    room.setRoomNumber(strarr[n]);
                    room.setBuildingName(buildingName);
                    accountMapper.updateRoom(room);
                }
            }

        }
        return status;
    }

    /**
     * 获取楼栋信息
     * @return
     */
    public List<Building> getBuildingList(){
        return accountMapper.getBuildingList();
    };

    /**
     * 获取房间信息
     * @param request
     * @return
     */
    public List<Room> getRoomList(HttpServletRequest request){
        String roomType=request.getParameter("roomtype");
        String buildingName=request.getParameter("buildingName");
        int page=(Integer.parseInt(request.getParameter("pagedom"))-1)*5;
        return accountMapper.getRoomList(roomType,buildingName,page);
    };

    /**
     * 获取房间总条数
     * @param request
     * @return
     */
    public int getRoomListCount(HttpServletRequest request){
        String roomType=request.getParameter("roomtype");
        String buildingName=request.getParameter("buildingName");
        return accountMapper.getRoomListCount(roomType,buildingName);
    };

    /**
     * 更新房间信息
     * @param room
     * @return
     */
    public int updateRoom(Room room){
        return accountMapper.updateRoom(room);
    }

    /**
     * 获取合同的租赁房间
     * @param request
     * @return
     */
    public List<Room> getRoomList2(HttpServletRequest request){
        String owner=request.getParameter("owner");
        String buildingName=request.getParameter("buildingName");
        int start=(Integer.parseInt(request.getParameter("startpage"))-1)*5;
        return accountMapper.getRoomList2(owner,buildingName,start);
    };

    /**
     * 获取该合同中的具体租赁房间条数
     * @param request
     * @return
     */
    public int getRoomListCount2(HttpServletRequest request){
        String owner=request.getParameter("owner");
        String buildingName=request.getParameter("buildingName");
        return accountMapper.getRoomListCount2(owner,buildingName);
    };

    /**
     * 变更日期格式
     * @param date
     * @return
     * @throws ParseException
     */
    public String dateFormat(String date) throws ParseException {
        SimpleDateFormat sdf1=new SimpleDateFormat("MM/dd/yy");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String outDate=sdf.format(sdf1.parse(date));

        return outDate;
    }

    /**
     * 查询公司是否存在
     * @param request
     * @return
     */
    public int getCompanyName(HttpServletRequest request){
        String enterpriseName=request.getParameter("companyName");
        return accountMapper.getCompanyName(enterpriseName);
    }

    /**
     * 获取房间类型
     * @param account
     * @return
     */
    public String getRoomType(Account account){
        return accountMapper.getRoomType(account);
    }

    /**
     * 根据公司名查询全部符合条件的公司名称
     * @param request
     * @return
     */
    public List<Enterprise> getAllCompany(HttpServletRequest request){
        String enterpriseName =request.getParameter("companyName");
        return accountMapper.getAllCompany(enterpriseName);
    }

//    public List<Enterprise> getAllCompany(){
//        return accountMapper.getAllCompany();
//    }
}
