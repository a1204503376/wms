import http from '@/http/api.js'

// 获取条形码规则
const barcodeRules  = ()  => {
	 return http.request({
		url: '/api/ApiPDA/findAllSysBarCodeRule',
		method: 'GET'
	})
}


export default {
	barcodeRules
}
