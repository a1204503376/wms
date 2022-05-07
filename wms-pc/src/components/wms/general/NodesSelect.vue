<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        collapse-tags
        placeholder="请选择"
        size="mini"
        style="width:100%;"
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

import func from "@/util/func";

export default {
    name: "NodesSelect",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Number, Array, String],
        multiple: {type: Boolean, required: false, default: true},
        dataSource: {type: Array, required: true}
    },
    data() {
        return {
            val: this.selectVal
        }
    },
    created() {
        if (func.isNumber(this.val) && this.val <= 0) {
            this.val = '';
        }
    },
    methods: {
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
