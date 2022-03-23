import request from '@/router/axios'

export const getSkuPackage = (params) => {
    return request({
        url: '/api/wms/basedata/sku-inc/getSkuPackage',
        method: 'get',
        params: params
    })
}
