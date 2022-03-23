import request from '@/router/axios'

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/basedata/skupackage/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getList = (params)=>{
    return request({
        url:'/api/wms/basedata/skupackage/list',
        method:'get',
        params:{
            ...params
        }
    })
}

export const submit = (row) => {
  return request({
    url: '/api/wms/basedata/skupackage/submit',
    method: 'post',
    data: row
  })
}

export const getDetail = (wspId) => {
  return request({
    url: '/api/wms/basedata/skupackage/detail',
    method: 'get',
    params: {
      wspId,
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/basedata/skupackage/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/basedata/skupackage/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/wms/basedata/skupackage/submit',
    method: 'post',
    data: row
  })
}

export const exportFile = (data) => {
  return request({
    url: '/api/wms/basedata/skupackage/export',
    method: 'get',
    params: {
      ...data
    },
    responseType: 'blob'
  })
}

