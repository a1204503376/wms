<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        style="width:100%;"
        collapse-tags
        placeholder="请选择"
        size="mini"
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
        if (func.isNumber(this.val)){
            this.val = undefined;
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
