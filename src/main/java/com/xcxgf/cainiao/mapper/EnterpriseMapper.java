package com.xcxgf.cainiao.mapper;

import com.xcxgf.cainiao.POJO.Enterprise;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 对数据库中enterpriseInfo表（园区企业信息表）的增删改查操作
 */
public interface EnterpriseMapper {
    /**
     * 查询所有满足查询条件的记录，并返回记录内容
     * @param search 查询条件
     * @param limit 需要返回的记录起始位置和终止位置
     * @return Enterprise类型的集合，所有满足条件的记录集合
     */
    @Select("select * from enterpriseInfo where state != -1 ${search} ${limit}")
    public List<Enterprise> getSearchList(String search, String limit);

    /**
     * 查询所有满足查询条件的记录条数
     * @param search 查询条件
     * @return int类型，记录条数
     */
    @Select("SELECT count(*) FROM enterpriseInfo WHERE state !=-1 ${search}")
    public int getSearchCount(String search);

    /**
     * 查询获得所有可用记录
     * @return Enterprise类型的集合，所有可用记录
     */
    @Select("select * from enterpriseInfo where state !=-1")
    public List<Enterprise> getEnterpriseList();

    /**
     * 更新记录
     * @param enterprise 需要被更新的记录对象
     * @return int类型，更新操作影响的记录条数，0为更新失败，否则更新成功
     */
    @Update("UPDATE enterpriseInfo SET enterpriseName=#{enterpriseName},enterprisePerson=#{enterprisePerson},enterpriseTelphone=#{enterpriseTelphone} WHERE id =#{id}")
    public int updateEnterpriseInfo(Enterprise enterprise);

    /**
     * 删除记录
     * @param enterprise 需要被删除的记录对象
     * @return int类型，删除操作影响的记录条数，0为删除失败，否则删除成功
     */
    @Delete("DELETE FROM enterpriseInfo WHERE id =#{id}")
    public int deleteEnterpriseInfo(Enterprise enterprise);

    /**
     * 插入记录
     * @param enterprise 需要被插入的记录对象
     * @return int类型，插入操作影响的记录条数，0为插入失败，否则插入成功
     */
    @Insert("INSERT INTO enterpriseInfo(enterpriseName,enterprisePerson,enterpriseTelphone) VALUES(#{enterpriseName}, #{enterprisePerson}, #{enterpriseTelphone})")
    public int insertEnterpriseInfo(Enterprise enterprise);

    /**
     * 查询是否存在重复记录（插入记录操作时）
     * @param enterprise 需要被查询的记录对象
     * @return int类型，存在的重复记录条数，0为不存在重复记录，否则存在重复记录
     */
    @Select("select count(*) from enterpriseInfo " +
            "where state != -1 " +
            "and enterpriseName = #{enterpriseName}")
    public int insertSearchSame(Enterprise enterprise);

    /**
     * 查询是否存在重复记录（更新记录操作时）
     * @param enterprise 需要被查询的记录对象
     * @return int类型，存在的重复记录条数，0为不存在重复记录，否则存在重复记录
     */
    @Select("SELECT COUNT(*) " +
            "FROM (SELECT * FROM enterpriseInfo WHERE id NOT in (SELECT id FROM enterpriseInfo WHERE state != -1 AND id = #{id})) AS temp " +
            "WHERE state != -1 " +
            "AND enterpriseName = #{enterpriseName}")
    public int updateSearchSame(Enterprise enterprise);

}
