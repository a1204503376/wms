<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        :clearable="true"
        collapse-tags
        placeholder="请选择"
        :size="size"
        @change="onChange">
        <el-option
            v-for="item in this.dataSource"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            <span style="float: left">{{ item.value }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.label }}</span>
        </el-option>
    </el-select>

</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getTaskStateSelectResponse} from "@/api/wms/task/taskDetail";


export default {
    name: "NodesTaskState",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
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
    async created() {
        await this.getDataSource()
    },
    watch: {
        selectVal(newVal) {
            this.val = newVal;
        }
    },
    methods: {
        async getDataSource() {
            const response = await getTaskStateSelectResponse();
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
