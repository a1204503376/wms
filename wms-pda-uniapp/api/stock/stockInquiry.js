import http from '@/http/api.js'


const findAllStockByNo =(data,params)=>{
	return http.request({
		url: '/api/ApiPDA/stock/stockInterior/findAllStockByNo',
		method: 'POST',
		data:data,
		params:params
	})
}


export default {
	findAllStockByNo
}