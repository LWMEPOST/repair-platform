package com.repair.backend.controller;

import com.repair.backend.entity.Order;
import com.repair.backend.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    // 管理端分页查询
    @GetMapping("/list")
    public Page<Order> listOrders(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size,
                                  @RequestParam(required = false) String status,
                                  @RequestParam(required = false) String orderNo) {
        return orderService.page(new Page<>(page, size), new LambdaQueryWrapper<Order>()
                .eq(StringUtils.isNotBlank(status), Order::getStatus, status)
                .like(StringUtils.isNotBlank(orderNo), Order::getOrderNo, orderNo)
                .orderByDesc(Order::getCreateTime));
    }

    @PostMapping
    public String createOrder(@RequestBody Order order) {
        // order.setUserId(2L); // Removed: Use userId from request body
        boolean success = orderService.createOrder(order);
        return success ? "下单成功" : "下单失败";
    }

    @GetMapping("/waiting")
    public List<Order> getWaitingOrders() {
        return orderService.getWaitingOrders();
    }

    @PostMapping("/{orderId}/grab")
    public String grabOrder(@PathVariable Long orderId, @RequestParam Long workerId) {
        try {
            boolean success = orderService.grabOrder(orderId, workerId);
            return success ? "抢单成功" : "抢单失败";
        } catch (Exception e) {
            e.printStackTrace();
            return "抢单失败，" + e.getMessage();
        }
    }

    @PostMapping("/{orderId}/confirm")
    public String confirmWorker(@PathVariable Long orderId, @RequestParam boolean accept) {
        return orderService.confirmWorker(orderId, accept) ? "操作成功" : "操作失败";
    }

    @PostMapping("/{orderId}/start")
    public String startService(@PathVariable Long orderId) {
        return orderService.startService(orderId) ? "开始服务成功" : "操作失败";
    }

    @PostMapping("/{orderId}/pay")
    public String payOrder(@PathVariable Long orderId) {
        return orderService.payOrder(orderId) ? "支付成功" : "支付失败";
    }

    @PostMapping("/{orderId}/finish")
    public String finishOrder(@PathVariable Long orderId) {
        return orderService.finishOrder(orderId) ? "完工确认成功" : "操作失败";
    }

    @PostMapping("/{orderId}/rate")
    public String rateOrder(@PathVariable Long orderId, @RequestParam Integer score, @RequestParam String content) {
        return orderService.rateOrder(orderId, score, content) ? "评价成功" : "操作失败";
    }

    @GetMapping("/user/{userId}")
    public List<Order> listByUser(@PathVariable Long userId) {
        return orderService.listByUser(userId);
    }

    @GetMapping("/worker/{workerId}")
    public List<Order> listByWorker(@PathVariable Long workerId) {
        return orderService.listByWorker(workerId);
    }
}
