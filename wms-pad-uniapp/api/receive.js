import http from '@/http/api.js'


const getReceiveList =(params)=>{
	return http.request({
		url: '/api/ApiPDA/receive/list',
		method: 'POST',
		data:params
	
	})
}

export default {
	getReceiveList
}