
<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        :clearable="true"
        size="mini"
        style="width:100%;"
        value-key="value"
        @change="onChange">
        <el-option
            v-for="item in this.dataSource"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            <span style="float: left">{{ item.value }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.label}}</span>
        </el-option>
    </el-select>
</template>

<script>

import {getStockStatusList} from "@/api/wms/stock/stock";
import {getWarehouseSelectResponseList} from "@/api/wms/warehouse/warehouse";

export default {
    name: "NodesInStoreMode",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Number, Array, String],
        //是否有默认值 true:有默认值  默认为false 编辑时将其设置为true
        defaultValue:{type:Boolean,required: false,default: () => false},
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false},
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: []
        }
    },
    watch: {
        selectVal(newVal) {
            this.val=newVal;
        },
    },
    async created() {
        await this.getDataSource()
        if(this.defaultValue){
            this.val = this.dataSource[0].value
            this.onChange(this.val);
        }
    },
    methods: {
        async getDataSource() {
            const response = await getStockStatusList();
            this.dataSource = response.data.data;
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
