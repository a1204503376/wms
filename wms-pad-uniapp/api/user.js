import http from '@/http/api.js'

// 获取token
const token = (tenantId, username, password, type) => {
	// return http.request({
	// 	url: '/api/ApiPDA/token',
	// 	method: 'POST',
	// 	header: {
	// 		'Tenant-Id': tenantId
	// 	},
	// 	params: {
	// 		tenantId,
	// 		username,
	// 		password,
	// 	}
	// })
	
	return http.request({
		url: '/wms/outstock/header/page',
		method: 'GET'
	})
}


export default {
	token
}
