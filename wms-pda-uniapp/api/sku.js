import http from '@/http/api.js'


const getSkuDropDownBox =()=>{
	return http.request({
		url: '/api/ApiPDA/sku/findSkuDropDownBox',
		method: 'GET'
	})
}

const getSkuByCode=(params)=>{
	return http.request({
		url:'/api/ApiPDA/sku/findSkuByCode',
		method: 'POST',
		data:params
	})
}


export default {
	getSkuDropDownBox,
	getSkuByCode
}