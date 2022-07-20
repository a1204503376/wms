import http from '@/http/api.js'



const getConnectionAreaLocation = (data, params) => {
	return http.request({
		url: '/api/ApiPDA/connectionAreaPicking/OutboundAccessAreaLocationQuery',
		method: 'POST',
		data: data,
		params: params
	})
}

const connectionAreaPicking = (data) => {
	return http.request({
		url: '/api/ApiPDA/connectionAreaPicking/ConnectionAreaPicking',
		method: 'POST',
		data: data
	})
}

const connectionAreaMove = (data) => {
	return http.request({
		url: '/api/ApiPDA/connectionAreaPicking/ConnectionAreaMove',
		method: 'POST',
		data: data
	})
}


export default {
	getConnectionAreaLocation,
	connectionAreaPicking,
	connectionAreaMove
}
