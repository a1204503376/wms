import http from '@/http/api.js'

const findAllSkuLotByWoId =(params)=>{
	return http.request({
		url: '/api/ApiPDA/skuLot/findAllSkuLotByWoId',
		method: 'POST',
		data:params
	})
}


export default {
	findAllSkuLotByWoId
}