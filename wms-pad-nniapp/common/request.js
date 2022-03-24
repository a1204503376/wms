// 此vm参数为页面的实例，可以通过它引用vuex中的变量
module.exports = (vm) => {
    // 初始化请求配置
    uni.$u.http.setConfig((config) => {
        /* config 为默认全局配置*/
        //config.baseURL = 'https://wmse.elinkpoc.spic.com.cn/wmsmb'; //正式环境
		//config.baseUrl = 'https://wmse.elinkpoc.spic.com.cn/wmsmbtest';   //正式测试环境	
		//config.baseURL = 'http://localhost:8088'; //本地环境
		config.baseURL = 'http://114.215.69.116:9098';  //公司云环境
		config.header = {
			'content-type': 'application/json',
			'x-requested-with': 'XMLHttpRequest',
			'User-Type': 'pda',
		};
        return config
    })
	
	// 请求拦截
	uni.$u.http.interceptors.request.use((config) => { // 可使用async await 做异步操作
	    // 初始化请求拦截器时，会执行此方法，此时data为undefined，赋予默认{}
	    config.data = config.data || {}
		// 根据custom参数中配置的是否需要token，添加对应的请求头
		// if(config?.custom?.auth) {
		// 	// 可以在此通过vm引用vuex中的变量，具体值在vm.$store.state中
		// 	config.header.token = vm.$store.state.userInfo.token
		// }
		config.header['Authorization'] = 'Basic c3dvcmQ6c3dvcmRfc2VjcmV0'
		config.header['Blade-Auth'] = 'bearer ' + vm.vuex_token
	    return config 
	}, config => { // 可使用async await 做异步操作
	    return Promise.reject(config)
	})
	
	// 响应拦截
	uni.$u.http.interceptors.response.use((response) => { /* 对响应成功做点什么 可使用async await 做异步操作*/
		const data = response.data
		console.log(response)
		// 自定义参数
		const custom = response.config?.custom
		if (response.statusCode !== 200) { 
			// 如果没有显式定义custom的toast参数为false的话，默认对报错进行toast弹出提示
			if (custom.toast !== false) {
				uni.$u.toast(data.message)
			}

		}
		return data === undefined ? {} : data
	}, (response) => { 
		// 对响应错误做点什么 （statusCode !== 200）
		return Promise.reject(response)
	})
}