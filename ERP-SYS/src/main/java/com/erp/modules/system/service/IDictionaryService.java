package com.erp.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.system.entity.Dictionary;

import java.util.List;

/**
 * 字典服务接口
 */
public interface IDictionaryService extends IService<Dictionary> {

    /**
     * 根据字典类型查询字典列表
     *
     * @param dictType 字典类型
     * @return 字典列表
     */
    List<Dictionary> getByDictType(String dictType);

    /**
     * 根据ID获取字典
     *
     * @param id 字典ID
     * @return 字典实体
     */
    Dictionary getDictById(Long id);

    /**
     * 新增字典
     *
     * @param dictionary 字典信息
     * @return 是否成功
     */
    boolean addDict(Dictionary dictionary);

    /**
     * 修改字典
     *
     * @param dictionary 字典信息
     * @return 是否成功
     */
    boolean updateDict(Dictionary dictionary);

    /**
     * 删除字典
     *
     * @param id 字典ID
     * @return 是否成功
     */
    boolean deleteDictById(Long id);
}
