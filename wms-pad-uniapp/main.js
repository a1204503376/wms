import App from './App'
import Vue from 'vue'

import uView from '@/uni_modules/uview-ui'
Vue.use(uView)

Vue.config.productionTip = false
App.mpType = 'app'


// 引入vuex
const vuexStore = require("@/store/$u.mixin.js");
Vue.mixin(vuexStore);
const app = new Vue({
    ...App
})
app.$mount()

// 接口集中管理
import httpInstall from '@/http/install.js'
Vue.use(httpInstall, app)


// 公共函数
import globalFunc from '@/utils/func.js'
Vue.use(globalFunc, app);
