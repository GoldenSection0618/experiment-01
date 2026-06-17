# 校园图书借阅自助管理系统设计说明

## 项目定位

`library` 分支用于 Web 应用开发大作业，课题为“校园图书借阅自助管理系统”。`main` 分支保留实验 1 到实验 3 的稳定版本。

本分支从实验三的 SpringBoot + MyBatis 项目继续演进，但图书系统不再以 `login_user` / `user_info` 作为核心业务模型。后续业务会围绕图书、分类、借阅记录、借阅规则和系统用户展开。

第 0 步只完成基础隔离、数据库草案、设计文档和开发边界冻结，不实现完整借阅业务。

## 数据库隔离

实验三数据库是 `experiment_01`。

图书系统数据库是 `experiment_01_library`。

脚本位置为项目根目录 `database.sql`，只允许创建和操作 `experiment_01_library`。`library` 分支不得连接或操作实验三数据库 `experiment_01`。

当前 `springboot_back/src/main/resources/application.properties` 已将默认连接库改为 `experiment_01_library`，保证在 `library` 分支直接启动时不会继续使用实验三数据。

## 继承关系

可以继承的内容：

- SpringBoot 后端工程结构
- Controller -> Service -> Mapper -> XML 的分层方式
- `Result` 统一返回结构
- MyBatis XML 手写 SQL 的方式
- 前后端分离的请求方式

需要替换的内容：

- 旧 `login_user` / `user_info` 业务模型
- 旧用户列表中心业务
- 旧 Vue2 + ElementUI 技术栈
- 旧 `experiment_01` 数据库连接

当前 Java 包名可以暂时保持 `com.example.experiment2`，后续如需重命名包，应单独安排一步处理。

## 技术栈目标

大作业最终目标：

- 前端：Vue3 + Vue Router + Pinia + Axios + Element Plus
- 后端：SpringBoot + MyBatis + MySQL 8.0
- 认证：JWT
- SQL：复杂查询、条件查询、统计查询、多表关联查询写在 MyBatis XML 中

禁止使用 MyBatis-Plus，禁止静态假数据，禁止伪功能。第 0 步不实现 JWT 和完整业务流程。

## 核心数据表

`sys_user`：系统用户表，用于登录、注册、角色权限和账号状态。主键为 `id`，`username` 和 `email` 唯一。

`book_category`：图书分类表，用于管理图书分类。主键为 `id`，`name` 唯一。

`book`：图书基础信息和库存表。主键为 `id`，通过 `category_id` 外键关联 `book_category(id)`，`isbn` 唯一。

`borrow_rule`：借阅规则表，用于配置借阅天数、续借天数、最大续借次数和逾期罚金，避免把规则硬编码在 Service 中。

`borrow_record`：借阅记录表，用于记录借阅、续借、归还、逾期和罚金信息。主键为 `id`，通过 `user_id` 外键关联 `sys_user(id)`，通过 `book_id` 外键关联 `book(id)`，通过 `rule_id` 外键关联 `borrow_rule(id)`。

`borrow_record.rule_id` 用于记录每条借阅记录生成时采用的借阅规则。后续即使默认规则被调整，历史借阅记录仍能追溯当时使用的借阅天数、续借次数和罚金配置。

## 业务状态机

图书状态：

- `ON_SHELF`：上架展示
- `OFF_SHELF`：下架，不允许借阅

图书可借条件必须同时满足：

- `book.status = ON_SHELF`
- `book.available_stock > 0`

借阅状态：

- `BORROWED`：已借出，未逾期，未归还
- `OVERDUE`：已逾期，未归还
- `RETURNED`：已归还

状态流转：

- 创建借阅：无记录 -> `BORROWED`
- 定时或查询时发现逾期：`BORROWED` -> `OVERDUE`
- 正常归还：`BORROWED` -> `RETURNED`
- 逾期归还：`OVERDUE` -> `RETURNED`

禁止状态：

