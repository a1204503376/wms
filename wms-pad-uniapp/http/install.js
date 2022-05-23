// 获取api目录所有js文件
const files = require.context('@/api', false, /\.js$/)
// 此处第二个参数vm，就是我们在页面使用的this，可以通过vm获取vuex等操作
const install = (Vue, vm) => {
	// 将各个定义的接口名称，统一放进对象挂载到vm.$u.api下(因为vm就是this，也即this.$u.api)
	// 自动将所有api挂载到vm.$u.api中
	vm.$u.api = {}
	files.keys().forEach(key => {
		const api = files(key).default
		for (let item in api) {
			vm.$u.api[item] = api[item]
		}
	})
}

export default {
	install
}
