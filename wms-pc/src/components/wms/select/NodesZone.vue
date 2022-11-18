<template>
    <el-select
        v-model="val"
        :clearable="true"
        :multiple="multiple"
        :size="size"
        :wh-id-list="whIdList"
        collapse-tags
        value-key="zoneId"
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
import func from "@/util/func";

export default {
    name: "NodesZone",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, Number, String],
        //是否有默认值 true:有默认值  默认为false 编辑时将其设置为true
        defaultValue: {type: Boolean, required: false, default: () => false},
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: () => false},
        // 库房id集合 ,可根据库房id查询该库房下的所有库区  默认为null查询所有库区
        whIdList: {type: [Number, Array, String], required: false, default: () => null},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"},
        // 根据库区名称排除默认值
        notSelectName: {type: Array, required: false, default: () => []},
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
    created() {
    },
    async mounted() {
        await this.getDataSource();
    },
    methods: {
        async getDataSource() {
            let res = await getZoneSelectResponse(this.whIdList);
            this.dataSource = res.data.data;
            this.processingData();
        },
        processingData() {
            if (this.defaultValue) {
                this.val = this.dataSource[0].zoneId
                this.onChange(this.val);
            }
            // 根据名称判断 默认勾选除 notSelectName 中的值以外的库区
            let notSelectId = []
            if (func.isNotEmpty(this.notSelectName)) {
                this.dataSource.forEach(value => {
                    if (!this.notSelectName.includes(value.zoneName)) {
                        notSelectId.push(value.zoneId);
                    }
                })
            }
            if (func.isNotEmpty(notSelectId)) {
                notSelectId.forEach(value => {
                    this.val.push(value)
                })
            }
            this.$emit('initZoneParams', this.val);
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
