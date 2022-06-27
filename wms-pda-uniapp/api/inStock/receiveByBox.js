import http from '@/http/api.js'

const getReceiveDetailLpn =(boxCode)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByBox/query',
		method: 'GET',
		params:{
			boxCode
			}
	})
}

const receiveByCode =(data)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByBox/receiveByCode',
		method: 'POST',
	    data:data
	})
}

export default {
	getReceiveDetailLpn,
	receiveByCode
}