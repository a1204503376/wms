import http from '@/http/api.js'

const receiveByMultiBox =(data)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByMultiBox/receiveByMultiBoxCode',
		method: 'POST',
	    data:data
	})
}


export default {
	receiveByMultiBox
}