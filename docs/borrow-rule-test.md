# 借阅规则验收说明

本文用于第 5 步验收：逾期刷新、续借限制和罚金计算。

## 准备

1. 启动后端和前端。
2. 使用 `student / 123456` 登录普通用户页面。
3. 使用 `admin / 123456` 登录管理员页面。

## 普通借阅

1. `student` 进入图书列表，借阅一本有库存的图书。
2. 进入“我的借阅”。
3. 页面应显示借阅时间、应还时间、剩余天数、续借次数和罚金。

## 正常续借

1. 对未逾期且续借次数未达上限的记录点击“续借”。
2. 后端应返回成功。
3. 列表刷新后，`dueTime` 后延，`renewCount` 增加 1。

默认测试数据中 `max_renew_count = 1`，所以同一条记录再次续借应失败，页面显示后端返回的“已达到最大续借次数”。

## 构造逾期

使用 MySQL 手动把一条未归还记录改成过去时间：

```sql
USE experiment_01_library;

UPDATE borrow_record
SET due_time = DATE_SUB(NOW(), INTERVAL 3 DAY),
    status = 'BORROWED',
    overdue_days = 0,
    fine_amount = 0
WHERE user_id = (SELECT id FROM sys_user WHERE username = 'student')
  AND status = 'BORROWED'
ORDER BY id DESC
LIMIT 1;
```

刷新“我的借阅”后，后端会懒刷新该用户逾期状态，记录应显示为 `OVERDUE`，并显示逾期天数和罚金。

## 逾期续借和归还

1. 对逾期记录点击“续借”，应失败，提示“借阅已逾期，不能续借”或“逾期记录不能续借”。
2. 对逾期记录点击“归还”，应成功。
3. 归还时后端按 `borrow_record.rule_id` 对应的 `borrow_rule.fine_per_day` 重新计算罚金，不依赖前端传值。
4. 归还成功后再次点击续借或归还，应失败。

## 管理员查看

1. `admin` 进入“借阅记录”。
2. 页面应显示用户名、书名、分类、借阅时间、应还时间、归还时间、状态、续借次数、逾期天数和罚金。
3. 使用状态筛选分别查询 `BORROWED`、`OVERDUE`、`RETURNED`。
4. 管理员页面只查看记录，不提供代还书、手动改状态、删除记录或修改罚金按钮。