- `RETURNED` 不能续借
- `OVERDUE` 不能续借
- `RETURNED` 不能再次归还
- `OFF_SHELF` 图书不能借
- `available_stock = 0` 的图书不能借
- 同一用户不能重复借同一本未归还图书

同一用户不能对同一本书存在 `status in ('BORROWED', 'OVERDUE')` 的未归还记录。后续 Service 层和 MyBatis XML 中必须先查询 `countActiveBorrow`，再决定是否允许借阅。

## 库存联动规则

- 借阅成功：`book.available_stock - 1`
- 归还成功：`book.available_stock + 1`
- 续借不改变库存
- 借阅失败不改变库存
- 库存字段必须满足 `0 <= available_stock <= total_stock`

SQL 脚本中保留 `CHECK` 约束，Service 层后续也必须做库存校验，避免只依赖数据库约束。

## 权限边界

普通用户 `USER`：

- 注册
- 登录
- 修改自己的密码
- 查询图书
- 查看图书详情
- 借阅图书
- 查看我的借阅记录
- 续借
- 归还
- 查看自己的逾期天数和罚金

管理员 `ADMIN`：

- 登录
- 修改自己的密码
- 用户管理
- 图书分类管理
- 图书管理
- 全部借阅记录管理
- 逾期记录查看
- 统计分析

页面、路由、按钮和接口都必须做权限隔离。第 0 步只冻结设计，不实现完整权限系统。

## 后端模块规划

Controller：

- `AuthController`
- `UserController`
- `CategoryController`
- `BookController`
- `BorrowController`
- `StatisticsController`

Service：

- `AuthService` 或 `UserService`
- `CategoryService`
- `BookService`
- `BorrowService`
- `StatisticsService`

Mapper：

- `SysUserMapper`
- `BookCategoryMapper`
- `BookMapper`
- `BorrowRuleMapper`
- `BorrowRecordMapper`
- `StatisticsMapper`

XML：

- `SysUserMapper.xml`
- `BookCategoryMapper.xml`
- `BookMapper.xml`
- `BorrowRuleMapper.xml`
- `BorrowRecordMapper.xml`
- `StatisticsMapper.xml`

## 接口规划

认证：

- `POST /api/auth/login`
- `POST /api/auth/register`
- `POST /api/auth/password`

图书分类：

- `GET /api/categories`
- `POST /api/categories`
- `PUT /api/categories/{id}`
- `PUT /api/categories/{id}/status`

图书：

- `GET /api/books`
- `GET /api/books/{id}`
- `POST /api/books`
- `PUT /api/books/{id}`
- `PUT /api/books/{id}/status`

借阅：

- `POST /api/borrows/{bookId}`
- `POST /api/borrows/{recordId}/renew`
- `POST /api/borrows/{recordId}/return`
- `GET /api/borrows/my`
- `GET /api/borrows`

统计：

- `GET /api/statistics/summary`
- `GET /api/statistics/top-books`
- `GET /api/statistics/overdue`

## 前端迁移策略

当前仓库前端是 Vue2 + ElementUI。大作业最终必须迁移到 Vue3 + Vue Router + Pinia + Axios + Element Plus。

第 0 步不做前端迁移。后续应单独进行前端迁移，避免在同一步同时修改数据库、后端业务和前端框架。

后续建议前端结构：

- `vue_front/src/api`
- `vue_front/src/router`
- `vue_front/src/stores`
- `vue_front/src/views`
- `vue_front/src/components`

## 第 0 步不做事项

- 不实现完整借阅功能
- 不实现完整 JWT
- 不实现完整前端页面迁移
- 不引入 MyBatis-Plus
- 不把业务 SQL 写成注解 SQL
- 不继续把 `login_user` / `user_info` 作为图书系统主模型
- 不删除 `main` 分支相关内容
- 不操作 `experiment_01` 数据库
- 不复制开源图书系统模板
- 不生成大量过度封装代码
