import http from '@/http/api.js'


const getAllSerialNumberManage = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/devanning/findAllSerialNumberManage',
		method: 'POST',
		data: data
	})
}

const devanningSubmit = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/devanning/devanningSubmit',
		method: 'POST',
		data: data
	})
}

export default {
	getAllSerialNumberManage,
	devanningSubmit
}
