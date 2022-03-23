import request from '@/router/axios'
export const getList = (params) => {
    return request({
        url: '/api/blade-system/region/list',
        method: 'get',
        params: {
            ...params,
        }
    })
}

export const getPage = (current, size, params) => {
    return request({
        url: '/api/blade-system/region/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const getDetail = (code) => {
    return request({
        url: '/api/blade-system/region/detail',
        method: 'get',
        params: {
           code
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/blade-system/region/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/blade-system/region/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/blade-system/region/submit',
        method: 'post',
        data: row
    })
}



