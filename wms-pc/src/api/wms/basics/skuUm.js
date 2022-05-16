import request from "@/router/axios";

export const getSkuUmSelectResponseListBySkuId = (data) => {
    return request({
        url: '/api/wms/basedata/sku/getSkuUmSelectResponseListBySkuId',
        method: 'post',
        data: data
    })
}
