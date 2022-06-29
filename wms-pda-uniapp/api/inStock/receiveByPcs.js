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

const getDetailByDetailId =(data)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByPcs/findDetailByReceiveDetailId',
		method: 'POST',
		data:data
	})
}

const submitReceiptByPcs =(data)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByPcs/receiptByPcs',
		method: 'POST',
		data:data
	})
}

const getLocationByLocCode =(data)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByPcs/findThisLocationByLocCode',
		method: 'POST',
		data:data
	})
}

const getStockByBoxCode =(data)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByPcs/findThisStockByBoxCode',
		method: 'POST',
		data:data
	})
}

const getSerialNumberList =(data)=>{
	return http.request({
		url: '/api/ApiPDA/receiveByPcs/getSerialNumberList',
		method: 'POST',
		data:data
	})
}




export default {
	getReceiveList,
	getReceiveDetailList,
	getDetailByDetailId,
	submitReceiptByPcs,
	getLocationByLocCode,
	getStockByBoxCode,
	getSerialNumberList
}