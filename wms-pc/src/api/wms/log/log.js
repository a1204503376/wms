import request from '@/router/axios'

export const getLogMsgCount = () => {
    return request({
        url: '/api/wms/log/getLogMsgCount',
        method: 'get',
    })
}
export const getLogMsgList = (num) => {
    return request({
        url: '/api/wms/log/getLogMsgList',
        method: 'get',
        params: {
           num
        }
    })
}
export const editLogMsgReaded = (num,id) => {
    return request({
        url: '/api/wms/log/editLogMsgReaded',
        method: 'get',
        params: {
            num,
            id
        }
    })
}

