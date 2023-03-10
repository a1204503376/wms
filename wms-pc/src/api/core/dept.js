import request from '@/router/axios'

export const getList = (params) => {
  return request({
    url: '/api/blade-system/dept/list',
    method: 'get',
      params: {
          ...params,
      }
  })
}

export const getPage = (current, size, params) =>{
    return request({
        url: '/api/blade-system/dept/page',
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
    url: '/api/blade-system/dept/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/blade-system/dept/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/blade-system/dept/submit',
    method: 'post',
    data: row
  })
}
export const getTree = () => {
  return request({
    url: '/api/blade-system/dept/tree',
    method: 'get',
  })
}
export const getDetail = (id) => {
  return request({
    url: '/api/blade-system/dept/detail',
    method: 'get',
    params: {
      id,
    }
  })
}
