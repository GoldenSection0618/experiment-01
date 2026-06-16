<template>
  <el-container class="app-layout">
    <AppHeader @toggle-menu="toggleMenu" />

    <el-container class="app-body">
      <div v-show="menuVisible && isSmallScreen" class="menu-overlay" @click="closeMenu" />

      <el-aside :class="['app-aside', { 'menu-visible': menuVisible }]" :width="isSmallScreen ? '220px' : 'auto'">
        <SideMenu />
      </el-aside>

      <el-main class="app-main">
        <slot />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import AppHeader from './AppHeader.vue';
import SideMenu from './SideMenu.vue';

export default {
  name: 'AppLayout',
  components: { AppHeader, SideMenu },
  data() {
    return {
      menuVisible: false,
      isSmallScreen: false
    };
  },
  created() {
    this.onResize();
    window.addEventListener('resize', this.onResize);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.onResize);
  },
  methods: {
    onResize() {
      this.isSmallScreen = window.innerWidth <= 1000;
      if (!this.isSmallScreen) {
        this.menuVisible = false;
      }
    },
    toggleMenu() {
      this.menuVisible = !this.menuVisible;
    },
    closeMenu() {
      this.menuVisible = false;
    }
  }
};
</script>

<style scoped>
.app-layout { height: 100vh; overflow: hidden; }
.app-body   { height: calc(100vh - 60px); }
.app-aside  {
  background-color: var(--menu-bg, #304156);
  overflow-y: auto;
  overflow-x: hidden;
  transition: transform 0.3s ease;
}
.app-main   {
  background-color: #f5f7fa;
  overflow-y: auto;
  padding: 24px;
}
</style>
