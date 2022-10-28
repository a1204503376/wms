import request from '@/router/axios';

export const getSkuSelectByPage = (page, query) => {
    return request({
        url: '/api/wms/basedata/sku/getSkuSelectByPage',
        method: 'post',
        data: {
            key: query
        },
        params: page
    })
}

export const findSkuSpecSelectListBySkuId = (skuId) => {
    return request({
        url: '/api/wms/basedata/sku/findSkuSpecSelectListBySkuId',
        method: 'post',
        data: {
            skuId: skuId
        }
    })
}

