<template>
  <div style="margin-top: 20px">
    <a-form
        layout="inline"
        class="antAdvancedSearchForm"
    >
      <a-form-item label="规则" style="size: 20px">
        <a-input v-model="params.subject" style="width: 100px" placeholder='请输入过滤规则'/>
      </a-form-item>
      <a-form-item label="topic" style="size: 20px">
        <a-input v-model="params.source" style="width: 100px" placeholder='请输入topic名称'/>
      </a-form-item>
      <a-form-item label="类型" style="size: 20px">
        <a-select v-model="params.action" default-value="ALL" style="width: 100px" @change="queryActionData">
          <a-select-option value="ALL">
            ALL
          </a-select-option>
          <a-select-option value="SUBSCRIBE">
            SUBSCRIBE
          </a-select-option>
          <a-select-option value="PUBLISH">
            PUBLISH
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="策略" style="size: 20px">
        <a-select v-model="params.aclType" default-value="ALLOW" style="width: 100px" @change="queryActionData">
          <a-select-option value="ALL">
            ALL
          </a-select-option>
          <a-select-option value="DENY">
            DENY
          </a-select-option>
          <a-select-option value="ALLOW">
            ALLOW
          </a-select-option>
        </a-select>
        <a-button style="width: 80px;margin-left: 20px" @click="showModal">
          新增
        </a-button>
        <a-button style="width: 80px;margin-left: 20px" @click="queryActionData">查询</a-button>
        <a-button style="width: 80px;margin-left: 20px" @click="reset">重置</a-button>
        <a-button style="width: 80px;margin-left: 20px" @click="deleteActionData">删除</a-button>

      </a-form-item>


    </a-form>



    <a-modal
        title="新增访问控制"
        :visible="visible"
        :confirm-loading="confirmLoading"
        @ok="handleOk"
        @cancel="handleCancel"
    >
      <a-form :model="form" :labelCol="{ span: 4 }" :wrapperCol="{ span: 20 }">
        <a-form-item label="规则">
          <a-input
              placeholder="请输入规则"
              v-model="form.subject"
              v-decorator="['subject', {
            rules: [
              { required: true, message: '请输入Subject' },
              { max: 64, message: '设备ID不超过64个字符' },
              { pattern: new RegExp(/^[0-9a-zA-Z_\-]+$/, 'g'), message: '产品ID只能由数字、字母、下划线、中划线组成' }
            ]
          }]"
          />
        </a-form-item>
        <a-form-item label="topic">
          <a-input
              placeholder="请输入topic名称"
              v-model="form.source"
              v-decorator="['source', {
            rules: [
              { required: true, message: '请输入Source名称' },
              { max: 200, message: 'Source名称不超过200个字符' }
            ]
          }]"
          />
        </a-form-item>
        <a-form-item
            label="类型"
            v-decorator="['action', {
          rules: [
            { required: true }
          ]
        }]"
        >
          <a-select  v-model="form.action"
                                  style="width: 100%" default-value="PUBLISH" placeholder="请选择类型">
            <a-select-opt-group>
              <a-select-option value="SUBSCRIBE">
                SUBSCRIBE
              </a-select-option>
              <a-select-option value="PUBLISH">
                PUBLISH
              </a-select-option>
            </a-select-opt-group>
          </a-select>
        </a-form-item>
        <a-form-item
            label="限制策略"
            v-decorator="['aclType', {
          rules: [
            { required: true }
          ]
        }]"
        >
          <a-select style="width: 100%"  v-model="form.aclType"  placeholder="请选择类型">
            <a-select-opt-group>
              <a-select-option value="DENY">
                DENY
              </a-select-option>
              <a-select-option value="ALLOW">
                ALLOW
              </a-select-option>
            </a-select-opt-group>
          </a-select>
          </a-form-item>
      </a-form>
    </a-modal>
    <div>
    </div>
    <a-table
        :pagination="pagination"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :columns="columns"
        :data-source="dataSource"
        @change="handleTableChange"
    />
  </div>

</template>

<script>
import {queryPolicyAction, deletePolicyAction, addPolicyAction} from '@/services/smqtt'


const columns = [
  {
    title: 'ID',
    width: '100px',
    customRender: (text, record, index) => index + 1
  },
  {
    title: '规则',
    dataIndex: "subject",
  },
  {
    title: 'topic',
    dataIndex: "source",
  },
  {
    title: '类型',
    dataIndex: "action",
  },
  {
    title: '策略',
    dataIndex: "aclType",
  }
]
export default {
  name: "Acl",
  data() {
    return {
      params: {
        action: "ALL",
        current: 1,
        pageSize: 10,
        subject: null,
        source: null,
        aclType: "ALL"
      },

      pagination: {
        pageSize: 20, // 默认每页显示数量
        showSizeChanger: true, // 显示可改变每页数量
        pageSizeOptions: ['10', '20', '30', '40'], // 每页数量选项
        showTotal: total => `Total ${total} items`, // 显示总数
        onShowSizeChange: (page, pageSize) => {
          this.pagination.pageSize = pageSize
        }
      },
      selectedRowKeys: [],
      columns: columns,
      dataSource: null,
      visible: false,
      confirmLoading: false,
      form: {
        action: "PUBLISH",
        subject: null,
        source: null,
        aclType: "ALLOW"
      }
    }
  },
  mounted() {
    this.queryActionData()
  },
  methods: {
    reset() {
      this.params.action = "ALL"
      this.params.current = 1
      this.params.pageSize = 10
      this.params.subject = null
      this.params.source = null
      this.params.aclType = null
      this.queryActionData()

    },
    async queryActionData() {
      await queryPolicyAction(this.params).then(res => {
        this.dataSource = res.data
      })

    },
    async deleteActionData() {
      for (const key in this.selectedRowKeys) {
        let loc = Number((this.params.current - 1) * this.params.pageSize) + Number(this.selectedRowKeys[key])
        let data = this.dataSource[loc]
        await deletePolicyAction(data).then(res => {
          this.$message.info("deleted:" + res.data);
        })
      }
      await this.queryActionData()
      this.selectedRowKeys = []

    },
    onSelectChange(selectedRowKeys) {
      console.log('selectedRowKeys changed: ', selectedRowKeys);
      this.selectedRowKeys = selectedRowKeys;

    },
    showModal() {
      this.visible = true;
    },
    async handleOk() {
      this.confirmLoading = true;
      await addPolicyAction(this.form).then(res => {
        this.$message.info("add:" + res.data);
      })
      this.visible = false;
      this.confirmLoading = false
      this.form = {
        action: "PUBLISH",
        subject: null,
        source: null,
        aclType: "ALLOW"
      }
      await this.queryActionData()

    },
    handleCancel() {
      console.log('Clicked cancel button');
      this.visible = false;
    },
    handleTableChange(val) {
      console.log(val)
      const pager = {...this.pagination};
      this.params.current = val.current;  // 查看文档可知current 是改变页码数必要字段
      this.params.pageSize = val.pageSize;  // 查看文档可知pageSize是改变动态条数必要字段
      this.selectedRowKeys = [];
      this.pagination = pager;
    },
  }
}
</script>
