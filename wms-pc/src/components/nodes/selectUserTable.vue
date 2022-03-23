<template>
    <div>
        <selectTable
                v-model="props.value"
                :option="option"
                :data="data"
                :prop="prop"
                :disabled="disabled"
                :tableLoading="loading"
                size="medium"
                @change="onChange"
        ></selectTable>
    </div>
</template>

<script>
    import selectTable from "@/components/nodes/selectTable";
    import {getList} from "@/api/core/user";

    export default {
        name: "selectUserTable",
        props: {
            value: {type: String, default: ""},
            text: {type: String, default: ""},
            disabled: {type: Boolean, default: false},
            placeholder: {type: String, default: "请选择"},
            size: {type: String, default: null},
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
                        {
                            prop: "tenantName",
                            label: "所属租户",
                        },
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
                data: [],
                props: {
                    value: undefined
                }
            };
        },
        watch:{
            value:{
                handler:function(){
                    this.props.value = this.value;
                }
            }
        },
        mounted() {
            this.loading = true;
            getList().then(res => {
                this.data = res.data.data;
            }).finally(() => {
                this.loading = false;
            });
        },
        methods: {
            onChange(val, obj) {
                this.$emit('change', val, obj);
            }
        }
    }
</script>

<style scoped>
</style>
