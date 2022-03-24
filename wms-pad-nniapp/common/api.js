const install = (Vue, vm) => {
	const http = vm.$u.http

	const config = vm.vuex_config


	vm.$u.api = {
		// 获取token
		getToken: (params = {}) => http.post(config.baseUrl + '/api/ApiPDA/token', {}, {params: params}),

		// 获取菜单
		getRoutes: (params = {}) => http.post(config.baseUrl + '/api/ApiPDA/routePDA',{}, params),

		// 获取任务列表
		getTaskList: (params = {}) => http.post(config.baseUrl + '/api/taskPDA/inStockTaskList',{}, {params: params}),

		//采购单查看共通页面
		getAsnPDAList: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/pdaAsnList',{}, {params: params}),
		
		//退料接收查询
		detailReturninbillPDA: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/detailReturninbillPDA',{}, {params: params}),
		
		//退料接收提交
		asnReturninPDA: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/asnReturninPDA', params),
		
		//修旧利废退料查询
		detailReturnxjPDAbill: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/detailReturnxjPDAbill', params),
		
		//修旧利废退料保存
		asnReturnxjPDA: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/asnReturnxjPDA', params),
		
		//采购接收查询
		getAsnPDADetail: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/pdaAsnDetail',{}, {params: params}),
		
		//采购接收提交
		submitRcv: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/pdaAsnDetailRcv', params),

		//质检明细查询
		detailQcPDAbill: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/detailQcPDAbill',{}, {params: params}),

		//质检保存
		asnQualityPDA: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/asnQualityPDA',params),

		//收货明细查询
		detailContainerPDAbill: (params = {}) => http.post(config.baseUrl +'/api/InStockPDA/detailContainerPDAbill',{}, {params: params}),

		//收货保存
		asnContainerPDA: (params = {}) => http.post(config.baseUrl + '/api/InStockPDA/asnContainerPDA', params),

		//获取待上架数据
		getputawaySku: (params = {}) => http.post(config.baseUrl + '/api/ApiPDA/putawaySkuSelect',{}, { params: params }),

		//获取推荐库位
		getRecommendLocCode: (params = {}) => http.post(config.baseUrl + '/api/ApiPDA/getRecommendLocCode',{}, { params: params }),

		//保存上架数据
		setputawaySku: (params = {}) => http.post(config.baseUrl + '/api/ApiPDA/putawaySkuSubmit',{}, { params: params }),

		//物资移动查询
		moveSkuSelect: (params = {}) => http.post(config.baseUrl + '/api/StockMove/moveSkuSelect',{}, {params: params}),

		//物资移动保存
		moveSkuSubmit: (params = {}) => http.post(config.baseUrl + '/api/StockMove/moveSkuSubmit',{}, {params: params}),

		//物资盘点查询
		inventorySkuSelect: (params = {}) => http.post(config.baseUrl +'/api/StockCountPDA/inventorySkuSelect',{}, {params: params}),

		//物资盘点保存List
		inventorySkuSubmitList:(params = {}) => http.post(config.baseUrl + '/api/StockCountPDA/inventorySkuSubmitList', params),

		//获取发货任务
		getOutStockTask: (params = {}) => http.post(config.baseUrl + '/api/taskPDA/outStockTaskList', {
			params: params
		}),

		//根据出库单头表ID获取出库单明细
		getSoDetailBySoBillId: (params = {}) => http.post(config.baseUrl +
			'/api/OutStock/pdaListBySoBillId', {
				params: params
			}),

		//根据物资ID获取推荐库位
		getRecommendLocationBySkuId: (params = {}) => http.post(config.baseUrl +
			'/api/OutStock/getRecommendLocationBySkuId', {
				params: params
			}),

		//保存出库明细
		saveSoDetail: (params = {}) => http.post(config.baseUrl + '/api/OutStock/pdaSaveSoDetail', {
			params: params,
		}),

		//根据库位获取库存数据
		getStockByLoc: (params = {}) => http.post(config.baseUrl + '/api/ApiPDA/pdaGetStockByLocCode',{}, { params: params }),
		
		//根据库位获取库存数据
		checkStock: (params = {}) => http.post(config.baseUrl + '/api/ApiPDA/pdaCheckStock',{}, { params: params }),
	}

}

export default {
	install
}
