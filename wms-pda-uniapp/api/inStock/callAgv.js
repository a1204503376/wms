import http from '@/http/api.js'

const findStockByBoxCode =(data)=>{
	return http.request({
		url: '/api/ApiPDA/putaway/findStockByBoxCode',
		method: 'POST',
		data:data,
	})
}
const findLocByLpnType =(data)=>{
	return http.request({
		url: '/api/ApiPDA/putaway/findLocByLpnType',
		method: 'POST',
		data:data,
	})
}
const callAgv =(data)=>{
	return http.request({
		url: '/api/ApiPDA/putaway/callAgv',
		method: 'POST',
		data:data,
	})
}
 

export default {
	findStockByBoxCode,
	findLocByLpnType,
	callAgv
}