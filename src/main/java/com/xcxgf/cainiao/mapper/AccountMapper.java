package com.xcxgf.cainiao.mapper;

import com.xcxgf.cainiao.POJO.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author gdd
 */
public interface AccountMapper {
    /**
     * 查询数据库合同信息表的1到10条数据
     * @return
     */
   @Select("select * from dormitoryfirstinfo order by endRentTime,roomNumber,id limit 0,10")
   public List<Account> getAccountList0();

    /**
     * 获取合同总信息表的数据按照合同结束日期进行排序 进行分页
     * @param start
     * @param pagesize
     * @return
     */
    @Select("select * from dormitoryfirstinfo where isDelete=#{isDelete} order by endRentTime,roomNumber,insertTime,id limit #{start},#{pagesize}")
    public List<Account> getAccountList(Boolean isDelete,int start,int pagesize);


    /**
     * 根据业主名搜索合同信息并进行分页
     * @param name
     * @param spg
     * @param spgsize
     * @return
     */
    @Select("select * from dormitoryfirstinfo where isDelete=#{isDelete} and owner like CONCAT('%',#{name},'%') " +
            "order by endRentTime,roomNumber,insertTime,id " +
            "limit #{spg},#{spgsize}")
    public List<Account> getAccountNameList(Boolean isDelete,String name,int spg,int spgsize);

    /**
     * 获取搜索的信息的总条数
     * @param name
     * @return
     */

    @Select("select count(*) from dormitoryfirstinfo  where isDelete=#{isDelete} and owner like CONCAT('%',#{name},'%')")
    public int getAccountNameCount(String name,Boolean isDelete);

    /**
     * 获取所选合同的业主信息
     * @param enterpriseName
     * @return
     */
    @Select("select * from enterpriseinfo where enterpriseName=#{enterpriseName}")
    public List<Enterprise> getOwnerList(String enterpriseName);

    /**
     * 获取合同信息总条数
     * @return
     */

    @Select("select count(*) from dormitoryfirstinfo where isDelete=false")
    public int getAccountCount();

    /**
     * 获取到期信息的总条数
     * @return
     */
    @Select("select count(*) from dormitoryfirstinfo  where isDelete=true")
    public int getDeleteCount();

    /**
     * 更新合同信息
     * @param entityAccount
     * @return
     */
    @Update("update dormitoryfirstinfo set " +
            "owner=#{owner}," +
            "buildingName=#{buildingName}," +
            "insertTime=#{insertTime}," +
            "startRentTime=#{startRentTime}," +
            "endRentTime=#{endRentTime}," +
            "totalPeriod=#{totalPeriod}," +
            "totalCost=#{totalCost} " +
            "where id=#{id}")
    public int updateAccount(Account entityAccount);

    /**
     * 新增合同信息
     * @param entityAccount
     * @return
     */
    @Insert("insert into dormitoryfirstinfo(contractId," +
            "owner," +
            "roomNumber," +
            "buildingName," +
            "startRentTime," +
            "endRentTime," +
            "rentPeriod," +
            "totalCost," +
            "totalPeriod," +
            "insertTime) " +
            "values(#{contractId}," +
            "#{owner}," +
            "#{roomNumber}," +
            "#{buildingName}," +
            "#{startRentTime}," +
            "#{endRentTime}," +
            "#{rentPeriod}," +
            "#{totalCost}," +
            "#{rentPeriod}," +
            "#{insertTime})")
    public int insertAccount(Account entityAccount);

    /**
     * 新增合同
     * @param entityReneweal
     * @return
     */
    @Insert("insert into dormitorycontinueinfo (owner," +
            "contractId," +
            "contractType," +
            "continueStartTime," +
            "continueEndTime," +
            "continuePeriod," +
            "totalCost," +
            "insertTime) " +
            "values(#{owner}," +
            "#{contractId}," +
            "#{contractType}," +
            "#{continueStartTime}," +
            "#{continueEndTime}," +
            "#{continuePeriod}," +
            "#{totalCost}," +
            "#{insertTime})")
    public int insertRenewals(Renewal entityReneweal);

    /**
     * 删除合同
     * @param contractId
     * @return
     */
    @Delete("update dormitoryfirstinfo set isDelete=true where contractId=#{contractId}")
    public int deleteAccount(String contractId);

