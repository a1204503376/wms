import http from '@/http/api.js'

const getPdaStockCountResponseList =(data)=>{
	return http.request({
		url: '/api/ApiPDA/count/StockStaticCheck/getPdaStockCountResponseList',
		method: 'POST',
		data:data,
	})
}

const getPdaStockCountDetailResponseList =(data)=>{
	return http.request({
		url: '/api/ApiPDA/count/StockStaticCheck/getPdaStockCountDetailResponseList',
		method: 'POST',
		data:data,
	})
}

const findPdaSkuQtyResponseList =(data)=>{
	return http.request({
		url: '/api/ApiPDA/count/StockStaticCheck/findPdaSkuQtyResponseList',
		method: 'POST',
		data:data,
	})
}

const generateCountReport =(data)=>{
	return http.request({
		url: '/api/ApiPDA/count/StockStaticCheck/generateCountReport',
		method: 'POST',
		data:data,
	})
}

const generateCountReportByAutoLocation =(data)=>{
	return http.request({
		url: '/api/ApiPDA/count/StockStaticCheck/generateCountReportByAutoLocation',
		method: 'POST',
		data:data,
	})
}


export default {
	getPdaStockCountResponseList,
	getPdaStockCountDetailResponseList,
	findPdaSkuQtyResponseList,
	generateCountReport,
	generateCountReportByAutoLocation
}