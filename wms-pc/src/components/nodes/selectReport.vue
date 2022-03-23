<template>
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
</template>

<script>
    import selectTable from "@/components/nodes/selectTable";
    import {getList} from "@/api/report/reportfile";

    export default {
        name: "selectReport",
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
                            prop: "name",
                            label: "名称",
                        }
                    ],
                },
                prop: {
                    label: "name",
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
