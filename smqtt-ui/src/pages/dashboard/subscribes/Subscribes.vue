<template>
  <div style="margin-top: 20px">
    <a-collapse v-for="(topicData,index) in dataShow" :key="index" style="margin-top: 10px" >
      <a-collapse-panel :key="index" :header="'Topic：'+ topicData.topic">
        <div>
          <a-form layout="horizontal">
            <a-row>
              <a-col :md="4" :sm="24">
                <a-input-search placeholder="输入客户端ID进行查询" enter-button @search="onSearch($event,index)" allowClear/>
              </a-col>
              <a-col :md="16" :sm="24">
              </a-col>
            </a-row>
          </a-form>
        </div>
        <standard-table
            :columns="columns"
            :dataSource="topicData.data"
            :row-key="(r,i)=>{i.toString()}"
        >
          <template slot="connection" slot-scope="{text,record}">
            <a-tag v-for="(v,k) in record.connection" :key="k" style="margin-top: 1px">{{ k }}:{{ v }}</a-tag>
          </template>
          <template slot="will" slot-scope="{text,record}">
            <a-tag v-for="(v,k) in record.will" :key="k" style="margin-top: 1px">{{ k }}:{{ v }}</a-tag>
          </template>
          <template slot="topics" slot-scope="{text,record}">
            <span v-if="record.topics.length===0"> — </span>
            <a-tag v-for="(item,index) in record.topics" v-else :key="index" style="margin-top: 1px">{{ item }}</a-tag>
          </template>
        </standard-table>
      </a-collapse-panel>
    </a-collapse>
  </div>
</template>

<script>
import {subscribes} from '@/services/smqtt'
import StandardTable from '@/components/table/StandardTable'
import moment from "moment";

const columns = [
  {
    title: 'ID',
    width: '100px',
    customRender: (text, record, index) => index + 1
  },
  {
    title:'客户端ID',
    dataIndex: 'clientIdentifier'
  },
  {
    title: '连接',
    dataIndex: 'connection',
    scopedSlots: {customRender: 'connection'},
    width: "100px"
  },
  {
    title: '在线状态',
    dataIndex: 'status',
  },
  {
    title: '激活时间',
    dataIndex: 'activeTime',
    customRender: (text, record) => moment(record.activeTime).format('YYYY-MM-DD HH:mm:ss')
  },
  {
    title: '认证时间',
    dataIndex: 'authTime',
    customRender: (text, record) => moment(record.authTime).format('YYYY-MM-DD HH:mm:ss')
  },
  {
    title: '是否持久化',
    dataIndex: 'sessionPersistent',
    customRender: (text, record) => record.sessionPersistent ? "是" : "否"
  },
  {
    title: '遗嘱消息',
    dataIndex: 'will',
    scopedSlots: {customRender: 'will'},
    width: "100px"
  },
  {
    title: '心跳时间',
    dataIndex: 'keepalive',
  },
  {
    title: '订阅Topic',
    dataIndex: 'topics',
    scopedSlots: {customRender: 'topics'},
    width: "100px"
  }
]
export default {
  name: "Subscribes",
  components: {StandardTable},
  data() {
    return {
      columns: columns,
      dataSource: [],
      dataShow:[],
    }
  },
  mounted() {
    this.getSubscribes()
  },
  methods: {
    getSubscribes() {
      subscribes().then(res => {
        for (var key in res.data) {
          this.dataSource.push({"topic": key, "data": res.data[key]})
        }
        this.dataShow=JSON.parse(JSON.stringify(this.dataSource))
      })
    },
    onSearch(val,idx){
      if(!val){
        this.dataShow[idx]['data']=JSON.parse(JSON.stringify(this.dataSource[idx]['data']))
      }else{
        let data = JSON.parse(JSON.stringify(this.dataSource[idx]['data']))
        let finalData=[]
        data.forEach((ite)=>{
            ite.clientIdentifier===val ? finalData.push(ite) : null
        })
        this.dataShow[idx]['data']=JSON.parse(JSON.stringify(finalData))
      }
    }
  }
}
</script>