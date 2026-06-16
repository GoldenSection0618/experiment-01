<template>
  <div class="side-menu">
    <el-menu
      :default-active="activeMenu"
      :collapse="collapsed"
      :unique-opened="true"
      router
      class="side-menu-el"
    >
      <el-menu-item index="/home">
        <i class="el-icon-s-home"></i>
        <span slot="title">主页</span>
      </el-menu-item>
      <el-menu-item index="/info">
        <i class="el-icon-info"></i>
        <span slot="title">信息页面</span>
      </el-menu-item>
      <el-menu-item index="/users">
        <i class="el-icon-user-solid"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>
    </el-menu>

    <div class="theme-switcher">
      <div class="theme-switcher-title">
        <i class="el-icon-s-marketing"></i>
        <span v-if="!collapsed">主题切换</span>
      </div>
      <div class="theme-buttons">
        <div
          :class="['theme-btn', 'theme-btn-blue', { active: currentTheme === 'blue' }]"
          @click="handleThemeChange('blue')"
          title="蓝色风格"
        >
          <span class="theme-dot"></span>
          <span v-if="!collapsed">蓝色风格</span>
        </div>
        <div
          :class="['theme-btn', 'theme-btn-yellow', { active: currentTheme === 'yellow' }]"
          @click="handleThemeChange('yellow')"
          title="黄色风格"
        >
          <span class="theme-dot"></span>
          <span v-if="!collapsed">黄色风格</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SideMenu',
  inject: ['getTheme', 'setTheme'],
  props: {
    collapsed: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path;
    },
    currentTheme() {
      return this.getTheme ? this.getTheme() : 'blue';
    }
  },
  methods: {
    handleThemeChange(theme) {
      if (this.setTheme) {
        this.setTheme(theme);
        this.$message.success(`已切换至${theme === 'blue' ? '蓝色' : '黄色'}风格`);
      }
    }
  }
};
</script>

<style scoped>
.side-menu {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.side-menu-el {
  flex: 1;
  border-right: none;
}

.side-menu-el:not(.el-menu--collapse) {
  width: 220px;
}

.theme-switcher {
  padding: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.theme-switcher-title {
  color: var(--menu-text, #bfcbd9);
  font-size: 13px;
  padding: 8px 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.theme-buttons {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 4px 0;
}

.theme-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.25s ease;
  color: var(--menu-text, #bfcbd9);
  font-size: 13px;
  user-select: none;
}

.theme-btn:hover {
  background-color: var(--menu-hover-bg, #263445);
}

.theme-btn.active {
  background-color: var(--menu-active-bg, #409EFF);
  color: var(--menu-active-text, #ffffff);
}

.theme-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  flex-shrink: 0;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.theme-btn-blue .theme-dot {
  background: #409EFF;
}

.theme-btn-yellow .theme-dot {
  background: #E6A23C;
}

.theme-btn.active .theme-dot {
  border-color: #ffffff;
}
</style>
