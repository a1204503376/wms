import request from "@/router/axios";

export const getStockData = () => {
    return request({
        url: '/api/wms/index/stockData',
        method: 'get'
    })
}

export const getInStockRate = () => {
    return request({
        url: '/api/wms/index/inStockRate',
        method: 'get'
    })
}

export const getOutStockRate = () => {
    return request({
        url: '/api/wms/index/outStockRate',
        method: 'get'
    })
}
