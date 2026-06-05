package com.repair.backend.controller;

import com.repair.backend.service.IArticleService;
import com.repair.backend.service.IOrderService;
import com.repair.backend.service.IUserService;
import com.repair.backend.entity.Order;
import com.repair.backend.entity.Article;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IArticleService articleService;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> result = new HashMap<>();

        // 1. Total Users
        long userCount = userService.count();
        result.put("userCount", userCount);
        // Mock growth rate for now as we don't track historical daily counts yet
        result.put("userGrowth", "12%"); 

        // 2. Total Orders
        long orderCount = orderService.count();
        result.put("orderCount", orderCount);
        result.put("orderGrowth", "5%");

        // 3. Total Transaction Amount (Sum of price where status is meaningful)
        List<Order> paidOrders = orderService.list(new LambdaQueryWrapper<Order>()
                .in(Order::getStatus, "PAID", "WAITING_GRAB", "SERVICE_ING", "FINISHED", "RATED"));
        BigDecimal totalAmount = paidOrders.stream()
                .map(Order::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("totalAmount", totalAmount);
        result.put("amountGrowth", "20%");

        // 4. Pending Work Orders (WAITING_GRAB)
        long pendingOrders = orderService.count(new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, "WAITING_GRAB"));
        result.put("pendingOrders", pendingOrders);

        return result;
    }
    
    @GetMapping("/notices")
    public List<Article> getNotices() {
        // Return latest 4 articles
        return articleService.list(new LambdaQueryWrapper<Article>()
                .orderByDesc(Article::getCreateTime)
                .last("LIMIT 4"));
    }
}
