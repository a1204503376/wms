import App from './App'
import Vue from 'vue'
import store from '@/store';
import uView from '@/uni_modules/uview-ui'
Vue.use(uView)

Vue.config.productionTip = false
App.mpType = 'app'

// 引入全局uView
Vue.use(uView);

// 引入vuex
const vuexStore = require("@/store/$u.mixin.js");
Vue.mixin(vuexStore);
const app = new Vue({
	store,
	...App
})

// 公共函数
import globalFunc from '@/utils/func.js'
Vue.use(globalFunc, app);

app.$mount()
