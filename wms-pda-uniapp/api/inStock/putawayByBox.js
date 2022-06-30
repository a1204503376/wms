import http from '@/http/api.js'

const getPutawayData =(data)=>{
	return http.request({
		url: '/api/ApiPDA/putaway/findPutawayDataByBoxCode',
		method: 'POST',
		data:data,
	})
}

export default {
	getPutawayData
}