<template>
    <div>
        <div style="margin-top: 15px; display: flex; justify-content: space-between">
            <div style="display: flex; flex-wrap: wrap">
                <div style="margin-top: 5px"><img src="@/assets/img/cluster.png" width="30"/></div>

                <div style="font-size: 25px;margin-left: 10px;" v-if="isCluster">控制台 [ 集群 ] </div>
                <div style="font-size: 25px;margin-left: 10px;" v-else>控制台 [ 单机 ] </div>
            </div>


            <div v-if="isCluster">
                <a-select
                    allowClear
                    show-search
                    placeholder="请选择节点"
                    style="width: 200px"
                    v-model="defaultNode"
                    :filter-option="false"
                    @search="filterOptions"
                    @change="handleChange"
                    @focus="handleFocus"
                >
                    <a-select-option v-for="it in optionsList.slice(0,10)" :key="it.alias">
                        {{ it.alias }}
                    </a-select-option>
                </a-select>
            </div>
        </div>
        <div style="margin-top: 30px;font-size: medium">系统信息</div>
        <div style="display: flex">
            <a-card size="small" :bordered="false" style="margin-top: 15px;width:50%;height: 100%">
                <div slot="title">SMQTT 信息（Ver {{jvmInfo["smqtt"] || "-"}}）</div>
                <span slot="extra"><img src="@/assets/img/jvm.png" width="32"/></span>
              <a-row style="height: 50%" v-if="Object.keys(jvmInfo).length>0">
                <a-col :span="12">
                  <p >
                    <img width="40" src="@/assets/img/version.png" />
                    JDK 版本： {{ jvmInfo.jdk_version }}
                  </p>

                </a-col>
                <a-col :span="12">
                  <p>
                    <img width="40" src="@/assets/img/path.png" />
                    JDK 路径： {{ jvmInfo.jdk_home }}
                  </p>
                </a-col>
              </a-row>
              <a-row v-if="Object.keys(cpuInfo).length>0">
                <a-col :span="12" >
                  <p >
                    <img width="40" src="@/assets/img/starttime.png" />
                    开始时间： {{ jvmInfo.start_time }}
                  </p>
                </a-col>
                <a-col :span="12">
                  <p >
                    <img width="40" src="@/assets/img/threadcount.png" />
                    线程数： {{ jvmInfo["thread.count"] }}
                  </p>
                </a-col>
              </a-row>
            </a-card>
            <a-card size="small" :bordered="false" style="margin-top: 15px;margin-left:10px;width: 50%">
                <div slot="title">CPU 信息（{{cpuInfo["cpuNum"] || "-"}} 核）</div>
                <span slot="extra"><img width="32" src="@/assets/img/cpu.png" /></span>
                <a-row style="height: 50%" v-if="Object.keys(cpuInfo).length>0">
                    <a-col :span="12">
                        <p >
                            <img width="40" src="@/assets/img/csys.png" />
                            系统使用率： {{ cpuInfo["cSys"] }}
                        </p>

                    </a-col>
                    <a-col :span="12">
                        <p>
                            <img width="40" src="@/assets/img/user.png" />
                            用户使用率： {{ cpuInfo["user"] }}
                        </p>
                    </a-col>
                </a-row>
                <a-row v-if="Object.keys(cpuInfo).length>0">
                  <a-col :span="12" >
                    <p >
                      <img width="40" src="@/assets/img/idle.png" />
                      当前使用率： {{ cpuInfo["idle"] }}
                    </p>
                  </a-col>
                  <a-col :span="12">
                    <p >
                      <img width="40" src="@/assets/img/iowait.png" />
                      当前等待率： {{ cpuInfo["iowait"] }}
                    </p>
                  </a-col>
                </a-row>
            </a-card>
        </div>

        <standard-table
            :columns="heapColumns"
            :dataSource="heapInfo"
            :row-key="(r,i)=>{i.toString()}"
            :pagination=false
        >
        </standard-table>

        <div style="margin-top: 30px;font-size: medium" v-if="nodeInfo.length>0">节点信息</div>
        <standard-table
            :columns="columns"
            :dataSource="nodeInfo"
            :row-key="(r,i)=>{i.toString()}"
            :pagination=false
            v-if="nodeInfo.length>0"
        >
        </standard-table>

        <div style="margin-top: 30px;font-size: medium">运行统计</div>
        <standard-table
            :columns="counterColumns"
            :dataSource="counterInfo"
            :row-key="(r,i)=>{i.toString()}"
            :pagination=false
        >
        </standard-table>
    </div>
</template>

