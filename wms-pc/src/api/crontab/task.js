import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/crontab/task/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const start =(data) =>{
    return request({
        url: '/api/wms/task/start',
        method: 'post',
        data: data
    })
}
export const execute =(data) =>{
    return request({
        url: '/api/wms/task/execute',
        method: 'post',
        data: data
    })
}

export const stop =(id) =>{
    return request({
        url: '/api/wms/task/cancel',
        method: 'get',
        params: {
            id
        }
    })
}


export const getDetail = (id) => {
  return request({
    url: '/api/wms/crontab/task/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const getLog = (data,page) => {
    return request({
        url: '/api/wms/crontab/task/getLogById',
        method: 'post',
        params: page,
        data:data
    })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/crontab/task/remove',
    method: 'post',
    data: ids
  })
}

export const newCrontabTask = (row) => {
    return request({
        url: '/api/wms/crontab/task/newCrontabTask',
        method: 'post',
        data: row
    })
}

export const editCrontabTask = (row) => {
    return request({
        url: '/api/wms/crontab/task/editCrontabTask',
        method: 'post',
        data: row
    })
}


export const detailById = (id) => {
    return request({
        url: '/api/wms/crontab/task/detailById',
        method: 'get',

    })
}



