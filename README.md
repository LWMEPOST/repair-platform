# 上门维修服务平台

## 项目介绍

本项目是一个上门维修服务平台，包含后台管理端、用户端、维修人员端和后端服务。系统围绕用户报修、工单派发、维修人员接单、服务进度跟踪、评价反馈和后台业务管理等流程设计。

## 技术栈

- Spring Boot 3
- MyBatis-Plus
- MySQL
- Redis/Redisson
- Vue 3
- Vite
- Element Plus
- 移动端 H5/前端应用

## 部署要求

- JDK 17
- Maven 3.x
- MySQL 8.0
- Redis
- Node.js 18 或以上
- npm/pnpm

## 运行流程

1. 创建 MySQL 数据库并导入初始化 SQL（如项目提供）。
2. 启动 Redis，并修改 repair-backend 中的数据库与 Redis 配置。
3. 在 repair-backend 目录执行 mvn spring-boot:run 启动后端。
4. 分别进入 repair-admin、repair-user、repair-worker 执行 npm install。
5. 执行 npm run dev 启动管理端、用户端和维修端前端项目。

## 项目结构

- repair-backend：Spring Boot 后端服务
- repair-admin：后台管理端
- repair-user：用户端前端
- repair-worker：维修人员端前端
- package.json：前端依赖与脚本入口

## 上传说明

本仓库只保留项目运行和二次开发所需的代码、配置、数据库脚本及少量必要静态资源。

以下内容不会上传：论文、答辩材料、临时文档、依赖目录、构建产物、压缩包、数据集、模型权重、视频、日志、本地工具包以及密钥配置。
