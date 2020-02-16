package com.xcxgf.cainiao.mapper;

import com.xcxgf.cainiao.POJO.SouContract;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SouContractMapper {
    /**
     * 获取合同总条数
     * @param name
     * @return
     */
    @Select("select count(*) from sousoucontractinfo where companyName like CONCAT('%',#{name},'%') and isDelete=#{isDelete}")
    public int getContractCount(String name,Boolean isDelete);

    /**
     * 检查公司学号是否与其他公司重复
     * @param sno
     * @param companyName
     * @return
     */
    @Select("select count(*) from sousouclientinfo where sno=#{sno} and companyName!=#{companyName}")
    public int checkCompanyAndSno(String sno,String companyName);

    /**
     * 检查公司学号
     * @param companyName
     * @return
     */
    @Select("select Sno from sousouclientinfo where companyName=#{companyName}")
    public String checkSno(String companyName);

    /**
     * 检查该公司是否存在合同
     * @param companyName
     * @return
     */
    @Select("select count(*) from sousoucontractinfo where companyName=#{companyName} and isDelete=false")
    public int checkContract(String companyName);

    /**
     * 更新公司学号
     * @param sno
     * @param companyName
     * @return
     */
    @Update("update sousouclientinfo set sno=#{sno} where companyName=#{companyName}")
    public int updateClientSno(String sno,String companyName);

    /**
     * 获取全部嗖嗖鸟合同信息
     * @param startPage
     * @param pageSize
     * @param name
     * @return
     */
    @Select("select * from sousoucontractinfo where companyName like CONCAT('%',#{name},'%') and isDelete=#{isDelete} order by htEndTime limit #{startPage},#{pageSize}")
    public List<SouContract> getContractList(int startPage, int pageSize, String name,Boolean isDelete);

    /**
     * 根据学号搜索全部合同
     * @param sno
     * @return
     */
    @Select("select * from sousoucontractinfo where sno=#{sno} order by htEndTime")
    public List<SouContract> getContractBySno(String sno);

    /**
     * 新增合同信息
     * @param souContract
     * @return
     */
    @Insert("insert into sousoucontractinfo(zsFuZeRen," +
            "city," +
            "companyName," +
            "sno," +
            "payment," +
            "htStartTime," +
            "htEndTime," +
            "tag," +
            "contactPerson," +
            "zhiWei," +
            "phoneNumber," +
            "category," +
            "pingTai," +
            "jyFangShi," +
            "pingPai," +
            "teamSize," +
            "sales," +
            "advantage," +
            "createTime)" +
            "Values(#{zsFuZeRen}," +
            "#{city}," +
            "#{companyName}," +
            "#{sno}," +
            "#{payment}," +
            "#{htStartTime}," +
            "#{htEndTime}," +
            "#{tag}," +
            "#{contactPerson}," +
            "#{zhiWei}," +
            "#{phoneNumber}," +
            "#{category}," +
            "#{pingTai}," +
            "#{jyFangShi}," +
            "#{pingPai}," +
            "#{teamSize}," +
            "#{sales}," +
            "#{advantage}," +
            "#{createTime})")
    public int insertContract(SouContract souContract);

    /**
     * 更新意向客户状态
     * @param isContract
     * @param companyName
     * @return
     */
    @Update("update sousouclientinfo set isContract=#{isContract} where companyName=#{companyName}")
    public int updateContractState(Boolean isContract,String companyName);

    /**
     * 更新优势备注信息
     * @param advantage
     * @param sno
     * @return
     */
    @Update("update sousoucontractinfo set advantage=#{advantage} where sno=#{sno}")
    public int updateAdvantage(String advantage,String sno);

    @Update("update sousoucontractinfo set isDelete=#{isDelete} where id=#{id}")
    public int updateDeleteState(Boolean isDelete,int id);


}
