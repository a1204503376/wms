import http from '@/http/api.js'


const estimateStockMove = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/stockManage/estimateStockMove',
		method: 'POST',
		data: data
	})
}

const stockMove = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/stockManage/stockMove',
		method: 'POST',
		data: data
	})
}

const estimateStockMoveByLpn = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/stockManage/estimateStockMoveByLpn',
		method: 'POST',
		data: data
	})
}

const stockMoveByLpn = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/stockManage/stockMoveByLpn',
		method: 'POST',
		data: data
	})
}

const estimateStockMoveByBoxCode = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/stockManage/estimateStockMoveByBoxCode',
		method: 'POST',
		data: data
	})
}

const stockMoveByBoxCode = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/stockManage/stockMoveByBoxCode',
		method: 'POST',
		data: data
	})
}

export default {
	estimateStockMove,
	stockMove,
	estimateStockMoveByLpn,
	stockMoveByLpn,
	estimateStockMoveByBoxCode,
	stockMoveByBoxCode
}
