package com.erp.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.system.entity.OperationLog;

/**
 * 操作日志服务接口
 */
public interface IOperationLogService extends IService<OperationLog> {

    /**
     * 分页查询操作日志
     *
     * @param page     当前页码
     * @param size     每页大小
     * @param username 用户名（可选）
     * @param module   操作模块（可选）
     * @return 分页结果
     */
    IPage<OperationLog> pageLog(Long page, Long size, String username, String module);

    /**
     * 保存操作日志
     *
     * @param operationLog 操作日志
     * @return 是否成功
     */
    boolean saveLog(OperationLog operationLog);

    /**
     * 根据ID获取日志详情
     *
     * @param id 日志ID
     * @return 日志实体
     */
    OperationLog getLogById(Long id);
}
