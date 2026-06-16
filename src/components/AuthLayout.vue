<template>
  <div class="auth-page">
    <div class="auth-card">
      <section class="auth-main">
        <slot />
      </section>
      <section v-if="$slots.side" class="auth-side">
        <slot name="side" />
      </section>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AuthLayout'
};
</script>
