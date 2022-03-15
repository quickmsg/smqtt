import {CLUSTERS, CONNECTIONS, SUBSCRIBES, ISCLUESTER, PUBLISH,ACLACTION} from '@/services/api'
import {request, METHOD} from '@/utils/request'

export function addPolicyAction(params) {
    return request(ACLACTION+'policy/add', METHOD.POST, params)
}


export function deletePolicyAction(params) {
    return request(ACLACTION+'policy/delete', METHOD.POST, params)
}


export async function  queryPolicyAction(params) {
    return request(ACLACTION+'policy/query', METHOD.POST, params)
}

/**
 * 获取当前连接信息
 */
export async function connections() {
    return request(CONNECTIONS, METHOD.POST, {})

}
/**
 * 获取当前集群信息
 */
export async function clusters() {
    return request(CLUSTERS, METHOD.POST, {})
}

/**
 * 获取当前订阅信息
 */
export async function subscribes() {
    return request(SUBSCRIBES, METHOD.POST, {})
}

/**
 * 是否是集群
 */
export async function isCluster() {
    return request(ISCLUESTER, METHOD.GET, {})
}

/**
 * 推送mqtt消息
 */
export async function publish(params){
    return request(PUBLISH,METHOD.POST,params)
}
