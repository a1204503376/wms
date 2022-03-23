import request from '@/router/axios'

export const createReplenishTask = (data) => {
  return request({
    url: '/api/wms/stock/transfer-header/createReplenishTask',
    method: 'post',
    data: data
  })
}

export const getNeedSku = () => {
    return request({
        url: '/api/wms/stock/transfer-header/getNeedSku',
        method: 'get'
    })
}

export const getNeedSkuDetail = (detailQuery) => {
    return request({
        url: '/api/wms/stock/transfer-header/getNeedSkuDetail',
        method: 'get',
        params: {
            ...detailQuery
        }
    })
}

