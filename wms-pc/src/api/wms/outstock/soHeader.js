import request from '@/router/axios'

export const getPage = (params, data) => {
    return request({
        url: '/api/wms/outstock/header/page',
        method: 'post',
        params: {
            params
        },
        data: data
    })
}

// export const getList = (current, size, params) => {
//     return request({
//         url: '/api/wms/outstock/header/page',
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
//         url: '/api/wms/outstock/header/submit',
//         method: 'post',
//         data: row
//     })
// }
// export const update = (row) => {
//     return request({
//         url: '/api/wms/outstock/header/submit',
//         method: 'post',
//         data: row
//     })
// }
// export const getSoBillNo = (row) => {
//     return request({
//         url: '/api/wms/outstock/header/getSoBillNo',
//         method: 'get',
//     })
// }
// export const remove = (ids) => {
//     return request({
//         url: '/api/wms/outstock/header/remove',
//         method: 'post',
//         params: {
//             ids,
//         }
//     })
// }
// export const getDetail = (row) => {
//     return request({
//         url: '/api/wms/outstock/header/detail',
//         method: 'get',
//         params: {
//             soBillId: row
//         }
//     })
// }
//
// export const canEdit = (id) => {
//     return request({
//         url: '/api/wms/outstock/header/canEdit',
//         method: 'get',
//         params: {
//             soHeaderId: id
//         }
//     })
// }
//
// export const cancel = (ids) => {
//     return request({
//         url: '/api/wms/outstock/header/cancel',
//         method: 'post',
//         params: {
//             ids
//         }
//     })
// }
//
// export const exportFile = (data) => {
//     return request({
//         url: '/api/wms/outstock/header/export',
//         method: 'get',
//         params: {
//             ...data
//         },
//         responseType: 'blob'
//     })
// }
// export const callback = (data) => {
//     return request({
//         url: '/api/wms/outstock/header/callback',
//         method: 'post',
//         data: data
//     })
// }
//
// export const completed = (ids) => {
//     return request({
//         url: '/api/wms/outstock/header/completed',
//         method: 'post',
//         params: {
//             ids,
//         }
//     })
// }
//
// export const completedOutstock = (ids) => {
//     return request({
//         url: '/api/wms/outstock/header/complated/outstock',
//         method: 'post',
//         params: {
//             ids
//         }
//     })
// }
//
// export const cancelPick = (ids) => {
//     return request({
//         url: '/api/wms/outstock/pickplan/cancelPick',
//         method: 'get',
//         params: {
//             ids
//         }
//     })
// }

