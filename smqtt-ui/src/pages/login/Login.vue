<template>
  <common-layout>
    <div class="top">
      <div class="header">
        <img alt="logo" class="logo" src="@/assets/img/logo.png"/>
        <span class="title">{{ systemName }}</span>
      </div>
      <div class="desc">smqtt 是一款Java实现的高性能可扩展的MQTT broker</div>
    </div>
    <div class="login">
      <a-form :form="form" @submit="onSubmit">
        <a-tabs :tabBarStyle="{textAlign: 'center'}" size="large" style="padding: 0 2px;">
          <a-tab-pane key="1" tab="账户密码登录">
            <a-alert v-show="error" :closable="true" :message="error" showIcon style="margin-bottom: 24px;"
                     type="error"/>
            <a-form-item>
              <a-input
                  v-decorator="['name', {rules: [{ required: true, message: '请输入账户名', whitespace: true}]}]"
                  autocomplete="autocomplete"
                  placeholder="smqtt"
                  size="large"
              >
                <a-icon slot="prefix" type="user"/>
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-input
                  v-decorator="['password', {rules: [{ required: true, message: '请输入密码', whitespace: true}]}]"
                  autocomplete="autocomplete"
                  placeholder="smqtt"
                  size="large"
                  type="password"
              >
                <a-icon slot="prefix" type="lock"/>
              </a-input>
            </a-form-item>
          </a-tab-pane>
          <!-- <a-tab-pane tab="手机号登录" key="2">
            <a-form-item>
              <a-input size="large" placeholder="mobile number" >
                <a-icon slot="prefix" type="mobile" />
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-row :gutter="8" style="margin: 0 -4px">
                <a-col :span="16">
                  <a-input size="large" placeholder="captcha">
                    <a-icon slot="prefix" type="mail" />
                  </a-input>
                </a-col>
                <a-col :span="8" style="padding-left: 4px">
                  <a-button style="width: 100%" class="captcha-button" size="large">获取验证码</a-button>
                </a-col>
              </a-row>
            </a-form-item>
          </a-tab-pane> -->
        </a-tabs>
        <div>
          <a-checkbox :checked="true">自动登录</a-checkbox>
          <a style="float: right">忘记密码</a>
        </div>
        <a-form-item>
          <a-button :loading="logging" htmlType="submit" size="large" style="width: 100%;margin-top: 24px"
                    type="primary">登录
          </a-button>
        </a-form-item>
        <!-- <div>
          其他登录方式
          <a-icon class="icon" type="alipay-circle" />
          <a-icon class="icon" type="taobao-circle" />
          <a-icon class="icon" type="weibo-circle" />
          <router-link style="float: right" to="/dashboard/workplace" >注册账户</router-link>
        </div> -->
      </a-form>
    </div>
  </common-layout>
</template>

<script>
import CommonLayout from '@/layouts/CommonLayout'
import {login} from '@/services/user'
import {setAuthorization} from '@/utils/request'
// import {loadRoutes} from '@/utils/routerUtil'
import {mapMutations} from 'vuex'

export default {
  name: 'Login',
  components: {CommonLayout},
  data() {
    return {
      logging: false,
      error: '',
      form: this.$form.createForm(this),
      userName:'admin'
    }
  },
  computed: {
    systemName() {
      return this.$store.state.setting.systemName
    }
  },
  methods: {
    ...mapMutations('account', ['setUser', 'setPermissions', 'setRoles']),
    onSubmit(e) {
      e.preventDefault()
      this.form.validateFields((err) => {
        if (!err) {
          this.logging = true
          const name = this.form.getFieldValue('name')
          const password = this.form.getFieldValue('password')
          this.userName=name
          login(name, password).then(this.afterLogin)
        }
      })
    },
    afterLogin(res) {
      this.logging = false
      const loginRes = res.data
      this.setUser({
        name: this.userName,
        avatar: '',
        address: '',
        position: ''
      })
      this.setPermissions([{id: 'queryForm', operation: ['add', 'edit']}])
      this.setRoles([{id: 'admin', operation: ['add', 'edit', 'delete']}])
      setAuthorization({token: loginRes.data.access_token, expireAt: new Date(loginRes.data.expires_in)})
      this.$router.push('/dashboard/connections')
      this.$message.success(`${this.userName}，欢迎回来`, 3)
      // // 获取路由配置
      // getRoutesConfig().then(result => {
      //   const routesConfig = result.data.data
      //   loadRoutes(routesConfig)
      //   this.$router.push('/dashboard/query')
      //   this.$message.success(loginRes.message, 3)
      // })
    }
  }
}
</script>

<style lang="less" scoped>
.common-layout {
  .top {
    text-align: center;

    .header {
      height: 44px;
      line-height: 44px;

      a {
        text-decoration: none;
      }

      .logo {
        height: 44px;
        vertical-align: top;
        margin-right: 16px;
      }

      .title {
        font-size: 33px;
        color: @title-color;
        font-family: 'Myriad Pro', 'Helvetica Neue', Arial, Helvetica, sans-serif;
        font-weight: 600;
        position: relative;
        top: 2px;
      }
    }

    .desc {
      font-size: 14px;
      color: @text-color-second;
      margin-top: 12px;
      margin-bottom: 40px;
    }
  }

  .login {
    width: 368px;
    margin: 0 auto;
    @media screen and (max-width: 576px) {
      width: 95%;
    }
    @media screen and (max-width: 320px) {
      .captcha-button {
        font-size: 14px;
      }
    }

    .icon {
      font-size: 24px;
      color: @text-color-second;
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: @primary-color;
      }
    }
  }
}
</style>
