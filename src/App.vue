<template>
  <div id="app-root" :class="`theme-${currentTheme}`">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      currentTheme: 'blue'
    };
  },
  provide() {
    return {
      getTheme: () => this.currentTheme,
      setTheme: this.setTheme
    };
  },
  methods: {
    setTheme(theme) {
      this.currentTheme = theme;
    }
  }
};
</script>

<style>
#app-root {
  min-height: 100vh;
}
</style>
