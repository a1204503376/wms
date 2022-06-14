
<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        collapse-tags
        size="mini"
        style="width:100%;"
        value-key="zoneId"
        :wh-id-list="whIdList"
        @change="onChange">
        <el-option
            v-for="item in this.dataSource"
            :key="item.zoneId"
            :label="item.zoneName"
            :value="item.zoneId">
            <span style="float: left">{{ item.zoneCode }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.zoneName }}</span>
        </el-option>
    </el-select>
</template>

<script>
import {getZoneSelectResponse} from "@/api/wms/basics/zone";

export default {
    name: "NodesZone",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, Number, String],
        //是否有默认值 true:有默认值  默认为false 编辑时将其设置为true
        defaultValue:{type:Boolean, required: false,default: () => false},
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: () => false},
        // 库房id集合 ,可根据库房id查询该库房下的所有库区  默认为null查询所有库区
        whIdList: {type: [Number, Array, String], required: false, default: ()=> null}
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
        },
    },
    async created() {
        await this.getDataSource()
        if(this.defaultValue){
            this.val = this.dataSource[0].zoneId
            this.onChange(this.val);
        }
    },
    methods: {
        async getDataSource() {
            let {data: {data}} = await getZoneSelectResponse(this.whIdList)
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
