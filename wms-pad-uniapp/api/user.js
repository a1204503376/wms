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

export default {
	token,
	getMenuList
}
