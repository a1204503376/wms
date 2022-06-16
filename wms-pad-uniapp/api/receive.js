import http from '@/http/api.js'


const getReceiveList =(receive)=>{
	return http.request({
		url: '/api/ApiPDA/receive/list',
		method: 'POST',
		data:receive
	
	})
}

export default {
	getReceiveList
}