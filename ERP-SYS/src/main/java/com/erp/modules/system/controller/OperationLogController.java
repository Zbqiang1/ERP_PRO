package com.erp.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erp.common.result.Result;
import com.erp.modules.system.entity.OperationLog;
import com.erp.modules.system.service.IOperationLogService;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志控制器（只读）
 */
@RestController
@RequestMapping("/api/logs")
public class OperationLogController {

    private final IOperationLogService operationLogService;

    public OperationLogController(IOperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 分页查询操作日志
     *
     * @param page     当前页码
     * @param size     每页大小
     * @param username 用户名（可选）
     * @param module   操作模块（可选）
     * @return 分页结果
     */
    @GetMapping
    public Result<IPage<OperationLog>> list(@RequestParam(defaultValue = "1") Long page,
                                             @RequestParam(defaultValue = "10") Long size,
                                             @RequestParam(required = false) String username,
                                             @RequestParam(required = false) String module) {
        IPage<OperationLog> logPage = operationLogService.pageLog(page, size, username, module);
        return Result.ok(logPage);
    }

    /**
     * 根据ID获取日志详情
     *
     * @param id 日志ID
     * @return 日志信息
     */
    @GetMapping("/{id}")
    public Result<OperationLog> getById(@PathVariable Long id) {
        OperationLog log = operationLogService.getLogById(id);
        return Result.ok(log);
    }
}
