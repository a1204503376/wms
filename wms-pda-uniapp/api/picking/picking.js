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

export default {
	findAllPickingByNo,
	pickByPcs,
	pickByBox,
	getPickingBySoBillId
}
