<template>
    <el-select
        v-model="val"
        :clearable="true"
        :collapse-tags="true"
        :disabled="disabled"
        :filterable="true"
        :size="size"
        placeholder="请选择"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item"
            :label="item"
            :value="item">
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
        selectVal: [String],
        // sku对象
        sku: {type: Object, required: false},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"},
        // 是否禁用 默认为 false不禁用
        disabled: {type: Boolean, required: false, default: () => false}
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: []
        }
    },
    watch: {
        sku: async function (newVal, oldVal) {
            // sku != {} （空对象）时, 才进行查询 ,为{}时, 清空组件
            if (Object.keys(newVal).length !== 0) {
                await this.getDataSource(newVal.skuId);
                // 物品规格数据大于0的话取第一个作为默认规格
                if (this.dataSource.length > 0 && func.isEmpty(this.selectVal)) {
                    this.val = this.dataSource[0];
                } else {
                    this.val = this.selectVal;
                }
                this.onChange(this.val);
            } else {
                this.onChange('');
                this.dataSource = [];
            }
        },
        selectVal(newVal) {
            this.val = newVal;
        }
    },
    methods: {
        async getDataSource(skuId) {
            const response = await findSkuSpecSelectListBySkuId(skuId);
            this.dataSource = response.data.data;
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        },
    }
}
</script>

<style scoped>

</style>
