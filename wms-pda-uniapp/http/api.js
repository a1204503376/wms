import {
	clientId,
	clientSecret,
	successCode
} from '@/common/setting'
import {
	options
} from '@/http/config.js';
import {
	Base64
} from '@/utils/base64.js';
import Request from '@/utils/luch-request/index.js';
const http = new Request(options);
http.interceptors.request.use((config) => { // 可使用async await 做异步操作
	config.header['Access-Control-Allow-Origin']='*'
	// 假设有token值需要在头部需要携带
	let accessToken = uni.getStorageSync('accessToken');
	if (accessToken) {
		config.header['Blade-Auth'] = 'bearer ' + accessToken;
		
	}
	if (config.text === true) {
		config.headers["Content-Type"] = "text/plain";
	}
	// 客户端认证参数
	config.header['Authorization'] = 'Basic ' + Base64.encode(clientId + ':' + clientSecret);

	// 额外参数
	// config.data = config.data || {};
	// config.data.pf = uni.getSystemInfoSync().platform;
	// config.data.sys = uni.getSystemInfoSync().system;

	// 演示custom 用处
	// if (config.custom.auth) {
	//   config.header.token = 'token'
	// }
	// if (config.custom.loading) {
	//  uni.showLoading()
	// }
	/**
	 /* 演示
	 if (!token) { // 如果token不存在，return Promise.reject(config) 会取消本次请求
		return Promise.reject(config)
	  }
	 **/
	return config
}, config => { // 可使用async await 做异步操作
	return Promise.reject(config)
})
http.interceptors.response.use((response) => {
	// 对错误信息进行统一拦截
	const statusCode = response.data.code || response.statusCode;
	if (statusCode !== successCode || response.data.error_code) {
		const errMsg = response.data.msg || response.data.error_description || '未知错误,可能是网络连接失败导致,来自api拦截器';
		uni.$u.toast(errMsg);
		return Promise.reject(response);
	}

	return response.data;
}, (response) => { // statusCode !== 200 时候的错误处理
	if(response.statusCode ===401){
		uni.$u.toast('登录已过期，需要重新登录');
		 setTimeout(function() {
		     uni.$u.func.route('/pages/login/login')    
		     }, 1000);
		return
	}


	let errMsg = response.data?.msg 
	|| response.data?.error_description 
	|| response.errMsg
	|| '未知错误,可能是网络连接失败导致,来自api拦截器';
	uni.$u.toast(errMsg);
	return Promise.reject(response);
})
export default http;
