<template>
    <el-select
        v-model="val"
        :data-source="dataSource"
        :multiple="true"
        :clearable="true"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.label"
            :label="item.label"
            :value="item.value">
            <span style="float: left">{{ item.label }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
        </el-option>
    </el-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getSoBillState} from "@/api/wms/state/soBillState";
import func from "@/util/func";

export default {
    name: "NodesSoBillState",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: Array
    },
    data() {
        return {
            val: [],
            dataSource: []
        }
    },
    watch: {
        selectVal(newVal) {
            this.val = newVal
        }
    },
    async created() {
        await this.getDataSource();
        if (func.isNotEmpty(this.selectVal)) {
            this.dataSource.forEach((item) => {
                if (this.selectVal.includes(item.value)) {
                    this.val.push(item.value);
                }
            })
        }
    },
    methods: {
        async getDataSource() {
            let {data: {data}} = await getSoBillState();
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
