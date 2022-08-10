<template>
    <el-select
        v-model="val"
        :data-source="dataSource"
        placeholder="请选择序列号状态"
        :multiple="true"
        :clearable="true"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
    </el-select>
</template>

<script>
import {getSerialState} from "@/api/wms/state/serialState";

export default {
    name: "NodesSerialState",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: Array
    },
    data() {
        return {
            val: [],
            dataSource: []
        }
    },
    watch: {
        selectVal(newVal) {
            this.val = newVal
        }
    },
    created() {
        this.getDataSource();
    },
    methods: {
        async getDataSource() {
            let {data: {data}} = await getSerialState();
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
