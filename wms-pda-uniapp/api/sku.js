import http from '@/http/api.js'


const getSkuDropDownBox =()=>{
	return http.request({
		url: '/api/ApiPDA/sku/findSkuDropDownBox',
		method: 'GET'
	})
}


export default {
	getSkuDropDownBox
}