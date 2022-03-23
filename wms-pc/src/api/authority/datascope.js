import request from '@/router/axios';

//权限 /api/blade-system/data-scope/list    点击编辑的接口
export const Treepaging = (params) => {
    return request({
        url: '/api/blade-system/data-scope/list',
        method: 'get',
        params: {
            ...params
        }
    })
}
//新增
export const add = (row) => {
    return request({
        url: '/api/blade-system/data-scope/submit',
        method: 'post',
        data: row
    })
}
//修改
export const update = (row) => {
    return request({
        url: '/api/blade-system/data-scope/update',
        method: 'POST',
        data: row
    })
}
//删除
export const remove = (ids) => {
    return request({
        url: '/api/blade-system/data-scope/remove',
        method: 'POST',
        params: {
            ids,
        }
    })
}
