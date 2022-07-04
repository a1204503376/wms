<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        size="mini"
        value-key="woId"
        @change="onChange">
        <el-option
            v-for="item in this.dataSource"
            :key="item.woId"
            :label="item.ownerName"
            :value="item.woId">
            <span style="float: left">{{ item.ownerCode }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.ownerName }}</span>
        </el-option>
    </el-select>
</template>

<script>
import {getOwnerSelectResponseTop10List} from "@/api/wms/basics/owner";

export default {
    name: "NodesOwner",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Object, String, Array],
        // 是否多选 true:多选 默认为单选
        multiple: {type: Boolean, required: false, default: () => false},
        //是否有默认值 true:有默认值  默认为false 编辑时将其设置为true
        defaultValue:{type:Boolean, required: false, default: () => false}
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: [],
        }
    },
    watch: {
        selectVal(newVal) {
            this.val=newVal;
        },
    },
    async created() {
        await this.getDataSource();
        if(this.defaultValue){
            this.val = this.dataSource[0].woId
            this.onChange(this.val);
        }
    },
    methods: {
        async getDataSource() {
            let {data: {data}} = await getOwnerSelectResponseTop10List();
            this.dataSource = data
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
