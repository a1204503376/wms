import request from '@/router/axios'

export const getList = (params) => {
    return request({
        url: '/api/blade-system/dict/list',
        method: 'get',
        params: {
            ...params,
        }
    })
}

export const getPage = (current, size, params) => {
    return request({
        url: '/api/blade-system/dict/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/blade-system/dict/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/blade-system/dict/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/blade-system/dict/submit',
        method: 'post',
        data: row
    })
}


export const getDetail = (id) => {
    return request({
        url: '/api/blade-system/dict/detail',
        method: 'get',
        params: {
            id,
        }
    })
}
export const getTree = () => {
    return request({
        url: '/api/blade-system/dict/tree',
        method: 'get'
    })
}

export const getDictByCode = (code) => {
    return request({
        url: '/api/wms/dictionary/getListByCode',
        method: 'get',
        params: {
            code
        }
    })
}
/**
 * 获取入库单的单据类型字典
 * @returns {*}
 */
export const getInStoreType = () => {
    return getDictByCode('instore_type');
}

export const changeVisible = (id, type) => request({
    url: '/api/blade-system/dict/change-visible',
    method: 'get',
    params: {
        id, type
    }
});



