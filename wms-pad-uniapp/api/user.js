import http from '@/http/api.js'

// 获取token
const token = (tenantId, username, password, type) => {
	return http.request({
		url: '/api/ApiPDA/token',
		method: 'POST',
		header: {
			'Tenant-Id': tenantId
		},
		params: {
			tenantId,
			username,
			password,
		}
	})
}


// 获取PDA菜单
const getMenuList = () => {
	return http.request({
		url: '/api/ApiPDA/routes',
		method: 'GET',
		params: {
			user:'',
			topMenuId:'',
		}
	})
}
const updatePassword = (id,oldPassword,newPassword,newPassword1) => {
	return http.request({
		url: '/api/ApiPDA/UpdatePassword',
		method: 'POST',
		params: {
			id,
			oldPassword,
			newPassword,
			newPassword1,
		}
	})
}

//签到
const sign = (loginStatus,token,suoId) => {
	return http.request({
		url: '/api/ApiPDA/userOnlinesubmit',
		method: 'post',
		data:{
			loginStatus,
			suoId,
			token
		} 
	})
}

const userOnlineList = (user) => {
	return http.request({
		url: '/api/ApiPDA/userOnlineList',
		method: 'get',
		params: user
	})
	
}

const userRegisterList = () => {
	return http.request({
		url: '/api/ApiPDA/userRegisterList',
		method: 'get',
	})
}


export default {
	token,
	getMenuList,
	updatePassword,
	userOnlineList,
	userRegisterList,
	sign
}
