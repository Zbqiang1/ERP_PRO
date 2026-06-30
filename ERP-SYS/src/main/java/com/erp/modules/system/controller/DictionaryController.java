package com.erp.modules.system.controller;

import com.erp.common.result.Result;
import com.erp.modules.system.entity.Dictionary;
import com.erp.modules.system.service.IDictionaryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理控制器
 */
@RestController
@RequestMapping("/api/dictionaries")
public class DictionaryController {

    private final IDictionaryService dictionaryService;

    public DictionaryController(IDictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    /**
     * 根据字典类型查询字典列表
     *
     * @param dictType 字典类型
     * @return 字典列表
     */
    @GetMapping
    public Result<List<Dictionary>> list(@RequestParam(required = false) String dictType) {
        if (dictType != null && !dictType.isEmpty()) {
            List<Dictionary> dicts = dictionaryService.getByDictType(dictType);
            return Result.ok(dicts);
        }
        List<Dictionary> dicts = dictionaryService.list();
        return Result.ok(dicts);
    }

    /**
     * 根据ID获取字典详情
     *
     * @param id 字典ID
     * @return 字典信息
     */
    @GetMapping("/{id}")
    public Result<Dictionary> getById(@PathVariable Long id) {
        Dictionary dict = dictionaryService.getDictById(id);
        return Result.ok(dict);
    }

    /**
     * 新增字典
     *
     * @param dictionary 字典信息
     * @return 操作结果
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody Dictionary dictionary) {
        boolean result = dictionaryService.addDict(dictionary);
        return Result.ok(result);
    }

    /**
     * 修改字典
     *
     * @param dictionary 字典信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody Dictionary dictionary) {
        dictionary.setId(id);
        boolean result = dictionaryService.updateDict(dictionary);
        return Result.ok(result);
    }

    /**
     * 删除字典
     *
     * @param id 字典ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = dictionaryService.deleteDictById(id);
        return Result.ok(result);
    }
}
