import request from '@/router/axios'


export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/outstock/wellen/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
};

export const getDetail=(row)=>{
    return request({
        url: '/api/wms/outstock/wellen/detail',
        method: 'get',
        params: {
            wellenId: row
        }
    })
}
