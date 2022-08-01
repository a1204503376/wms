import http from '@/http/api.js'


const freezeByLocCode = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/freezeByLocCode',
		method: 'POST',
		data: data
	})
}

const freezeByLotNumber = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/freezeByLotNumber',
		method: 'POST',
		data: data
	})
}

const freezeBySerialNumber = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/freezeBySerialNumber',
		method: 'POST',
		data: data
	})
}

const portionFreeze = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/portionFreeze',
		method: 'POST',
		data: data
	})
}

const unFreezeByLocCode = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/unFreezeByLocCode',
		method: 'POST',
		data: data
	})
}

const unFreezeByLotNumber = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/unFreezeByLotNumber',
		method: 'POST',
		data: data
	})
}

const unFreezeBySerialNumber = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/unFreezeBySerialNumber',
		method: 'POST',
		data: data
	})
}

const portionUnFreeze = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/portionUnFreeze',
		method: 'POST',
		data: data
	})
}

const freezeByBoxCode = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/freezeByBoxCode',
		method: 'POST',
		data: data
	})
}

const unfreezeByBoxCode = (data) => {
	return http.request({
		url: '/api/ApiPDA/stock/freezeOrUnFreeze/unfreezeByBoxCode',
		method: 'POST',
		data: data
	})
}




export default {
	freezeByLocCode,
	freezeByLotNumber,
	freezeBySerialNumber,
	portionFreeze,
	unFreezeByLocCode,
	unFreezeByLotNumber,
	unFreezeBySerialNumber,
	portionUnFreeze,
	freezeByBoxCode,
	unfreezeByBoxCode
}
