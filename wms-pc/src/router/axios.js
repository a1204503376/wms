// noinspection JSUnresolvedFunction,JSUnresolvedVariable

/**
 * 全站http配置
 *
 * axios参数说明
 * isSerialize是否开启form表单提交
 * isToken是否需要token
 */
import axios from 'axios';
import store from '@/store/';
import router from '@/router/router';
import {serialize} from '@/util/util';
import {getToken} from '@/util/auth';
import {Message} from 'element-ui';
import website from '@/config/website';
import {Base64} from 'js-base64';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import func from '@/util/func';
import {dateFormat} from "@/util/date";

//默认超时时间
axios.defaults.timeout = 1000 * 60;
//返回其他状态码
axios.defaults.validateStatus = function (status) {
    return status >= 200 && status <= 500;
};
//跨域请求，允许保存cookie
axios.defaults.withCredentials = true;
// NProgress 配置
NProgress.configure({
    showSpinner: false
});

function processingDate(obj) {
    if (func.isArray(obj)) {
        let temp = [];
        obj.forEach((item) => {
            temp.push(processingDate(item));
        })
        return temp;
    }
    if (obj) {
        let dateRangeParams;
        for (let key of Object.keys(obj)) {
            let suffix = 'DateRange';
            let value = obj[key];
            if (key.endsWith(suffix)
                && func.isArray(value)
                && value.length === 2
                && func.isNotEmpty(value[0])
                && func.isNotEmpty(value[1])
            ) {
                dateRangeParams = {};
                let prefixItem = key.substring(0, key.indexOf(suffix));
                dateRangeParams[`${prefixItem}Begin`] = dateFormat(`${value[0]} 00:00:00`);
                dateRangeParams[`${prefixItem}End`] = dateFormat(`${value[1]} 23:59:59`);
            }
        }
        let flag = func.isEmpty(dateRangeParams);
        if (flag) {
            return obj;
        }
        return Object.assign({}, obj, dateRangeParams);
    }
}

//http request拦截
axios.interceptors.request.use(config => {
    //开启 progress bar
    NProgress.start();
    const meta = (config.meta || {});
    const isToken = meta.isToken === false;
    config.headers['Authorization'] = `Basic ${Base64.encode(`${website.clientId}:${website.clientSecret}`)}`;
    //让每个请求携带token
    if (getToken() && !isToken) {
        config.headers[website.tokenHeader] = 'bearer ' + getToken()
    }
    //headers中配置text请求
    if (config.text === true) {
        config.headers["Content-Type"] = "text/plain";
    }
    //headers中配置serialize为true开启序列化
    if (config.method === 'post') {
        config.data = processingDate(config.data);
        if (meta.isSerialize === true) {
            config.data = serialize(config.data);
        }
    }
    return config
}, error => {
    return Promise.reject(error)
});
//http response 拦截
axios.interceptors.response.use(res => {
    //关闭 progress bar
    NProgress.done();
    //获取状态码
    const status = res.data.code || res.status;
    const statusWhiteList = website.statusWhiteList || [];
    const message = res.data.msg || res.data.error_description || '连接服务器失败';
    //如果在白名单里则自行catch逻辑处理
    if (statusWhiteList.includes(status)) return Promise.reject(res);
    //如果是401则跳转到登录页面
    if (status === 401) store.dispatch('FedLogOut').then(() => router.push({path: '/login'}));
    // 如果请求为非200否者默认统一处理
    if (status !== 200) {
        Message({
            message: message,
            type: 'error'
        });
        return Promise.reject(new Error(message))
    }
    return res;
}, error => {
    NProgress.done();
    Message({
        message: '连接服务器失败',
        type: 'error'
    });
    return Promise.reject(new Error(error));
});

export default axios;
