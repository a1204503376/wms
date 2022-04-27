import request from '@/router/axios'

export const getCrudColumnResponseList = (menuId) => request({
    url: '/api/wms/nodesCurdColumn/getCrudColumnResponseList',
    method: 'get',
    params: {
        menuId: menuId
    }
});

export const submit = (columnList) => request({
    url: '/api/wms/nodesCurdColumn/submit',
    method: 'post',
    data: columnList
})




