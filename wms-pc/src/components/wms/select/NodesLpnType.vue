<template>
    <el-select
        v-model="val"
        :collapse-tags="true"
        :multiple="multiple"
        :clearable="true"
        :size="size"
        value-key="id"
        @change="onChange">
        <el-option
            v-for="item in this.dataSource"
            :key="item.id"
            :label="item.code"
            :value="item.id">
            <span style="float: left">{{ item.code }}</span>
        </el-option>
    </el-select>
</template>

<script>
import {getLpnTypeSelectList} from "@/api/wms/basics/LpnType";

export default {
    name: "NodesLpnType",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
        //是否有默认值 true:有默认值  默认为false 编辑时将其设置为true
        defaultValue:{type:Boolean, required: false,default: () => false},
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
            this.val=newVal;
        },
    },
    async created() {
        await this.getDataSource()
        if(this.defaultValue){
            this.val = this.dataSource[0].id
            this.onChange(this.val);
        }
    },
    methods: {
        async getDataSource() {
            let {data: {data}} = await getLpnTypeSelectList()
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
