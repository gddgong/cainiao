package com.xcxgf.cainiao.mapper;

import com.xcxgf.cainiao.POJO.Active;
import com.xcxgf.cainiao.POJO.SouContract;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author gdd
 */
public interface ActiveMapper {
    /**
     * 获取合同总条数
     * @param name
     * @return
     */
    @Select("select count(*) from activity where companyName like CONCAT('%',#{name},'%')")
    public int getActiveCount(String name);

    /**
     * 获取全部活动信息
     * @param startPage
     * @param pageSize
     * @param name
     * @return
     */
    @Select("select * from activity where companyName like CONCAT('%',#{name},'%') limit #{startPage},#{pageSize}")
    public List<Active> getActiveList(int startPage, int pageSize, String name);
}
