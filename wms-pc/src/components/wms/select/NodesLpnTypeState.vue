<template>
    <el-select
        v-model="val"
        :clearable="true"
        :collapse-tags="true"
        :multiple="multiple"
        :size="size"
        placeholder="请选择"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            <span style="float: left">{{ item.value }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.label }}</span>
        </el-option>
    </el-select>

</template>

<script>
import {lpnTypeStateService} from "@/api/wms/basics/LpnTypeState";


export default {
    name: "lpnTypeStateService",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String, Number],
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"},
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
            this.dataSource = await lpnTypeStateService.getLpnTypeState();
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
