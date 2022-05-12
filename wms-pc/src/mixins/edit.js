import throttle from "lodash/throttle";
import func from "@/util/func";

export const editMixin = {
    props: {
        id: {type: String, default: ''}
    },
    data() {
        return {
            loading: false,
            form: {
                params: {},
                rules: {}
            }
        }
    },
    computed: {
        isEdit: function () {
            return func.isStr(this.id) && func.isNotEmpty(this.id) && this.id !== '0';
        }
    },
    created() {
        this.autoTableHeight();
        window.addEventListener('resize', this.autoTableHeight);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.autoTableHeight)
    },
    methods: {
        onSubmit() {
            if (this.checkGlobal()) {
                this.submitFormDebounce();
            }
        },
        // 提交表单节流，10s内只执行一次
        submitFormDebounce: throttle(function () {
            this.loading = true;
            this.submitFormParams().then(res => {
                this.$message.success(res.msg);
                this.onClose();
                if (func.isNotEmpty(res.router)){
                    this.$router.push(res.router);
                }
            });
            this.loading = false;
        }, 10000, {leading: true, trailing: false}),
        /**
         * 提交表单参数到后台程序，处理成功之后需要返回一个对象，包含两个属性：msg和router
         * @returns {msg: string, router: Object}
         */
        submitFormParams() {
            return new Promise((resolve, reject) => {
                resolve({msg: '提交成功'});
            });
        },
        checkForm() {
            let result;
            // noinspection JSUnresolvedFunction
            this.$refs['form'].validate((valid) => {
                result = valid;
            });
            return result;
        },
        checkGlobal() {
            return this.checkForm();
        },
        autoTableHeight() {
            this.$nextTick(() => {
                document.getElementById('container').style.height
                    = `${window.innerHeight - 150}px`;
            })
        },
        clearComponentCache() {
            if (!(this.$vnode && this.$vnode.data.keepAlive)) {
                return;
            }
            // noinspection JSUnresolvedVariable
            if (!(this.$vnode.parent
                && this.$vnode.parent.componentInstance
                && this.$vnode.parent.componentInstance.cache)) {
                return;
            }
            if (!this.$vnode.componentOptions) {
                return;
            }
            // noinspection JSUnresolvedVariable
            const key = this.$vnode.key == null
                ? this.$vnode.componentOptions.Ctor.cid + (this.$vnode.componentOptions.tag ? `::${this.$vnode.componentOptions.tag}` : '')
                : this.$vnode.key;
            const cache = this.$vnode.parent.componentInstance.cache;
            const keys = this.$vnode.parent.componentInstance.keys;
            if (cache[key]) {
                delete cache[key]
                if (keys.length) {
                    const index = keys.indexOf(key);
                    if (index > -1) {
                        keys.splice(index, 1)
                    }
                }
            }
        },
        onClose() {
            this.clearComponentCache();
            this.$destroy()
            // noinspection JSUnresolvedVariable
            this.$router.$avueRouter.closeTag(this.$route.path);
            this.$router.back();
        },
    }
}
