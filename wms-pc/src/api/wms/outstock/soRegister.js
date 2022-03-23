import request from '@/router/axios'

export const getList = (current, size, params) => {
  return request({
    url: '/api/wms/outstock/soRegister/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/wms/outstock/soRegister/detail',
    method: 'get',
    params: {
      trrId:id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/outstock/soRegister/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/outstock/soRegister/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/outstock/soregister/submit',
    method: 'post',
    data: row
  })
}
