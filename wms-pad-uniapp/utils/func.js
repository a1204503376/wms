import api from '@/api/user.js'
import tool from '@/utils/tool.js'
// 全局公共方法
const install = (Vue, vm) => {

	// 登录成功之后的操作
	const login = (userInfo) => {
		vm.$u.vuex('userName', userInfo.account)
		vm.$u.vuex('loginTime',new Date().toLocaleDateString())
		vm.$u.vuex('accessToken', userInfo.access_token)
		vm.$u.vuex('refreshToken', userInfo.refresh_token)
		vm.$u.vuex('expiresIn', userInfo.expires_in)
		vm.$u.vuex('isLogin', true)
		vm.$u.vuex('userId', userInfo.user_id)
		uni.hideLoading();
		uni.redirectTo({
			url: '/pages/home/home'
		})
	}
	
	// 退出登录
	const logout = () => {
		vm.$u.vuex('accessToken', '')
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
	
	const navigateBack = () => {
		uni.navigateBack({
			delta:1,//返回层数，2则上上页
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
	
	let hasRegisterScanner = false;
	// 注册扫码组件
	const registerScanner = (callback) => {
		if (hasRegisterScanner){
			uni.$off('on-scanner-data')
		}
		hasRegisterScanner = true;
		uni.$on('on-scanner-data', (data) => {
			callback(data)
		})
	}

	// 移除扫码组件注册
	const unRegisterScanner = () => {
		hasRegisterScanner = false;
		uni.$off('on-scanner-data')
	}

	// 将定义的方法挂载，使用this.$u.func.xx调用
	Vue.prototype.$u.func = {
		login,
		logout,
		route,
		navigateBack,
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
