// 定义一个混入对象
let paramsMemoryMixin = {
    data () {
        return {
            // 记住参数存储的key, 请在引用该mixin的组件中重写
            memoryParamsKey: 'nb-memory-params'
        }
    },
    created: function () {
        this.initParams();
        // 在页面刷新时将筛选信息保存到sessionStorage里
        window.addEventListener('beforeunload', this.onPageUnload);
    },
    methods: {
        initParams () {
            let userParams = JSON.parse(sessionStorage.getItem(this.memoryParamsKey));

            for (let key in userParams) {
                this[key] = userParams[key];
            }
        },
        onPageUnload () {
            sessionStorage.setItem(this.memoryParamsKey, JSON.stringify(this.getMemoryParams()));
        },

        /**
         * 需要记住的页面参数
         * @return { key: value }
         */
        getMemoryParams () {
            throw Error('请重写paramsMemoryMixin的getMemoryParams方法');
        }
    },

    beforeDestroy () {
        window.removeEventListener('beforeunload', this.onPageUnload);
    },

    beforeRouteLeave (to, from, next) {
        this.onPageUnload();
        next();
    }
};

export default paramsMemoryMixin;
