package com.repair.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repair.backend.entity.WorkerInfo;
import com.repair.backend.entity.WorkerSkill;
import com.repair.backend.mapper.WorkerInfoMapper;
import com.repair.backend.mapper.WorkerSkillMapper;
import com.repair.backend.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Service
public class WorkerServiceImpl extends ServiceImpl<WorkerInfoMapper, WorkerInfo> implements IWorkerService {

    @Autowired
    private WorkerSkillMapper workerSkillMapper;

    @Override
    public WorkerInfo getByUserId(Long userId) {
        return this.getOne(new LambdaQueryWrapper<WorkerInfo>().eq(WorkerInfo::getUserId, userId));
    }

    @Override
    public boolean submitAuth(Long userId, WorkerInfo info) {
        WorkerInfo exist = getByUserId(userId);
        if (exist == null) {
            info.setUserId(userId);
            info.setAuthStatus("AUDITING"); // 提交即审核中 (模拟)
            return this.save(info);
        } else {
            exist.setRealName(info.getRealName());
            exist.setIdCard(info.getIdCard());
            exist.setIdCardFront(info.getIdCardFront());
            exist.setIdCardBack(info.getIdCardBack());
            exist.setAuthStatus("AUDITING");
            return this.updateById(exist);
        }
    }

    @Override
    public List<WorkerSkill> getSkills(Long userId) {
        return workerSkillMapper.selectList(new LambdaQueryWrapper<WorkerSkill>().eq(WorkerSkill::getUserId, userId));
    }

    @Override
    public boolean addSkill(Long userId, String skillName) {
        WorkerSkill skill = new WorkerSkill();
        skill.setUserId(userId);
        skill.setSkillName(skillName);
        return workerSkillMapper.insert(skill) > 0;
    }

    @Override
    public boolean deleteSkill(Long skillId) {
        return workerSkillMapper.deleteById(skillId) > 0;
    }

    @Override
    public Page<WorkerInfo> getWorkerPage(Page<WorkerInfo> page, String status) {
        return this.page(page, new LambdaQueryWrapper<WorkerInfo>()
                .eq(StringUtils.isNotBlank(status), WorkerInfo::getAuthStatus, status)
                .orderByDesc(WorkerInfo::getCreateTime));
    }

    @Override
    public boolean auditWorker(Long workerId, boolean pass, String reason) {
        WorkerInfo worker = this.getById(workerId);
        if (worker != null) {
            worker.setAuthStatus(pass ? "AUTHED" : "REJECTED");
            // 这里可以记录审核原因 reason，暂时仅更新状态
            return this.updateById(worker);
        }
        return false;
    }
}
