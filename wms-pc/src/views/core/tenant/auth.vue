<template>
    <edit-dialog ref="dialogEdit"
                 v-model="form"
                 title="租户授权配置"
                 :visible="visible"
                 :isEdit="false"
                 :isView="isView"
                 :group="group"
                 :dataSource="form"
                 width="50%"
                 :root="$parent"
                 @before-open="beforeOpen"
                 @callback="callback">

    </edit-dialog>
</template>

<script>
    import EditDialog from "@/components/nodes/editDialog";
    import {setting} from "@/api/core/tenant";

    export default {
        name: "auth",
        components: {EditDialog},
        props: {
            visible: {type: Boolean, default: false},
            ids: {type: String, default: ''},
            dataSource: {
                type: Object, default: function () {
                    return {};
                }
            }
        },
        watch: {
            dataSource: {
                handler: function (newVal, oldVal) {
                    this.form = Object.assign({}, newVal);
                },
                deep: true,
                immediate: true
            }
        },
        data() {
            return {
                form: {},
                isView: false,
                group: [
                    {
                        label: '',
                        column: [
                            {
                                label: "账号额度",
                                prop: "accountNumber",
                                type: "number",
                                span: 24,
                            },
                            {
                                label: "过期时间",
                                prop: "expireTime",
                                type: "date",
                                format: "yyyy-MM-dd hh:mm:ss",
                                valueFormat: "yyyy-MM-dd hh:mm:ss",
                                span: 24,
                            },
                            {
                                label: '库房上限',
                                prop: 'whMax',
                                type: 'inputNumber',
                                span: 24,
                                min: 1
                            }
                        ]
                    }
                ],
            }
        },
        methods: {
            beforeOpen(done, type, finish) {
                done();
            },
            callback(data) {
                if (data.success) {
                    this.save(data);
                } else {
                    this.$emit('callback', data);
                }
            },
            save(data) {
                setting(this.ids, data.data).then(() => {
                    this.$emit('callback', data);
                }).finally(() => {
                    data.loading();
                });
            }
        }
    }
</script>

<style scoped>

</style>
