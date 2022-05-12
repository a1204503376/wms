<template>
    <nodes-select
        v-model="val"
        :data-source="dataSource"
        :multiple="true"
    >
    </nodes-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {lpnTypeStateService} from "@/api/wms/basics/LpnTypeState";
import func from "@/util/func";

export default {
    name: "NodesLpnTypeState",
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
        val(newVal) {
            let result = newVal;
            if (func.isArray(newVal)) {
                result = newVal.map(d => d.value);
            } else if (func.isObject(newVal)) {
                result = newVal.value
            }
            this.$emit('selectValChange', result);
        }
    },
    async created() {
        await this.getDataSource();
        if (func.isNotEmpty(this.selectVal)) {
            this.dataSource.forEach((item) => {
                if (this.selectVal.includes(item.value)) {
                    this.val.push(item);
                }
            })
        }
    },
    methods: {
        async getDataSource() {
            this.dataSource = await lpnTypeStateService.getLpnTypeState();
        }
    }
}
</script>

<style scoped>

</style>
