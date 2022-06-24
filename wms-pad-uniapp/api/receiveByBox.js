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


export default {
	getReceiveDetailLpn
}