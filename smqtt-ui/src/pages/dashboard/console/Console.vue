<template>
    <div>
        <div style="margin-top: 15px; display: flex; justify-content: space-between">
            <div style="font-size: 25px">控制台</div>
            <div>
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
                    <a-select-option v-for="it in optionsList.slice(0,10)" :key="it.host">
                        {{ it.alias }}
                    </a-select-option>
                </a-select>
            </div>
        </div>
        <div style="margin-top: 30px;font-size: medium">系统信息</div>
        <div style="display: flex">
            <a-card title="JVM 信息" size="small" :bordered="false" style="margin-top: 15px;width:50%">
                <span slot="extra"><img src="@/assets/img/jvm.png" width="32"/></span>
                <a-row align="top" justify="center" type="flex" v-if="Object.keys(jvmInfo).length>0">
                    <a-col :span="12">
                        <p class="height-100">
                            smqtt: {{ jvmInfo.smqtt }}
                        </p>
                        <p class="height-100">
                            jdk_home: {{ jvmInfo.jdk_home }}
                        </p>
                        <p class="height-100">
                            jdk_version: {{ jvmInfo.jdk_version }}
                        </p>
                        <p class="height-100">
                            start_time: {{ jvmInfo.start_time }}
                        </p>
                        <p class="height-100">
                            thread.count: {{ jvmInfo["thread.count"] }}
                        </p>
                    </a-col>
                    <a-col :span="12">
                        <p class="height-100">
                            heap-max: {{ jvmInfo["heap-max"] }}
                        </p>
                        <p class="height-100">
                            heap-init: {{ jvmInfo["heap-init"] }}
                        </p>
                        <p class="height-100">
                            heap-used: {{ jvmInfo["heap-used"] }}
                        </p>
                        <p class="height-100">
                            heap-commit: {{ jvmInfo["heap-commit"]}}
                        </p>
                        <p class="height-100">
                            no_heap-max: {{ jvmInfo["no_heap-max"] }}
                        </p>
                        <p class="height-100">
                            no_heap-init: {{ jvmInfo["no_heap-init"] }}
                        </p>
                        <p class="height-100">
                            no_heap-used: {{ jvmInfo["no_heap-used"] }}
                        </p>
                        <p class="height-100">
                            no_heap-commit: {{ jvmInfo["no_heap-commit"] }}
                        </p>
                    </a-col>
                </a-row>
            </a-card>
            <a-card title="CPU 信息" size="small" :bordered="false" style="margin-top: 15px;margin-left:10px;width: 50%">
                <span slot="extra"><img width="32" src="@/assets/img/cpu.png" /></span>
                <p v-for="(v,k) in cpuInfo" :key="k">{{ k }} : {{ v }}</p>
            </a-card>
        </div>

        <div style="margin-top: 30px;font-size: medium">节点信息</div>
        <standard-table
            :columns="columns"
            :dataSource="nodeInfo"
            :row-key="(r,i)=>{i.toString()}"
            :pagination=false
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
import {clusters} from '@/services/smqtt'
import StandardTable from '@/components/table/StandardTable'
import axios from "axios";

const columns = [
    {
        title: 'ID',
        width: '100px',
        customRender: (text, record, index) => index + 1
    },
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
        title: 'ID',
        width: '100px',
        customRender: (text, record, index) => index + 1
    },
    {
        title: '连接数',
        dataIndex: 'connect_size',
    },
    {
        title: '写字节数',
        dataIndex: 'write_size',
    },
    {
        title: '写字节数/时',
        dataIndex: 'write_hour_size',
    },
    {
        title: '读字节数',
        dataIndex: 'read_size',
    },
    {
        title: '读字节数/时',
        dataIndex: 'read_hour_size',
    }

]
export default {
    name: "Console",
    components: {StandardTable},
    data() {
        return {
            columns: columns,
            counterColumns: counterColumns,
            dataSource: [],
            optionsList: [],
            defaultNode: undefined,
            nodeInfo:[],
            jvmInfo:{},
            cpuInfo:{},
            counterInfo:[]
        }
    },
    mounted() {
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
                    this.getConsoleInfo(newVal)
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
                this.defaultNode = this.optionsList.length===0 ? undefined : this.optionsList[0]['host']
                this.nodeInfo = res.data.slice(0,1) || []

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
                    item.host===this.defaultNode?this.nodeInfo = new Array(item):null
                })
            }
        },
        getConsoleInfo(host){
            // console.log(host)
            let jvm = `http://${host}:60000/smqtt/monitor/jvm`
            let cpu = `http://${host}:60000/smqtt/monitor/cpu`
            let counter = `http://${host}:60000/smqtt/monitor/counter`
            let options = {
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            }
            axios.get(jvm, options).then(res => {
                this.jvmInfo = res.data
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