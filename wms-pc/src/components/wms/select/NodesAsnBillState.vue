<template>
    <nodes-select
        v-model="val"
        :data-source="dataSource"
        :multiple="true"
        @change="onChange"
    >
    </nodes-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {stateService} from "@/api/wms/state/asnBillState";
import func from "@/util/func";

export default {
    name: "NodesAsnBillState",
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
        selectVal(newVal){
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
            this.dataSource = await stateService.getAsnBillState();
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
