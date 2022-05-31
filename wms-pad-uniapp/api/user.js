import http from '@/http/api.js'

// 获取token
const token = (tenantId, username, password, type) => {
	return http.request({
		url: '/api/ApiPDA/token',
		method: 'POST',
		header: {
			'Tenant-Id': tenantId
		},
		params: {
			tenantId,
			username,
			password,
		}
	})
}

// 获取PDA菜单
const getMenuList = () => {
	return http.request({
		url: '/api/ApiPDA/routes',
		method: 'GET',
		params: {
			user:'',
			topMenuId:'',
		}
	})
}

const updatePassword = (id,oldPassword,newPassword,newPassword1) => {
	return http.request({
		url: '/api/ApiPDA/UpdatePassword',
		method: 'POST',
		params: {
			id,
			oldPassword,
			newPassword,
			newPassword1,
		}
	})
}

// 检查跟新
const CheckUpdate = () => {
	return http.request({
		url: '/api/ApiPDA/UpdateVerDetail',
		method: 'POST',
		data: {
			updateVer:''
		}
	})
}
export default {
	token,
	getMenuList,
	updatePassword,
	CheckUpdate
}
