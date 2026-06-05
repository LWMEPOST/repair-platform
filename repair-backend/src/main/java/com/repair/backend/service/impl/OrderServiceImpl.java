package com.repair.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repair.backend.entity.Order;
import com.repair.backend.mapper.OrderMapper;
import com.repair.backend.service.IOrderService;
import com.repair.backend.entity.WorkerInfo;
import com.repair.backend.service.IWorkerService;
import com.repair.backend.entity.Comment;
import com.repair.backend.service.ICommentService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private ICommentService commentService;

    @Override
    public boolean createOrder(Order order) {
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        // 恢复正常流程：CREATED -> PAID -> WAITING_GRAB
        order.setStatus("CREATED"); 
        order.setCreateTime(LocalDateTime.now());
        return this.save(order);
    }

    @Override
    public List<Order> getWaitingOrders() {
        return this.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, "WAITING_GRAB")
                .orderByDesc(Order::getCreateTime));
    }

    @Override
    @Transactional
    public boolean grabOrder(Long orderId, Long workerId) {
        if (redissonClient == null) {
            throw new RuntimeException("RedissonClient is null");
        }
        String lockKey = "order:grab:" + orderId;
        RLock lock = redissonClient.getLock(lockKey);
        
        try {
            if (lock.tryLock(3, 10, TimeUnit.SECONDS)) {
                Order order = this.getById(orderId);
                if (order == null) throw new RuntimeException("订单不存在");
                if (!"WAITING_GRAB".equals(order.getStatus())) {
                    throw new RuntimeException("订单已被抢");
                }

                // 检查师傅认证状态
                WorkerInfo workerInfo = workerService.getByUserId(workerId);
                if (workerInfo == null || !"AUTHED".equals(workerInfo.getAuthStatus())) {
                    throw new RuntimeException("师傅未实名认证，无法接单");
                }
                
                order.setStatus("SERVICE_ING");
                order.setWorkerId(workerId);
                order.setGrabTime(LocalDateTime.now());
                
                boolean updated = this.updateById(order);
                if (!updated) {
                    throw new RuntimeException("订单已被抢");
                }
                return true;
            } else {
                throw new RuntimeException("订单已被抢");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("订单已被抢");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public boolean confirmWorker(Long orderId, boolean accept) {
        Order order = this.getById(orderId);
        if (order != null && "WAITING_USER_CONFIRM".equals(order.getStatus())) {
            if (accept) {
                order.setStatus("WAITING_START");
            } else {
                order.setStatus("WAITING_GRAB");
                order.setWorkerId(null); // 清除师傅ID，重新进入抢单池
            }
            return this.updateById(order);
        }
        return false;
    }

    @Override
    public boolean startService(Long orderId) {
        Order order = this.getById(orderId);
        if (order != null && "WAITING_START".equals(order.getStatus())) {
            order.setStatus("SERVICE_ING");
            return this.updateById(order);
        }
        return false;
    }

    @Override
    public boolean payOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order != null && "CREATED".equals(order.getStatus())) {
            order.setStatus("WAITING_GRAB"); // 支付后进入抢单池
            order.setPayTime(LocalDateTime.now());
            return this.updateById(order);
        }
        return false;
    }

    @Override
    public boolean finishOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order != null && "SERVICE_ING".equals(order.getStatus())) {
            order.setStatus("FINISHED");
            order.setFinishTime(LocalDateTime.now());
            return this.updateById(order);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean rateOrder(Long orderId, Integer score, String content) {
        Order order = this.getById(orderId);
        if (order != null && "FINISHED".equals(order.getStatus())) {
            order.setStatus("RATED");
            this.updateById(order);
            
            Comment comment = new Comment();
            comment.setOrderId(orderId);
            comment.setUserId(order.getUserId());
            comment.setWorkerId(order.getWorkerId());
            comment.setScore(score);
            comment.setContent(content);
            comment.setCreateTime(LocalDateTime.now());
            
            return commentService.save(comment);
        }
        return false;
    }

    @Override
    public List<Order> listByUser(Long userId) {
        return this.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreateTime));
    }

    @Override
    public List<Order> listByWorker(Long workerId) {
        return this.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getWorkerId, workerId)
                .orderByDesc(Order::getGrabTime));
    }
}
