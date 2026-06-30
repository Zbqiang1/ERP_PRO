package com.erp.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.system.entity.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 字典Mapper接口
 */
@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    /**
     * 根据字典类型查询字典列表
     *
     * @param dictType 字典类型
     * @return 字典列表
     */
    @Select("SELECT * FROM t_dictionary WHERE dict_type = #{dictType} AND status = 1 ORDER BY sort_order ASC")
    List<Dictionary> selectByDictType(@Param("dictType") String dictType);
}
