// 全局公共方法
const install = (Vue, vm) => {

	// 登录操作
	const login = (userInfo) => {
		vm.$u.vuex('userInfo', userInfo)
		vm.$u.vuex('accessToken', userInfo.accessToken)
		vm.$u.vuex('isLogin', true)
		uni.redirectTo({
			url: '/pages/menu/home/home'
		})
	}

	// 退出登录
	const logout = () => {
		vm.$u.vuex('userInfo', {
			avatar: '',
			nick_name: '游客',
			tenant_id: '暂无'
		})
		vm.$u.vuex('accessToken', '')
		vm.$u.vuex('isLogin', false)
		uni.redirectTo({
			url: '/pages/login/login'
		})
	}

	// 检查登录状态
	const checkLogin = (e = {}) => {
		if (!vm.isLogin) {
			uni.navigateTo({
				url: '/pages/login/login'
			})
			return false
		}
		return true
	}

	// 跳转路由前检查登录状态
	const route = (url) => {
		if (!vm.isLogin) {
			uni.showToast({
				title: '请先登录',
				icon: 'none'
			})
			setTimeout(() => {
				uni.navigateTo({
					url: '/pages/login/login'
				})
			}, 500)
			return false
		}
		uni.navigateTo({
			url: url
		})
	}

	// URL参数转对象
	const paramsToObj = (url) => {
		if (url.indexOf('?') != -1) {
			let arr = url.split('?')[1]
		}
		let arr = url.split('&')
		let obj = {}
		for (let i of arr) {
			obj[i.split('=')[0]] = i.split('=')[1]
		}
		return obj
	}

	// 刷新当前页面
	const refreshPage = () => {
		const pages = getCurrentPages()
		const currentPage = pages[pages.length - 1]
		const path = '/' + currentPage.route + vm.$u.queryParams(currentPage.options)
		if (vm.$u.test.contains(currentPage.route, 'tabbar')) {
			uni.reLaunch({
				url: path,
				fail: (err) => {
					console.log(err)
				}
			})
		} else {
			uni.redirectTo({
				url: path,
				fail: (err) => {
					console.log(err)
				}
			})
		}
	}

	// 提示
	const showToast = (data = {}) => {
		if (typeof data == 'string') {
			uni.showToast({
				title: data,
				icon: 'none'
			})
		} else {
			uni.showToast({
				title: data.title,
				icon: data.icon || 'none',
				image: data.image || '',
				mask: data.mask || false,
				position: data.position || 'center',
				duration: data.duration || 1500,
				success: () => {
					setTimeout(() => {
						if (data.back) return uni.navigateBack()
						data.success && data.success()
					}, data.duration || 1500)
				}
			})
		}
	}
	
	// 注册扫码组件
	const registerScanner = (callback) => {
		let subNVue = uni.getSubNVueById('honeywellScannerComponent');
		subNVue.hide();
		uni.$on('on-scanner-data', (data) => {
			callback(data)
		})
	}
	
	// 移除扫码组件注册
	const unRegisterScanner = () => {
		uni.$off('on-scanner-data')
	}

	// 将定义的方法挂载，使用this.$u.func.xx调用
	Vue.prototype.$u.func = {
		login,
		logout,
		route,
		checkLogin,
		paramsToObj,
		refreshPage,
		showToast,
		registerScanner,
		unRegisterScanner
	}
}
export default {
	install
}
