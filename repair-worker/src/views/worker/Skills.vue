<template>
  <div class="skill-page">
    <van-nav-bar
      title="技能管理"
      left-arrow
      @click-left="$router.back()"
      right-text="添加"
      @click-right="showAddDialog = true"
    />

    <van-list>
      <van-cell 
        v-for="skill in skills" 
        :key="skill.id" 
        :title="skill.skillName" 
        :label="formatTime(skill.createTime)"
      >
        <template #right-icon>
          <van-button size="mini" type="danger" @click="handleDelete(skill.id)">删除</van-button>
        </template>
      </van-cell>
      <van-empty v-if="skills.length === 0" description="暂无技能" />
    </van-list>

    <van-dialog v-model:show="showAddDialog" title="添加技能" show-cancel-button @confirm="handleAdd">
      <van-field v-model="newSkillName" label="技能名称" placeholder="例如：空调维修" />
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { showSuccessToast, showFailToast, showConfirmDialog } from 'vant';

const skills = ref([]);
const showAddDialog = ref(false);
const newSkillName = ref('');
const userInfo = ref({});

onMounted(() => {
  const userStr = localStorage.getItem('userInfo');
  if (userStr) {
    userInfo.value = JSON.parse(userStr);
    loadSkills();
  }
});

const loadSkills = async () => {
  try {
    const res = await axios.get(`/api/worker/skills/${userInfo.value.id}`);
    skills.value = res.data || [];
  } catch (e) {
    console.error(e);
  }
};

const handleAdd = async () => {
  if (!newSkillName.value) return;
  try {
    await axios.post('/api/worker/skills', {
      userId: userInfo.value.id,
      skillName: newSkillName.value
    });
    showSuccessToast('添加成功');
    newSkillName.value = '';
    loadSkills();
  } catch (e) {
    showFailToast('添加失败');
  }
};

const handleDelete = (id) => {
  showConfirmDialog({
    title: '确认删除',
    message: '确定要删除该技能吗？'
  }).then(async () => {
    try {
      await axios.delete(`/api/worker/skills/${id}`);
      showSuccessToast('删除成功');
      loadSkills();
    } catch (e) {
      showFailToast('删除失败');
    }
  });
};

const formatTime = (time) => {
  return time ? time.split('T')[0] : '';
};
</script>

<style scoped>
.skill-page {
  min-height: 100vh;
  background-color: #f7f8fa;
}
</style>
