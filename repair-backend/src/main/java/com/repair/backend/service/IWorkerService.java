package com.repair.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repair.backend.entity.WorkerInfo;
import com.repair.backend.entity.WorkerSkill;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface IWorkerService extends IService<WorkerInfo> {
    // 获取师傅信息
    WorkerInfo getByUserId(Long userId);
    // 提交认证
    boolean submitAuth(Long userId, WorkerInfo info);
    // 获取技能列表
    List<WorkerSkill> getSkills(Long userId);
    // 添加技能
    boolean addSkill(Long userId, String skillName);
    // 删除技能
    boolean deleteSkill(Long skillId);

    // 分页查询师傅
    Page<WorkerInfo> getWorkerPage(Page<WorkerInfo> page, String status);
    // 审核师傅
    boolean auditWorker(Long workerId, boolean pass, String reason);
}
