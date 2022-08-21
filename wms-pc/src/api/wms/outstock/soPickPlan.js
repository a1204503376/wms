import request from '@/router/axios'

export const getPage = (page, data) => {
    return request({
        url: '/api/wms/outstock/soBill/getSoPickPlanpage',
        method: 'post',
        params: page,
        data: data
    })
}
export const exportExcel = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/exportSoPickPlan',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}
