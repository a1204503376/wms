<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        collapse-tags
        placeholder="请选择"
        :clearable="true"
        size="mini"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.value"
            :label="item.label"
            :value="item.label">
        </el-option>
    </el-select>

</template>

<script>
import {getTypeList} from "@/api/wms/stock/stockLog";


export default {
    name: "stockLogType",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
        // 是否多选，默认为false
        multiple: {type: Boolean, required: false, default: false}
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: []
        }
    },
    watch: {
        selectVal(newVal) {
            this.val = newVal;
        }
    },
    async created() {
        await this.getDataSource();
    },
    methods: {
        async getDataSource() {
            let {data: {data}} = await getTypeList();
            this.dataSource = data;
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
