import request from '@/router/axios';

export const getList = (params) => {
  return request({
    url: '/api/wms/basedata/skulotval/list',
    method: 'get',
    params: {
      ...params
    }
  })
}

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/basedata/skulotval/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (wslvId) => {
  return request({
    url: '/api/wms/basedata/skulotval/detail',
    method: 'get',
    params: {
      wslvId
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/basedata/skulotval/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/basedata/skulotval/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/wms/basedata/skulotval/submit',
    method: 'post',
    data: row
  })
}
export const exportFile = (data) => {
  return request({
      url: '/api/wms/basedata/skulotval/export',
      method: 'get',
      params: {
          ...data
      },
      responseType: 'blob'
  })
}
