<template>
  <div class="article-list-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>文章管理</span>
          <el-button type="primary" @click="handleAdd">新增文章</el-button>
        </div>
      </template>

      <!-- 筛选栏 -->
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="分类">
          <el-select v-model="queryForm.category" placeholder="选择分类" clearable>
            <el-option label="维修指南" value="维修指南" />
            <el-option label="家电科普" value="家电科普" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="category" label="分类" width="120">
          <template #default="scope">
            <el-tag>{{ scope.row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="coverImg" label="封面" width="120">
          <template #default="scope">
            <el-image 
              v-if="scope.row.coverImg"
              style="width: 50px; height: 50px" 
              :src="scope.row.coverImg" 
              fit="cover" 
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        style="margin-top: 20px; justify-content: flex-end;"
        background
        layout="prev, pager, next"
        :total="total"
        v-model:current-page="page"
        @current-change="fetchData"
      />
    </el-card>

    <!-- 编辑/新增弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑文章' : '新增文章'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择">
            <el-option label="维修指南" value="维修指南" />
            <el-option label="家电科普" value="家电科普" />
          </el-select>
        </el-form-item>
        <el-form-item label="封面URL">
          <el-input v-model="form.coverImg" placeholder="输入图片链接" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const queryForm = reactive({
  category: ''
})

const dialogVisible = ref(false)
const form = reactive({
  id: null,
  title: '',
  category: '',
  coverImg: '',
  content: ''
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/articles', {
      params: {
        page: page.value,
        size: 10,
        category: queryForm.category
      }
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (err) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  form.id = null
  form.title = ''
  form.category = ''
  form.coverImg = ''
  form.content = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这篇文章吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/api/articles/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (err) {
      ElMessage.error('删除失败')
    }
  })
}

const submitForm = async () => {
  try {
    if (form.id) {
      await axios.put('/api/articles', form)
    } else {
      await axios.post('/api/articles', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  } catch (err) {
    ElMessage.error('保存失败')
  }
}

const formatTime = (time) => {
  return time ? time.replace('T', ' ').substring(0, 16) : ''
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
