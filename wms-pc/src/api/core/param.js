import request from '@/router/axios'

export const getList = (current, size, params) => {
  return request({
    url: '/api/blade-system/param/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getPage = (current, size, params) => {
    return request({
        url: '/api/blade-system/param/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const getDetail = (params)=>{
  return request({
    url: '/api/blade-system/param/detail',
    method: 'get',
    params: {
      ...params
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/blade-system/param/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/blade-system/param/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/blade-system/param/submit',
    method: 'post',
    data: row
  })
}
export const detail = (id)=>{
  return request({
    url: '/api/blade-system/param/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const changeVisible = (id, type) => request({
  url: '/api/blade-system/param/change-visible',
  method: 'get',
  params: {
    id, type
  }
});
