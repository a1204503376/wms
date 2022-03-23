import request from '@/router/axios'

export const getColumnList = () => request({
    url: '/api/nodesCurdColumn/list',
    method: 'get'
});


