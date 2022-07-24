<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        :clearable="true"
        collapse-tags
        placeholder="请选择"
        :size="size"
        :disabled="disabled"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.whId"
            :label="item.whName"
            :value="item.whId">
            <span style="float: left">{{ item.whCode }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.whName }}</span>
        </el-option>
    </el-select>

</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getWarehouseSelectResponseList} from "@/api/wms/warehouse/warehouse";


export default {
    name: "NodesWarehouse",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false},
        //是否有默认值 true:有默认值  默认为false 编辑时将其设置为true
        defaultValue: {type: Boolean, required: false, default: () => false},
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
    async created() {
        await this.getDataSource()
        if (this.defaultValue) {
            this.val = this.dataSource[0].whId
            this.onChange(this.val);
        }
    },
    watch: {
        selectVal(newVal) {
            this.val = newVal;
        }
    },
    methods: {
        async getDataSource() {
            const response = await getWarehouseSelectResponseList();
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
