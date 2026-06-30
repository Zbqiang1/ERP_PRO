package com.erp.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.system.entity.Dictionary;
import com.erp.modules.system.mapper.DictionaryMapper;
import com.erp.modules.system.service.IDictionaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典服务实现类
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {

    private final DictionaryMapper dictionaryMapper;

    public DictionaryServiceImpl(DictionaryMapper dictionaryMapper) {
        this.dictionaryMapper = dictionaryMapper;
    }

    @Override
    public List<Dictionary> getByDictType(String dictType) {
        List<Dictionary> result = dictionaryMapper.selectByDictType(dictType);
        return result != null ? result : new ArrayList<>();
    }

    @Override
    public Dictionary getDictById(Long id) {
        Dictionary dict = dictionaryMapper.selectById(id);
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }
        return dict;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDict(Dictionary dictionary) {
        return dictionaryMapper.insert(dictionary) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDict(Dictionary dictionary) {
        Dictionary existDict = dictionaryMapper.selectById(dictionary.getId());
        if (existDict == null) {
            throw new BusinessException("字典不存在");
        }
        return dictionaryMapper.updateById(dictionary) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictById(Long id) {
        return dictionaryMapper.deleteById(id) > 0;
    }
}
