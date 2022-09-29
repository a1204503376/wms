import http from '@/http/api.js'

const findAllPickingByNo = (data, params) => {
	return http.request({
		url: '/api/ApiPDA/pickByPcs/findAllPickingByNo',
		method: 'POST',
		data: data,
		params: params
	})
}

const pickByPcs = (data) => {
	return http.request({
		url: '/api/ApiPDA/pickByPcs/pickByPcs',
		method: 'POST',
		data: data
	})
}

const pickByBox = (data) => {
	return http.request({
		url: '/api/ApiPDA/pickByBox/pickByBox',
		method: 'POST',
		data: data
	})
}

const getPickingBySoBillId = (data) => {
	return http.request({
		url: '/api/ApiPDA/pickByPcs/findPickingBySoBillId',
		method: 'POST',
		data: data
	})
}

const getPickPlanBySoBillId = (data, params) => {
	return http.request({
		url: '/api/ApiPDA/pickByPcs/findPickPlanBySoBillId',
		method: 'POST',
		data: data,
		params: params
	})
}

const getPickPlanBySoBillIdAndBoxCode = (data) => {
	return http.request({
		url: '/api/ApiPDA/pickByPcs/findPickPlanBySoBillIdAndBoxCode',
		method: 'POST',
		data: data
	})
}

const bulkPick = (data) => {
	return http.request({
		url: '/api/ApiPDA/pickByPcs/bulkPick',
		method: 'POST',
		data: data
	})
}

const outStockCheckoutFindSoBill = (data, page) => {
	return http.request({
			url: '/api/ApiPDA/pickByPcs/outStockCheckoutFindSoBill',
			method: 'POST',
			data: data,
			params: page
		})
}

const outStockCheckout = (data) => {
	return http.request({
			url: '/api/ApiPDA/pickByPcs/outStockCheckout',
			method: 'POST',
			data: data
		})
}


export default {
	findAllPickingByNo,
	pickByPcs,
	pickByBox,
	getPickingBySoBillId,
	getPickPlanBySoBillId,
	getPickPlanBySoBillIdAndBoxCode,
	outStockCheckoutFindSoBill,
	outStockCheckout,
	bulkPick
}
