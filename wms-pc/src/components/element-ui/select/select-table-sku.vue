<template>
    <selectTable
        ref="selectTable"
        v-model="props.value"
        :data="props.data"
        :disabled="disabled"
        :option="option"
        :prop="prop"
        :size="size || 'medium'"
        :tableLoading="loading"
        @change="onChange"
        @open="onOpen"
    ></selectTable>
</template>

<script>
import selectTable from "./select-table";
import {getList} from "@/api/wms/basedata/sku";

export default {
    name: "selectSkuTable",
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
        },
        beforeOpen: {
            type: Function, default: () => {
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
                    {
                        prop: "skuCode",
                        label: "物品编码",
                    },
                    {
                        prop: "skuName",
                        label: "物品名称",
                    },
                    {
                        prop: "skuNameS",
                        label: "物品简称",
                    },
                    {
                        prop: "skuType",
                        label: "分类",
                    },
                    {
                        prop: "wspName",
                        label: "包装",
                    }
                ]
            },
            prop: {
                label: "skuName",
                value: "skuId",
            },
            props: {
                value: undefined,
                data: [],
            },
            params:{},
        }
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
