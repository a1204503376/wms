<template>
    <el-select
        v-model="val"
        :clearable="true"
        :collapse-tags="true"
        :disabled="disabled"
        :size="size"
        placeholder="请选择"
        :filterable="true"
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

/**
 * 新建时，根据skuId获取所有的物品规格并默认显示第一个
 * 编辑的时候默认绑定物品规格编码，获得焦点的时候获取该物料的所有物品规格
 */
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
            val: '',
            dataSource: []
        }
    },
    watch: {
        sku: async function (newVal, oldVal) {
            // sku != {} （空对象）时, 才进行查询
            if (Object.keys(newVal).length !== 0){
                await this.getDataSource(newVal.skuId);
                if (this.dataSource.length > 0) {
                    this.val = this.dataSource[0];
                    this.onChange(this.val);
                } else {
                    this.onChange('');
                }
            }else {
                this.onChange('');
                this.dataSource = [];
            }
        },
        selectVal(newVal) {
            console.log("进入监听"+ newVal);
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
