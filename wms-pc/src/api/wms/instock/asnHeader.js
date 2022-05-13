import request from '@/router/axios'

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/asn/page',
        method: 'post',
        params:page,
        data: params
    })
}

export const remove = (removeObj) => {
    return request({
        url: '/api/wms/asn/remove',
        method: 'post',
        data: removeObj
    })
}

export const exportFile = (params) => {
    return request({
        url: '/api/wms/asn/export',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}

export const add = (params) => {
    return request({
        url: '/api/wms/asn/add',
        method: 'post',
        data: params,
    })
}
// export const getPage = (current, size, params) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/page',
//         method: 'get',
//         params: {
//             ...params,
//             current,
//             size,
//         }
//     })
// }
// export const add = (row) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/submit',
//         method: 'post',
//         data: row
//     })
// }

// export const update = (row) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/submit',
//         method: 'post',
//         data: row
//     })
// }
// export const remove = (ids) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/remove',
//         method: 'post',
//         params: {
//             ids,
//         }
//     })
// }
// export const finishAsnBill = (ids) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/finishAsnBill',
//         method: 'post',
//         params: {
//             ids,
//         }
//     })
// }
// export const creatLpn = (ids) => {
//     return request({
//         url: '/api/wms/instock/asnLpnDetail/creatLpn',
//         method: 'post',
//         params: {
//             ids,
//         }
//     })
// }
// export const getDetail = (row) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/detail',
//         method: 'get',
//         params: {
//             asnBillId: row
//         }
//     })
// }

// export const canEdit = (id) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/canEdit',
//         method: 'get',
//         params: {
//             asnHeaderId: id
//         }
//     })
// }

// export const getAsnBillNo = (row) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/getAsnBillNo',
//         method: 'get',
//     })
// }

// export const cancel = (ids) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/cancel',
//         method: 'post',
//         params: {
//             ids
//         }
//     })
// }

// export const exportFile = (data) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/export',
//         method: 'get',
//         params: {
//             ...data
//         },
//         responseType: 'blob'
//     })
// }

// export const callback = (data) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/callback',
//         method: 'post',
//         data: data
//     })
// }

// export const printBoxLabel = (ids) => {
//     return request({
//         url: '/api/wms/instock/asnHeader/print/boxLabel',
//         method: 'post',
//         params: {
//             ids
//         }
//     })
// }
