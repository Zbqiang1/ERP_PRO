package com.erp.modules.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.purchase.dto.PurchaseRequestDTO;
import com.erp.modules.purchase.dto.PurchaseRequestDetailDTO;
import com.erp.modules.purchase.dto.PurchaseRequestQueryDTO;
import com.erp.modules.purchase.entity.PurchaseRequest;
import com.erp.modules.purchase.entity.PurchaseRequestDetail;
import com.erp.modules.purchase.mapper.PurchaseRequestDetailMapper;
import com.erp.modules.purchase.mapper.PurchaseRequestMapper;
import com.erp.modules.purchase.service.IPurchaseRequestService;
import com.erp.modules.purchase.vo.PurchaseRequestDetailVO;
import com.erp.modules.purchase.vo.PurchaseRequestVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购申请 服务实现类
 */
@Service
public class PurchaseRequestServiceImpl extends ServiceImpl<PurchaseRequestMapper, PurchaseRequest> implements IPurchaseRequestService {

    private final PurchaseRequestDetailMapper detailMapper;

    public PurchaseRequestServiceImpl(PurchaseRequestDetailMapper detailMapper) {
        this.detailMapper = detailMapper;
    }

    @Override
    public Page<PurchaseRequestVO> queryPage(PurchaseRequestQueryDTO queryDTO) {
        Page<PurchaseRequest> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<PurchaseRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getPrNo()), PurchaseRequest::getPrNo, queryDTO.getPrNo());
        wrapper.eq(queryDTO.getStatus() != null, PurchaseRequest::getStatus, queryDTO.getStatus());
        wrapper.orderByDesc(PurchaseRequest::getCreateTime);
        Page<PurchaseRequest> resultPage = this.page(page, wrapper);
        Page<PurchaseRequestVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public PurchaseRequestVO getDetailById(Long id) {
        PurchaseRequest pr = this.getById(id);
        if (pr == null) {
            throw new BusinessException("采购申请不存在");
        }
        PurchaseRequestVO vo = toVO(pr);
        LambdaQueryWrapper<PurchaseRequestDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(PurchaseRequestDetail::getPrId, id);
        List<PurchaseRequestDetail> details = detailMapper.selectList(detailWrapper);
        vo.setDetails(details.stream().map(this::detailToVO).collect(Collectors.toList()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(PurchaseRequestDTO dto) {
        PurchaseRequest pr = new PurchaseRequest();
        BeanUtils.copyProperties(dto, pr);
        this.save(pr);
        if (!CollectionUtils.isEmpty(dto.getDetails())) {
            for (PurchaseRequestDetailDTO detailDTO : dto.getDetails()) {
                PurchaseRequestDetail detail = new PurchaseRequestDetail();
                BeanUtils.copyProperties(detailDTO, detail);
                detail.setPrId(pr.getId());
                if (detail.getQuantity() != null && detail.getUnitPrice() != null) {
                    detail.setAmount(detail.getQuantity().multiply(detail.getUnitPrice()));
                }
                detailMapper.insert(detail);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PurchaseRequestDTO dto) {
        PurchaseRequest pr = new PurchaseRequest();
        BeanUtils.copyProperties(dto, pr);
        this.updateById(pr);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        LambdaQueryWrapper<PurchaseRequestDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(PurchaseRequestDetail::getPrId, id);
        detailMapper.delete(detailWrapper);
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long id) {
        PurchaseRequest pr = this.getById(id);
        if (pr == null) {
            throw new BusinessException("采购申请不存在");
        }
        pr.setStatus(1);
        this.updateById(pr);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long id) {
        PurchaseRequest pr = this.getById(id);
        if (pr == null) {
            throw new BusinessException("采购申请不存在");
        }
        pr.setStatus(3);
        this.updateById(pr);
    }

    private PurchaseRequestVO toVO(PurchaseRequest pr) {
        PurchaseRequestVO vo = new PurchaseRequestVO();
        BeanUtils.copyProperties(pr, vo);
        return vo;
    }

    private PurchaseRequestDetailVO detailToVO(PurchaseRequestDetail detail) {
        PurchaseRequestDetailVO vo = new PurchaseRequestDetailVO();
        BeanUtils.copyProperties(detail, vo);
        return vo;
    }
}
