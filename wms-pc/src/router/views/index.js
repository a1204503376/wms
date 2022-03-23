import Layout from '@/page/index/'

export default [{
    path: '/wel',
    component: Layout,
    redirect: '/wel/index',
    children: [{
        path: 'index',
        name: '首页',
        meta: {
            i18n: 'dashboard'
        },
        component: () =>
            import( /* webpackChunkName: "views" */ '@/views/wel/index')
    }, {
        path: 'dashboard',
        name: '控制台',
        meta: {
            i18n: 'dashboard',
            menu: false,
        },
        component: () =>
            import( /* webpackChunkName: "views" */ '@/views/wel/dashboard')
    }]
}, {
    path: '/info',
    component: Layout,
    redirect: '/info/index',
    children: [{
        path: 'index',
        name: '个人信息',
        meta: {
            i18n: 'info'
        },
        component: () =>
            import( /* webpackChunkName: "views" */ '@/views/system/userinfo')
    }]
}, {
    path: '/form',
    component: Layout,
    children: [{
        path: '/form/index',
        name: 'form',
        component: () =>
            import('@/components/element-ui/form/index'),
        meta: {
            // keepAlive: true,
            // isTab: true,
            // isAuth: true
        }
    }]
}]
