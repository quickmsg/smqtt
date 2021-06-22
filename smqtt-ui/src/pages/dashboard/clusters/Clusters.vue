<template>
  <standard-table
      :columns="columns"
      :dataSource="dataSource"
      :row-key="(r,i)=>{i.toString()}"
  >
  </standard-table>
</template>

<script>
import {clusters} from '@/services/smqtt'
import StandardTable from '@/components/table/StandardTable'
const columns = [
  {
    title: 'ID',
    width:'100px',
    customRender: (text, record, index) => index + 1
  },
  {
    title: 'Node名称',
    dataIndex: 'alias',
  },
  {
    title: '主机IP',
    dataIndex: 'host',
  },
  {
    title: '端口',
    dataIndex: 'port',
  },
  {
    title: '命名空间',
    dataIndex: 'namespace',
  },
]
export default {
  name: "Clusters",
  components: {StandardTable},
  data(){
    return{
      columns: columns,
      dataSource: []
    }
  },
  mounted() {
    this.getClusters()
  },
  methods:{
    getClusters(){
      clusters().then(res=>{
        this.dataSource=res.data
      })
    }
  }
}
</script>