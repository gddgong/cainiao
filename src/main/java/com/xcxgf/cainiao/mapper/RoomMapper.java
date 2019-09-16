package com.xcxgf.cainiao.mapper;

import com.xcxgf.cainiao.POJO.Building;
import com.xcxgf.cainiao.POJO.Lease;
import com.xcxgf.cainiao.POJO.Room;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 对数据库中roominfo表（办公室管理表）的增删改查操作
 */
public interface RoomMapper {
    /**
     * 查询满足条件的可用记录
     *
     * @param search 查询条件
     * @param limit  需要返回的记录的起始位置和需要返回的记录条数
     * @return Room类型的集合，满足条件的可用记录
     */
    @Select("select * " +
            "FROM roominfo " +
            "where buildingName IN (select buildingName FROM buildinginfo where buildingType = '${dataType}') " +
            "${search} ORDER BY buildingName,roomNumber,insertTime desc ${limit}")
    public List<Room> getSearchList(String search, String limit, String dataType);

    /**
     * 查询满足条件的可用记录的条数
     *
     * @param search 查询条件
     * @return int类型，满足条件的记录条数
     */
    @Select("SELECT count(*) " +
            "FROM roominfo " +
            "where buildingName IN (select buildingName FROM buildinginfo where buildingType = '${dataType}') " +
            "${search}")
    public int getSearchCount(String search, String dataType);

    /**
     * 删除记录
     *
     * @param room 需要删除的记录对象
     * @return int类型，删除操作影响的记录条数，为0时删除失败，否则删除成功
     */
    @Delete("DELETE FROM roominfo " +
            "WHERE buildingName=#{buildingName} " +
            "and roomNumber=#{roomNumber}")
    public int deleteRoomInfo(Room room);

    /**
     * 更新记录,更新内容为，计租面积，建筑面积，租金，唯一标识：楼栋名称+房间号
     *
     * @param room 需要更新的记录对象
     * @return int类型，更新操作影响的记录条数，为0时更新失败，否则更新成功
     */
    @Update("UPDATE roominfo SET rentArea=#{rentArea}," +
            "buildingArea=#{buildingArea}," +
            "updateTime=#{updateTime}," +
            "monthRent=#{monthRent}" +
            "WHERE buildingName=#{buildingName} " +
            "and roomNumber=#{roomNumber}")
    public int updateRoomInfo(Room room);

    /**
     * 插入新记录
     *
     * @param room 需要插入的记录对象
     * @return int类型，插入记录影响的记录条数，为0时插入失败，否则插入成功
     */
    @Insert("INSERT INTO leaseinfo(roomNumber,buildingName,rentArea,buildingArea,monthRent,insertTime) " +
            "VALUES(#{roomNumber}, #{buildingName}, #{rentArea}, #{buildingArea},#{monthRent},#{insertTime})")
    public int insertRoomInfo(Room room);

    /**
     * 更新记录（根据租赁信息更新房间的业主）
     *
     * @param lease 需要更新的记录对象
     * @return int类型，更新操作影响的记录条数，为0时更新失败，否则更新成功
     */
    @Update("UPDATE roominfo SET owner=#{owner} " +
            "WHERE roomNumber=#{roomNumber} " +
            "and buildingName=#{buildingName}")
    public int updateRoomInfoOwner(Lease lease);

    /**
     * 更新记录（更新房间的业主为空闲）
     *
     * @param lease 需要更新的记录对象
     * @return int类型，更新操作影响的记录条数，为0时更新失败，否则更新成功
     */
    @Update("UPDATE roominfo SET owner='空闲' " +
            "WHERE roomNumber=#{roomNumber} " +
            "and buildingName=#{buildingName}")
    public int deleteRoomInfoOwner(Lease lease);

    /**
     * 查询是否存在重复记录（执行插入记录操作时）
     *
     * @param room 需要查询是否存在的记录对象
     * @return int类型，满足查询条件的记录条数，为0时不存在重复记录，否则存在重复记录
     */
    @Select("select count(*) " +
            "from roominfo " +
            "where buildingName = #{buildingName} " +
            "and roomNumber = #{roomNumber}")
    public int insertSearchSame(Room room);

    /**
     * 查询是否存在重复记录（执行更新记录操作时）
     *
     * @param room 需要查询是否存在的记录对象
     * @return int类型，满足查询条件的记录条数，为0时不存在重复记录，否则存在重复记录
     */
    @Select("SELECT COUNT(*) " +
            "FROM (SELECT * FROM roominfo WHERE id NOT in (SELECT id FROM roominfo WHERE roomNumber = #{roomNumber} AND buildingName = #{buildingName} )) AS temp " +
            "WHERE roomNumber = #{roomNumber} AND buildingName = #{buildingName}")
    public int updateSearchSame(Room room);

    /**
     * 查询是否存在该楼栋
     *
     * @param room     需要被查询的记录对象
     * @param dataType 楼栋类型
     * @return 0为不存在，1为存在
     */
    @Select("select count(*) " +
            "from buildinginfo " +
            "where buildingName='${room.getBuildingName()}' " +
            "and buildingType='${dataType}'")
    public int insertSearchBuildingName(Room room, String dataType);

    /**
     * 查询楼栋类型
     *
     * @param room
     * @return
     */
    @Select("select buildingType " +
            "from buildinginfo " +
            "where buildingName=#{buildingName}")
    public String getBuildingType(Room room);

    /**
     * 获取办公楼数据
     *
     * @param dataType
     * @return
     */
    @Select("select * from buildinginfo where buildingType='${dataType}'")
    public List<Building> getBuildingList(String dataType);
}
