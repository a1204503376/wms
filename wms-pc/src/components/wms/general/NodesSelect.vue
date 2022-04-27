<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        class="d-select"
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

export default {
    name: "NodesSelect",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Number, Array],
        multiple: {type: Boolean, required: false, default: true},
        dataSource: {type: Array, required: true}
    },
    data() {
        return {
            val: this.selectVal
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
