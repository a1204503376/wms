<template>
    <el-select
        v-model="val"
        :default-first-option="true"
        :multiple="multiple"
        :wh-Id="whId"
        :loading="loading"
        :remote-method="remoteMethod"
        filterable
        placeholder="请输入关键词"
        remote
        reserve-keyword
        size="mini"
        style="width: 340px"
        value-key="zoneId"
        @change="onChange">
        <el-option
            v-for="item in options"
            :key="item.zoneId"
            :label="item.zoneName"
            :value="item.zoneId">
            <span style="float: left">{{ item.zoneCode }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.zoneName }}</span>
        </el-option>
    </el-select>
</template>

<script>
import debounce from "lodash/debounce";
import {getZoneSelectResponseTop10List} from "@/api/wms/basics/zone";

export default {
    name: "NodesZone",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
        whId: {type: [Number,Array], required: false, default:()=>null},
        multiple: {type: Boolean, required: false, default:()=>false}
    },
    data() {
        return {
            options: [this.selectVal],
            val: this.selectVal,
            loading: false,
            whIdVal: this.whId
        }
    },
    methods: {
        // 防抖 在等待时间到达前的请求全部取消，保留最后一次
        remoteMethod: debounce(async function (key) {
            if (key !== '') {
                this.loading = true;
                let zoneSelectQuery = {
                    key: key,
                    whId: this.whIdVal
                };
                let {data: {data}} = await getZoneSelectResponseTop10List(zoneSelectQuery);
                this.options = data;
                this.loading = false;
            } else {
                this.options = [];
            }
        }, 500),
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
