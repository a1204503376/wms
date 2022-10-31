<template>
    <el-select
        :clearable="true"
        :collapse-tags="true"
        :disabled="disabled"
        :filterable="true"
        :size="size"
        @change="onChange"
        placeholder="请选择"
        v-model="val">
        <el-option
            :key="item"
            :label="item"
            :value="item"
            v-for="item in dataSource">
        </el-option>
    </el-select>
</template>

<script>

    import {findSkuSpecSelectListBySkuId} from "@/api/wms/basics/sku";
    import func from "@/util/func";

    export default {
        name: "NodesSkuSpec",
        model: {
            prop: 'selectVal',
            event: 'selectValChange'
        },
        props: {
            // sku对象
            sku: {type: Object, required: false},
            // 组件大小，默认为mini, 支持 medium/small/mini
            size: {type: String, required: false, default: () => "mini"},
        },
        data() {
            return {
                val: '',
                dataSource: [],
                disabled: false //物品有规格则禁用，没有规格则不禁用
            }
        },
        watch: {
            sku: async function (newVal, oldVal) {
                // 选择物品后返回的规格如果为空 则取所有规格中的第一个作为该物品的默认规格
                if (func.isNotEmpty(newVal.skuSpec)) {
                    this.disabled = true;
                    this.dataSource = [newVal.skuSpec];
                } else {
                    this.disabled = false;
                    await this.getDataSource(newVal.skuId);
                }
                this.val = this.dataSource[0];
                this.onChange(this.val);
            },
        },
        methods: {
            async getDataSource(skuId) {
                if (func.isNotEmpty(skuId)) {
                    await findSkuSpecSelectListBySkuId(skuId).then((res) => {
                        this.dataSource = res.data.data;
                    });
                } else {
                    this.dataSource = [];
                    this.$emit('selectValChange', undefined);
                }
            },
            onChange(val) {
                this.$emit('selectValChange', val);
            },
        }
    }
</script>

<style scoped>

</style>
