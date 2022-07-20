import http from '@/http/api.js'

const findAllPickingByNo = (data, params) => {
	return http.request({
		url: '/api/ApiPDA/picking/findAllPickingByNo',
		method: 'POST',
		data: data,
		params: params
	})
}

const pickingByPcs = (data) => {
	return http.request({
		url: '/api/ApiPDA/picking/pickingByPcs',
		method: 'POST',
		data: data
	})
}

const pickingByBox = (data) => {
	return http.request({
		url: '/api/ApiPDA/picking/pickingByBox',
		method: 'POST',
		data: data
	})
}

export default {
	findAllPickingByNo,
	pickingByPcs,
	pickingByBox
}
