<template>
  <section class="avatar-section">
    <div class="avatar-content">
      <h3 class="avatar-title">设置头像</h3>
      <div class="avatar-preview" @click="$refs.fileInput.click()">
        <img v-if="url" :src="url" alt="头像预览" class="avatar-img" />
        <div v-else class="avatar-placeholder">
          <i class="el-icon-camera-solid"></i>
          <span>点击上传头像</span>
        </div>
      </div>
      <p class="avatar-hint">支持 JPG、PNG 格式，大小不超过 2MB</p>
      <input ref="fileInput" type="file" accept="image/jpeg,image/png" hidden @change="onFileChange" />
    </div>
  </section>
</template>

<script>
export default {
  name: 'AvatarUploader',
  data() {
    return {
      url: ''
    };
  },
  methods: {
    onFileChange(e) {
      const file = e.target.files[0];
      if (!file) {
        return;
      }
      const isImage = /image\/(jpeg|png)/.test(file.type);
      if (!isImage) {
        this.$message.error('仅支持 JPG、PNG 格式');
        return;
      }
      const isOverSize = file.size / 1024 / 1024 > 2;
      if (isOverSize) {
        this.$message.error('图片大小不能超过 2MB');
        return;
      }
      const reader = new FileReader();
      reader.onload = (ev) => {
        this.url = ev.target.result;
        this.$message.success('头像上传成功');
      };
      reader.readAsDataURL(file);
    }
  }
};
</script>

<style scoped>
.avatar-section {
  width: 360px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: linear-gradient(180deg, #f8f9fc, #e8ecf2);
}
.avatar-content  { text-align: center; max-width: 280px; }
.avatar-title    { font-size: 20px; font-weight: 600; color: #303133; margin-bottom: 24px; }
.avatar-preview  {
  width: 180px;
  height: 180px;
  border-radius: 50%;
  margin: 0 auto 16px;
  cursor: pointer;
  overflow: hidden;
  background: #fff;
  border: 3px dashed #d3d9e6;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}
.avatar-preview:hover {
  border-color: var(--primary-color, #409EFF);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}
.avatar-img       { width: 100%; height: 100%; object-fit: cover; }
.avatar-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: #909399;
}
.avatar-placeholder i   { font-size: 36px; color: #c0c4cc; }
.avatar-placeholder span { font-size: 13px; }
.avatar-hint      { font-size: 12px; color: #c0c4cc; }
</style>
