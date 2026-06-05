package com.repair.backend.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.repair.backend.entity.Order;
import java.util.List;

public interface IOrderService extends IService<Order> {
    // 创建订单
    boolean createOrder(Order order);
    // 获取待抢订单
    List<Order> getWaitingOrders();
    // 抢单
    boolean grabOrder(Long orderId, Long workerId);

    // 用户确认师傅
    boolean confirmWorker(Long orderId, boolean accept);
    
    // 开始服务
    boolean startService(Long orderId);

    // 支付订单
    boolean payOrder(Long orderId);
    // 完工
    boolean finishOrder(Long orderId);
    // 评价
    boolean rateOrder(Long orderId, Integer score, String content);
    
    // 获取用户订单
    List<Order> listByUser(Long userId);
    // 获取师傅订单
    List<Order> listByWorker(Long workerId);
}
