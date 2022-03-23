import request from '@/router/axios';

//列表
export const getList = (params) => {
    return request({
        url: '/api/blade-system/role/list',
        method: 'get',
        params: {
            ...params,
        }
    })
}
//新增
export const added = (row) => {
    return request({
        url: '/api/blade-system/role/submit',
        method: 'POST',
        data: row
    })
}
//修改
export const update = (row) => {
    return request({
        url: '/api/blade-system/role/submit',
        method: 'POST',
        data: row
    })
}
//上级角色
export const Superior = () => {
    return request({
        url: '/api/blade-system/role/tree',
        method: 'get'
    })
}
//删除
export const remove = (ids) => {
    return request({
        url: '/api/blade-system/role/remove',
        method: 'POST',
        params: {
            ids,
        }
    })
}
//权限设置
export const grantTree = () => {
    return request({
        url: '/api/blade-system/menu/grant-tree',
        method: 'get',
    })
}
//  下拉数据接口
export const getTopselect = (topMenuIds) => {
    return request({
        url: '/api/blade-system/tenant/select',
        method: 'get',
        params: {
            topMenuIds,
        }
    })
}
//权限确认
export const grant = (roleIds, menuIds, dataScopeIds, apiScopeIds) => {
    return request({
        url: '/api/blade-system/role/grant',
        method: 'post',
        data: {
            roleIds,
            menuIds,
            dataScopeIds,
            apiScopeIds
        }
    })
}
//角色分配树
export const getRole = (roleIds) => {
    return request({
        url: '/api/blade-system/menu/role-tree-keys',
        method: 'get',
        params: {
            roleIds,
        }
    })
}
export const getDetail = (id) => {
    return request({
        url: '/api/blade-system/role/detail',
        method: 'get',
        params: {
            id,
        }
    })
}
