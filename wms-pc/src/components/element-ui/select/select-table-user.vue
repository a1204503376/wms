<template>
    <selectTable
        ref="selectTable"
        v-model="props.value"
        :data="data"
        :disabled="disabled"
        :option="option"
        :prop="prop"
        :size="size || 'medium'"
        :tableLoading="loading"
        @change="onChange"
    ></selectTable>
</template>

<script>
import selectTable from "./select-table";
import {getList} from "@/api/core/user";

export default {
    name: "selectUserTable",
    props: {
        value: {type: String, default: undefined},
        text: {type: String, default: ""},
        disabled: {type: Boolean, default: false},
        placeholder: {type: String, default: "请选择"},
        size: {type: String, default: null},
        data: {
            type: Array, default: () => {
                return [];
            }
        }
    },
    components: {
        selectTable,
    },
    model: {
        prop: 'value',
        event: 'change'
    },
    data() {
        return {
            loading: false,
            option: {
                column: [
                    // {
                    //     prop: "tenantName",
                    //     label: "所属租户",
                    // },
                    {
                        prop: "name",
                        label: "用户昵称",
                    },
                    {
                        prop: "realName",
                        label: "用户姓名",
                    },
                    {
                        prop: "roleName",
                        label: "用户角色"
                    },
                    {
                        prop: "deptName",
                        label: "机构"
                    }
                ],
            },
            prop: {
                label: "realName",
                value: "id",
            },
            props: {
                value: undefined,
                data: [],
            }
        };
    },
    watch: {
        value: {
            handler: function () {
                this.props.value = this.value;
            }
        },
        data: {
            handler: function () {
                this.props.data = this.data;
            },
            deep: true,
            immediate: true,
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.onLoad();
        })
    },
    methods: {
        onLoad() {
            if (this.loading) {
                return;
            }
            this.loading = true;
            getList(this.params).then(res => {
                this.props.data = res.data.data;
                if (!this.validatenull(this.props.value) || !this.validatenull(this.value) && this.$refs.selectTable) {
                    this.$refs.selectTable.$emit('change', this.value);
                }
            }).finally(() => {
                this.loading = false;
            });
        },
        onChange(val, obj) {
            this.$emit('change', val, obj);
        },
        onOpen() {
            if (this.beforeOpen) {
                this.beforeOpen(this);
            }
            if (JSON.stringify(this.params) != JSON.stringify(this.paramsOld)) {
                this.paramsOld = this.deepClone(this.params);
                this.onLoad();
            }
        }
    }
}
</script>

<style scoped>
</style>
