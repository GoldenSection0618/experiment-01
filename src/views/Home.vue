<template>
  <app-layout>
    <div class="home-content">
      <section class="welcome-bar">
        <div>
          <h2>欢迎，{{ username }}</h2>
          <p class="welcome-date">{{ currentDate }}</p>
        </div>
        <div class="stat-row">
          <div class="stat" v-for="s in stats" :key="s.label">
            <strong>{{ s.value }}</strong>
            <span>{{ s.label }}</span>
          </div>
        </div>
      </section>

      <section class="image-row">
        <div class="img-card c1"><i class="el-icon-picture-outline"></i><span>图片展示区 1</span></div>
        <div class="img-card c2"><i class="el-icon-picture-outline"></i><span>图片展示区 2</span></div>
        <div class="img-card c3"><i class="el-icon-picture-outline"></i><span>图片展示区 3</span></div>
      </section>

      <section class="home-grid">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <i class="el-icon-date"></i>
            <span>日历</span>
          </div>
          <el-calendar v-model="calendarValue">
            <template slot="dateCell" slot-scope="{ data }">
              <div class="cal-day" :class="{ today: data.day === todayStr }">
                {{ data.day.split('-').slice(2).join('') }}
              </div>
            </template>
          </el-calendar>
        </el-card>

        <div class="side-col">
          <el-card shadow="hover">
            <div slot="header" class="card-header">
              <i class="el-icon-s-data"></i>
              <span>系统信息</span>
            </div>
            <div class="info-list">
              <div class="info-row">
                <span>系统版本</span>
                <el-tag size="small" type="success">v1.0.0</el-tag>
              </div>
              <div class="info-row">
                <span>技术栈</span>
                <el-tag size="small" type="primary">Vue</el-tag>
              </div>
              <div class="info-row">
                <span>UI 框架</span>
                <el-tag size="small" type="warning">ElementUI</el-tag>
              </div>
              <div class="info-row">
                <span>前端路由</span>
                <el-tag size="small" type="info">vue-router</el-tag>
              </div>
            </div>
          </el-card>

          <el-card shadow="hover">
            <div slot="header" class="card-header">
              <i class="el-icon-s-flag"></i>
              <span>公告栏</span>
            </div>
            <div class="notice-list">
              <div class="notice" v-for="n in notices" :key="n.id">
                <span class="dot"></span>
                <div>
                  <p>{{ n.title }}</p>
                  <small>{{ n.time }}</small>
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </section>
    </div>
  </app-layout>
</template>

<script>
import AppLayout from '../components/AppLayout.vue';

export default {
  name: 'HomePage',
  components: { AppLayout },
  data() {
    const d = new Date();
    const pad = n => String(n).padStart(2, '0');
    return {
      username: '张三',
      calendarValue: new Date(),
      todayStr: `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`,
      stats: [
        { value: 12, label: '今日访问' },
        { value: 48, label: '消息通知' },
        { value: 6, label: '待办事项' }
      ],
      notices: [
        { id: 1, title: '系统升级通知：Vue 2.7 版本已发布', time: '2026-06-01' },
        { id: 2, title: '实验一验收时间安排已公布', time: '2026-05-28' },
        { id: 3, title: '请及时完成静态页面设计实验', time: '2026-05-25' },
        { id: 4, title: 'ElementUI 组件库使用培训通知', time: '2026-05-20' }
      ]
    };
  },
  computed: {
    currentDate() {
      const d = new Date();
      const weekdays = ['日', '一', '二', '三', '四', '五', '六'];
      return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日 星期${weekdays[d.getDay()]}`;
    }
  }
};
</script>

<style scoped>
.home-content { max-width: 1400px; }

/* 欢迎区 */
.welcome-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 12px;
  padding: 24px 32px;
  margin-bottom: 20px;
  color: #fff;
  background: linear-gradient(135deg, var(--accent-gradient-start, #409EFF), var(--accent-gradient-end, #66b1ff));
}
.welcome-bar h2  { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
.welcome-date    { font-size: 14px; opacity: 0.85; }
.stat-row        { display: flex; gap: 28px; }
.stat {
  text-align: center;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  padding: 12px 20px;
}
.stat strong { display: block; font-size: 22px; }
.stat span   { font-size: 12px; opacity: 0.85; }

/* 图片区 */
.image-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}
.img-card {
  height: 140px;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #fff;
  font-size: 15px;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.img-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15); }
.img-card i     { font-size: 36px; opacity: 0.8; }
.c1 { background: linear-gradient(135deg, #667eea, #764ba2); }
.c2 { background: linear-gradient(135deg, #f093fb, #f5576c); }
.c3 { background: linear-gradient(135deg, #4facfe, #00f2fe); }

/* 主内容区 */
.home-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}
.home-grid .el-card { border-radius: 10px; }
.side-col { display: flex; flex-direction: column; gap: 16px; }

/* 日历 */
.cal-day {
  text-align: center;
  padding: 4px;
}
.cal-day.today {
  background: var(--primary-color, #409EFF);
  color: #fff;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

/* 系统信息 */
.info-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}
.info-row:last-child { border-bottom: none; }
.info-row span { color: #606266; font-size: 14px; }

/* 公告栏 */
.notice-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.notice {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
}
.notice:last-child { border-bottom: none; }
.notice p     { font-size: 14px; color: #303133; margin-bottom: 4px; }
.notice small { font-size: 12px; color: #c0c4cc; }
.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--primary-color, #409EFF);
  margin-top: 6px;
  flex-shrink: 0;
}
</style>
