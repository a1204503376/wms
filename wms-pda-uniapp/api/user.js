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

// 获取版本号
const UpdateVerDetail = () => {
	return http.request({
		url: '/api/ApiPDA/UpdateVerDetail',
		method: 'POST',
		data: {
			updateVer:''
		}
	})
}
//获取签到状态
const getSignInStatus = () => {
	return http.request({
		url: '/api/ApiPDA/getLoginStatus',
		method: 'GET',
	})
}

// 请求签到或签退
const editUserLoginStatus = (loginStatus,token) => {
	return http.request({
		url: '/api/ApiPDA/editUserLoginStatus',
		method: 'POST',
		data: {
				loginStatus,
				token,
			}
		
	})
}
export default {
	token,
	getMenuList,
	editUserLoginStatus,
	updatePassword,
	getSignInStatus,
	UpdateVerDetail
}
