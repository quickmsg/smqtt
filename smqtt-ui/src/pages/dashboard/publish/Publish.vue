<template>
    <div>
      <div style="margin-top: 15px;">
        <a-form-model
            ref="ruleForm"
            :model="params"
            :rules="rules"
            layout="horizontal" v-bind="{
            labelCol: { span: 3 },
            wrapperCol: { span: 21 },
          }" style="width: 80%;margin: 50px auto">
          <a-form-model-item label="Topic" prop="topic">
            <a-input v-model="params.topic" placeholder="请输入Topic" />
          </a-form-model-item>
          <a-form-model-item label="消息" prop="message">
            <a-textarea v-model="params.message" placeholder="请输入消息" :auto-size="{ minRows: 4 }" />
          </a-form-model-item>
          <a-form-model-item label="服务等级" prop="qos">
            <a-select v-model="params.qos" style="width: 100px">
              <a-select-option value="0">
                0
              </a-select-option>
              <a-select-option value="1">
                1
              </a-select-option>
              <a-select-option value="2">
                2
              </a-select-option>
            </a-select>
          </a-form-model-item>
          <a-form-model-item label="是否保留" prop="retain">
            <a-switch v-model="params.retain" checked-children="保留" un-checked-children="不保留"/>
          </a-form-model-item>
          <a-form-model-item>
            <a-button style="margin-left: 14%" type="primary" @click="publish" icon="thunderbolt">
              发送
            </a-button>

            <a-button style="margin-left: 20px" @click="reset" icon="redo">
              重置
            </a-button>
          </a-form-model-item>
        </a-form-model>
      </div>
    </div>
</template>

<script>
import {publish} from '@/services/smqtt'
export default {
    name: "Publish",
    data() {
        return {
          params:{
            topic:"",
            qos:0,
            retain:true,
            message:""
          },
          rules:{
            topic: [{ required: true, message: '必填项，请输入topic', trigger: 'blur' }],
            message: [{ required: true, message: '必填项，请输入消息', trigger: 'blur' }],
          }
        }
    },
    methods: {
      publish(){
        this.$refs.ruleForm.validate(valid => {
          if (valid) {
            publish(this.params).then(()=>{
              this.$message.success("消息推送成功")
            }).catch(()=>{
              this.$message.error("消息推送失败，请重试")
            })
          } else {
            return false;
          }
        })
      },
      reset(){
        this.$refs.ruleForm.resetFields();
      }
    }
}
</script>