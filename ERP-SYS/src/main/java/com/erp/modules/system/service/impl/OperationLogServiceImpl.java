package com.erp.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.system.entity.OperationLog;
import com.erp.modules.system.mapper.OperationLogMapper;
import com.erp.modules.system.service.IOperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 操作日志服务实现类
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

    private final OperationLogMapper operationLogMapper;

    public OperationLogServiceImpl(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    @Override
    public IPage<OperationLog> pageLog(Long page, Long size, String username, String module) {
        Page<OperationLog> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(username), OperationLog::getUsername, username)
                .like(StringUtils.hasText(module), OperationLog::getModule, module)
                .orderByDesc(OperationLog::getCreateTime);
        return operationLogMapper.selectPage(pageParam, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveLog(OperationLog operationLog) {
        return operationLogMapper.insert(operationLog) > 0;
    }

    @Override
    public OperationLog getLogById(Long id) {
        OperationLog log = operationLogMapper.selectById(id);
        if (log == null) {
            throw new BusinessException("操作日志不存在");
        }
        return log;
    }
}