    /**
     * 删除相关联的合同详细信息
     * @param contractId
     * @return
     */
    @Delete("delete from dormitorycontinueinfo where contractId=#{contractId}")
    public int deleteAccount2(String contractId);

    /**
     * 更新合同总租期
     * @param addNum
     * @param totalCost
     * @param endRentTime
     * @param contractId
     * @return
     */
    @Update("update dormitoryfirstinfo set totalPeriod=totalPeriod+#{addNum}," +
            "totalCost=totalCost+#{totalCost}," +
            "endRentTime=#{endRentTime} where contractId=#{contractId}")
    public int updateleasePeriod(int addNum,float totalCost,String endRentTime,String contractId);

    /**
     * 合同变更
     * @param owner
     * @param contractId
     * @return
     */
 @Update("update dormitoryfirstinfo set owner=#{owner}  where contractId=#{contractId}")
 public int updateOwner(String owner,String contractId);

    /**
     * 获取宿舍楼信息
     * @return
     */
    @Select("select * from buildinginfo where buildingType='宿舍'")
    public List<Building> getBuildingList();

    /**
     * 根据楼号和房间类型搜索房间
     * @param roomType
     * @param buildingName
     * @param start
     * @return
     */
    @Select("select * from roominfo " +
            "where owner='空闲' and roomType=#{roomType} and buildingName=#{buildingName} limit #{start},5")
    public List<Room> getRoomList(String roomType,String buildingName, int start);

    /**
     * 根据楼号和房间类型获取房间总条数
     * @param roomType
     * @param buildingName
     * @return
     */
    @Select("select count(*) from roominfo where owner='空闲' and roomType=#{roomType} and buildingName=#{buildingName}")
    public int getRoomListCount(String roomType,String buildingName);

    /**
     * 更新房间业主信息
     * @param room
     * @return
     */
    @Update("update roominfo set owner=#{owner} where buildingName=#{buildingName} and roomNumber=#{roomNumber}")
    public int updateRoom(Room room);

    /**
     * 删除合同后更新房间业主
     * @param room
     * @return
     */
    @Update("update roominfo set owner='空闲' " +
            "where buildingName=#{buildingName} and owner=#{owner} and roomNumber=#{roomNumber}")
    public int updateRoom2(Room room);

    /**
     * 获取合同的租赁房间
     * @param owner
     * @param buildingName
     * @param start
     * @return
     */
    @Select("select * from roominfo where owner=#{owner} and buildingName=#{buildingName} limit #{start},5")
    public List<Room> getRoomList2(String owner,String buildingName, int start);

    /**
     * 获取合同租赁房间总条数
     * @param owner
     * @param buildingName
     * @return
     */
    @Select("select count(*) from roominfo where owner=#{owner} and buildingName=#{buildingName}")
    public int getRoomListCount2(String owner,String buildingName);

    /**
     * 查询宿舍是否存在
     * @param buildingName
     * @return
     */
    @Select("select count(*) from buildinginfo where buildingType='宿舍' and buildingName=#{buildingName}")
    public int getBuildingListCount(String buildingName);

    /**
     * 查询房间是否已存在
     * @param roomNumber
     * @param buildingName
     * @return
     */
    @Select("select count(*) from roominfo where roomNumber=#{roomNumber} and buildingName=#{buildingName}")
    public int getRoomListCount3(String roomNumber,String buildingName);

    /**
     * 查询公司是否存在
     * @param enterpriseName
     * @return
     */
    @Select("select count(*) from enterpriseinfo where enterpriseName=#{enterpriseName}")
    public int getCompanyName(String enterpriseName);

    /**
     * 获取宿舍房间的房间类型
     * @param account
     * @return
     */
    @Select("select roomType from roominfo where buildingName=#{buildingName} and roomNumber=#{roomNumber}")
    public String getRoomType(Account account);

    /**
     * 根据公司名查询全部符合条件的公司名称
     * @param enterpriseName
     * @return
     */
    @Select("select * from enterpriseinfo where enterpriseName like CONCAT('%',#{enterpriseName},'%')")
    public List<Enterprise> getAllCompany(String enterpriseName);

//    @Select("select * from enterpriseinfo")
//    public List<Enterprise> getAllCompany();
}


