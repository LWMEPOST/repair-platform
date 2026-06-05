package com.repair.backend.controller;

import com.repair.backend.entity.WorkerInfo;
import com.repair.backend.entity.WorkerSkill;
import com.repair.backend.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.repair.backend.entity.Comment;
import com.repair.backend.entity.Order;
import com.repair.backend.entity.User;
import com.repair.backend.service.ICommentService;
import com.repair.backend.service.IOrderService;
import com.repair.backend.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    @Autowired
    private IWorkerService workerService;
    
    @Autowired
    private ICommentService commentService;
    
    @Autowired
    private IOrderService orderService;
    
    @Autowired
    private IUserService userService;

    // 获取我的收益
    @GetMapping("/earnings/{workerId}")
    public Map<String, Object> getEarnings(@PathVariable Long workerId) {
        List<Order> orders = orderService.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getWorkerId, workerId)
                .in(Order::getStatus, "FINISHED", "RATED")
                .orderByDesc(Order::getFinishTime));

        BigDecimal total = orders.stream()
                .map(Order::getPrice)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("records", orders);
        return result;
    }

    // 管理端分页查询
    @GetMapping("/list")
    public Page<WorkerInfo> listWorkers(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) String status) {
        return workerService.getWorkerPage(new Page<>(page, size), status);
    }

    // 管理端审核
    @PostMapping("/audit")
    public String auditWorker(@RequestBody Map<String, Object> params) {
        Long workerId = Long.valueOf(params.get("id").toString());
        boolean pass = Boolean.parseBoolean(params.get("pass").toString());
        String reason = (String) params.get("reason");
        return workerService.auditWorker(workerId, pass, reason) ? "操作成功" : "操作失败";
    }

    // 获取公开信息 (供用户端查看)
    @GetMapping("/public/{userId}")
    public Map<String, Object> getPublicInfo(@PathVariable Long userId) {
        WorkerInfo info = workerService.getByUserId(userId);
        if (info == null) return null;
        
        List<WorkerSkill> skills = workerService.getSkills(userId);
        
        // 计算评分和接单数
        long orderCount = orderService.count(new LambdaQueryWrapper<Order>().eq(Order::getWorkerId, userId));
        List<Comment> comments = commentService.list(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getWorkerId, userId)
                .orderByDesc(Comment::getCreateTime));
        
        double avgScore = comments.isEmpty() ? 5.0 : comments.stream().mapToInt(Comment::getScore).average().orElse(5.0);
        
        // 组装评论列表（带用户名）
        List<Map<String, Object>> commentList = comments.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("score", c.getScore());
            map.put("content", c.getContent());
            map.put("createTime", c.getCreateTime());
            User user = userService.getById(c.getUserId());
            map.put("userName", user != null ? (user.getName() != null ? user.getName() : user.getUsername()) : "匿名用户");
            map.put("avatar", user != null ? user.getAvatar() : null);
            return map;
        }).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("realName", info.getRealName()); // 可以考虑脱敏
        result.put("authStatus", info.getAuthStatus());
        result.put("skills", skills);
        result.put("score", String.format("%.1f", avgScore)); 
        result.put("orderCount", orderCount); 
        result.put("comments", commentList);
        
        return result;
    }

    @GetMapping("/info/{userId}")
    public WorkerInfo getInfo(@PathVariable Long userId) {
        return workerService.getByUserId(userId);
    }

    @PostMapping("/auth")
    public String submitAuth(@RequestBody WorkerInfo info) {
        // info.getUserId() needs to be set from frontend or token
        return workerService.submitAuth(info.getUserId(), info) ? "提交成功" : "提交失败";
    }

    @GetMapping("/skills/{userId}")
    public List<WorkerSkill> getSkills(@PathVariable Long userId) {
        return workerService.getSkills(userId);
    }

    @PostMapping("/skills")
    public String addSkill(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        String skillName = (String) params.get("skillName");
        return workerService.addSkill(userId, skillName) ? "添加成功" : "添加失败";
    }

    @DeleteMapping("/skills/{skillId}")
    public String deleteSkill(@PathVariable Long skillId) {
        return workerService.deleteSkill(skillId) ? "删除成功" : "删除失败";
    }
}
