import http from '@/http/api.js'

const getWarehouseList =()=>{
	return http.request({
		url: '/api/ApiPDA/getWarehouseList',
		method: 'GET',
	})
}

const warehouseChange =(warehouse)=>{
	return http.request({
		url: '/api/ApiPDA/warehouseChange',
		method: 'POST',
		data: {
			warehouse
		}
	})
}


export default {
	getWarehouseList,
	warehouseChange
}