<template>
  <div class="worker-audit">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>师傅审核管理</span>
          <div class="header-actions">
            <el-select v-model="queryParams.status" placeholder="认证状态" style="width: 120px; margin-right: 10px;" clearable @change="handleSearch">
              <el-option label="待审核" value="AUDITING" />
              <el-option label="已认证" value="AUTHED" />
              <el-option label="已驳回" value="REJECTED" />
              <el-option label="未认证" value="UNAUTH" />
            </el-select>
            <el-button type="primary" @click="handleSearch">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="idCard" label="身份证号" width="180" />
        <el-table-column prop="serviceArea" label="服务区域" />
        <el-table-column prop="authStatus" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.authStatus)">
              {{ getStatusText(scope.row.authStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="身份证" width="150">
          <template #default="scope">
            <el-image 
              v-if="scope.row.idCardFront"
              style="width: 50px; height: 50px" 
              :src="scope.row.idCardFront" 
              :preview-src-list="[scope.row.idCardFront, scope.row.idCardBack]" 
              preview-teleported
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <div v-if="scope.row.authStatus === 'AUDITING'">
              <el-button type="success" size="small" @click="handleAudit(scope.row, true)">通过</el-button>
              <el-button type="danger" size="small" @click="handleAudit(scope.row, false)">驳回</el-button>
            </div>
            <span v-else class="text-gray">无需操作</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.size"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 审核弹窗 -->
    <el-dialog v-model="auditDialogVisible" title="审核驳回" width="30%">
      <el-input
        v-model="auditReason"
        :rows="4"
        type="textarea"
        placeholder="请输入驳回原因"
      />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="auditDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReject">确认驳回</el-button>
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
const auditDialogVisible = ref(false)
const auditReason = ref('')
const currentWorkerId = ref(null)

const queryParams = reactive({
  page: 1,
  size: 10,
  status: 'AUDITING' // 默认只看待审核
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await axios.get('/worker/list', { params: queryParams })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.page = 1
  fetchList()
}

const handleSizeChange = (val) => {
  queryParams.size = val
  fetchList()
}

const handleCurrentChange = (val) => {
  queryParams.page = val
  fetchList()
}

const handleAudit = (row, pass) => {
  if (pass) {
    ElMessageBox.confirm(
      `确认通过师傅 ${row.realName} 的认证申请吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
      .then(async () => {
        try {
          await axios.post('/worker/audit', {
            id: row.id,
            pass: true,
            reason: '审核通过'
          })
          ElMessage.success('操作成功')
          fetchList()
        } catch (err) {
          ElMessage.error('操作失败')
        }
      })
      .catch(() => {})
  } else {
    currentWorkerId.value = row.id
    auditReason.value = ''
    auditDialogVisible.value = true
  }
}

const submitReject = async () => {
  if (!auditReason.value) {
    ElMessage.warning('请输入驳回原因')
    return
  }
  try {
    await axios.post('/worker/audit', {
      id: currentWorkerId.value,
      pass: false,
      reason: auditReason.value
    })
    ElMessage.success('操作成功')
    auditDialogVisible.value = false
    fetchList()
  } catch (err) {
    ElMessage.error('操作失败')
  }
}

const getStatusType = (status) => {
  const map = {
    AUDITING: 'warning',
    AUTHED: 'success',
    REJECTED: 'danger',
    UNAUTH: 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    AUDITING: '审核中',
    AUTHED: '已认证',
    REJECTED: '已驳回',
    UNAUTH: '未认证'
  }
  return map[status] || status
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.text-gray {
  color: #909399;
}
</style>