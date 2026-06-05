const mysql = require('mysql2/promise');
const axios = require('axios');

async function runTest() {
    const connection = await mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: 'root',
        database: 'repair_db'
    });

    console.log('Connected to DB. Preparing test data...');

    // Generate 50 workers
    const workerIds = [];
    for (let i = 1; i <= 50; i++) {
        // Insert user
        const [userResult] = await connection.execute(
            `INSERT INTO sys_user (username, password, role) VALUES (?, '123456', 'WORKER')`,
            [`test_worker_${i}_${Date.now()}`]
        );
        const userId = userResult.insertId;
        
        // Insert worker info
        await connection.execute(
            `INSERT INTO sys_worker_info (user_id, real_name, id_card, auth_status) VALUES (?, ?, ?, 'AUTHED')`,
            [userId, `师傅${i}`, `11010519900101${String(i).padStart(4, '0')}`]
        );
        workerIds.push(userId);
    }

    // Generate 1 order
    const [orderResult] = await connection.execute(
        `INSERT INTO sys_order (order_no, user_id, service_content, address, contact_phone, price, status, create_time) 
         VALUES (?, 1, '测试并发抢单', '测试地址', '13800138000', 100.00, 'WAITING_GRAB', NOW())`,
        [`TEST_GRAB_${Date.now()}`]
    );
    const orderId = orderResult.insertId;

    console.log(`Created 50 workers and 1 order (ID: ${orderId}).`);
    console.log('Starting 50 concurrent grab requests...');

    const promises = workerIds.map(workerId => {
        return axios.post(`http://localhost:8080/api/orders/${orderId}/grab?workerId=${workerId}`)
            .then(res => ({ workerId, success: true, data: res.data }))
            .catch(err => ({ workerId, success: false, data: err.response?.data || err.message }));
    });

    const results = await Promise.all(promises);

    let successCount = 0;
    let failCount = 0;
    let expectedFailCount = 0;
    let successWorkerId = null;

    results.forEach(r => {
        if (r.data === '抢单成功') {
            successCount++;
            successWorkerId = r.workerId;
        } else {
            failCount++;
            if (r.data === '抢单失败，订单已被抢') {
                expectedFailCount++;
            } else {
                console.log(`[Worker ${r.workerId}] Unexpected fail message: ${r.data}`);
            }
        }
    });

    console.log(`\n=== Test Results ===`);
    console.log(`Total Requests: 50`);
    console.log(`Success Count: ${successCount} (Expected: 1)`);
    console.log(`Fail Count: ${failCount} (Expected: 49)`);
    console.log(`Failed with message "抢单失败，订单已被抢": ${expectedFailCount} (Expected: 49)`);

    // Verify DB state
    const [finalOrderRows] = await connection.execute(
        `SELECT worker_id, status FROM sys_order WHERE id = ?`,
        [orderId]
    );
    const finalOrder = finalOrderRows[0];

    console.log(`\n=== Final Order State ===`);
    console.log(`Status: ${finalOrder.status} (Expected: SERVICE_ING)`);
    console.log(`Worker ID: ${finalOrder.worker_id} (Expected: ${successWorkerId})`);

    await connection.end();
}

runTest().catch(console.error);