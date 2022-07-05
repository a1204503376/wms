import http from '@/http/api.js'

const findStockByBoxCode =(data)=>{
	return http.request({
		url: '/api/ApiPDA/putaway/findStockByBoxCode',
		method: 'POST',
		data:data,
	})
}

export default {
	findStockByBoxCode
}