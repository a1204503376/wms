import http from '@/http/api.js'

const getWarehouseList =()=>{
	return http.request({
		url: '/api/ApiPDA/getWarehouseList',
		method: 'GET',
	})
}

export default {
	getWarehouseList
}