<script>
    import {clusters, isCluster} from '@/services/smqtt'
    import StandardTable from '@/components/table/StandardTable'
    import axios from "axios";

    const columns = [
    {
        title: 'Node名称',
        dataIndex: 'alias',
        key: 'alias',
    },
    {
        title: '主机IP',
        dataIndex: 'host',
        key:'host'
    },
    {
        title: '端口',
        dataIndex: 'port',
        key:'port'
    },
    {
        title: '命名空间',
        dataIndex: 'namespace',
        key: 'namespace',
    },
]
const counterColumns = [
    {
        title: '连接数',
        dataIndex: 'connect_size',
    },
    {
        title: '写字节数',
        dataIndex: 'write_size',
    },
    {
        title: '写字节数/秒',
        dataIndex: 'write_second_size',
    },
    {
        title: '读字节数',
        dataIndex: 'read_size',
    },
    {
        title: '读字节数/秒',
        dataIndex: 'read_second_size',
    }

]
const heapColumns = [
  {
    title: '最大可用堆内存',
    dataIndex: 'heap-max',
    key: 'heap-max',
  },
  {
    title: '初始化堆内存',
    dataIndex: 'heap-init',
    key: 'heap-init',
  },
  {
    title: '已用堆内存',
    dataIndex: 'heap-used',
    key: 'heap-used',
  },
  {
    title: '已提交堆内存',
    dataIndex: 'heap-commit',
    key: 'heap-commit',
  },
  {
    title: '最大可用非堆内存',
    dataIndex: 'no_heap-max',
    key: 'no_heap-max',
  },
  {
    title: '初始化非堆内存',
    dataIndex: 'no_heap-init',
    key: 'no_heap-init',
  },
  {
    title: '已用非堆内存',
    dataIndex: 'no_heap-used',
    key: 'no_heap-used',
  },
  {
    title: '已提交非堆内存',
    dataIndex: 'no_heap-commit',
    key: 'no_heap-commit',
  },
]
export default {
    name: "Console",
    components: {StandardTable},
    data() {
        return {
            columns: columns,
            counterColumns: counterColumns,
            heapColumns:heapColumns,
            isCluster:false,
            dataSource: [],
            optionsList: [],
            defaultNode: undefined,
            nodeInfo:[],
            jvmInfo:{},
            cpuInfo:{},
            counterInfo:[],
            heapInfo:[],
        }
    },
    mounted() {
        isCluster().then(res=>{
            this.isCluster = res.data
        })
        this.getClusters()
    },
    beforeDestroy() {
        if (this.timer) {
            clearTimeout(this.timer)
        }
    },
    watch: {
        defaultNode: {
            handler(newVal) {
                if(!newVal){
                    console.log("pass")
                }else {
                    this.getConsoleInfo()
                }
            },
            deep: true,
        },
    },
    methods: {
        getClusters() {
            clusters().then(res => {
                // 存储原始数据
                this.dataSource = res.data
                // 设置默认的展示节点数据
                this.optionsList =[...res.data]
                this.defaultNode = this.optionsList.length===0 ? undefined : this.optionsList[0]['alias']
                this.nodeInfo = res.data.slice(0,1) || []
                //如果单机nodeInfo不发生变化，watch不会调用接口请求
                if(this.nodeInfo.length===0){
                  this.getConsoleInfo()
                }
            })
        },
        filterOptions(input) {
            this.optionsList = []
            this.dataSource.map(item => {
                item.alias.toLowerCase().indexOf(input.toLowerCase()) >= 0 ? this.optionsList.push(item): null
            })
        },
        handleFocus(){
            this.optionsList = this.dataSource
        },
        handleChange(){
            if(!this.defaultNode){
                console.log("pass")
            }else {
                this.dataSource.map(item=>{
                    item.alias===this.defaultNode?this.nodeInfo = new Array(item):null
                })
            }
        },
        getConsoleInfo(){
            let host = window.location.host.split(':')[0]
            if(this.isCluster){
               host = this.nodeInfo[0]['host']
            }
            let jvm = `http://${host}:60000/smqtt/monitor/jvm`
            let cpu = `http://${host}:60000/smqtt/monitor/cpu`
            let counter = `http://${host}:60000/smqtt/monitor/counter`
            let options = {
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
            }
            axios.get(jvm, options).then(res => {
                this.jvmInfo = res.data
                this.heapInfo = [res.data]
            })
            axios.get(cpu,options).then(res=>{
                this.cpuInfo = res.data
            })
            axios.get(counter,options).then(res=>{
                this.counterInfo = new Array(res.data)
            })
            if (this.timer) {
                clearTimeout(this.timer)
            }
            this.timer = setTimeout(() => {
                this.getConsoleInfo(host)
            }, 3000)
        }
    }
}
</script>