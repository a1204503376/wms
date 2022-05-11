import debounce from "lodash/debounce";
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
        // 提交表单防抖
        submitFormDebounce: debounce(async function () {
            this.loading = true;
            await this.submitFormParams();
            this.loading = false;
        }, 500),
        // 该提交函数要求异步，用于统一上述防抖回调函数中的同步代码
        async submitFormParams() {
            console.log('提交表单数据', this.form.params);
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
