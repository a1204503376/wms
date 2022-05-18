import request from "@/router/axios";

export const findSkuUmSelectResponseListBySkuId = (data) => {
    return request({
        url: '/api/wms/basedata/sku/findSkuUmSelectResponseListBySkuId',
        method: 'post',
        data: data
    })
}
