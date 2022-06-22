import http from '@/http/api.js'


const getReceiveList =(data,params)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByPcs/list',
		method: 'POST',
		data:data,
		params:params
	})
}


const getReceiveDetailList =(data)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByPcs/findDetailListByReceiveId',
		method: 'POST',
		data:data
	})
}

export default {
	getReceiveList,
	getReceiveDetailList
}