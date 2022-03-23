import request from '@/router/axios'

export const getList = (params) => {
  return request({
    url: '/api/blade-user/list',
    method: 'get',
    params: {
      ...params
    }
  })
}

export const getPage = (current, size, params) => {
  return request({
    url: '/api/blade-user/page',
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
    url: '/api/blade-user/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/blade-user/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/blade-user/update',
    method: 'post',
    data: row
  })
}

export const getUser = (id) => {
  return request({
    url: '/api/blade-user/detail',
    method: 'get',
    params: {
      id,
    }
  })
}

export const resetPassword = (userIds) => {
  return request({
    url: '/api/blade-user/reset-password',
    method: 'post',
    params: {
      userIds,
    }
  })
}

export const updatePassword = (oldPassword, newPassword, newPassword1) => {
  return request({
    url: '/api/blade-user/update-password',
    method: 'post',
    params: {
      oldPassword,
      newPassword,
      newPassword1,
    }
  })
}

export const updateInfo = (form) => {
  return request({
    url: '/api/blade-user/update-info',
    method: 'post',
    data: form
  })
}

export const getUserInfo = () => {
  return request({
    url: '/api/blade-user/info',
    method: 'get',
  })
}