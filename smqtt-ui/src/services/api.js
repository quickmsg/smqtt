//跨域代理前缀
const API_PROXY_PREFIX='/api'
const BASE_URL = process.env.NODE_ENV === 'production' ? process.env.VUE_APP_API_BASE_URL : API_PROXY_PREFIX
const IP = window.location.host.split(':')[0]
module.exports = {
  LOGIN: `http://${IP}:60000/auth/login`,
  CLUSTERS: `http://${IP}:60000/smqtt/cluster`,
  CONNECTIONS: `http://${IP}:60000/smqtt/connection`,
  SUBSCRIBES:`http://${IP}:60000/smqtt/subscribe`,
  ISCLUESTER:`http://${IP}:60000/smqtt/is/cluster`,
  PUBLISH:`http://${IP}:60000/smqtt/publish`,
  ROUTES: `${BASE_URL}/routes`,
  ACLACTION: `http://${IP}:60000/smqtt/acl/`
}
