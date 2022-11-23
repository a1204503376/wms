<template>
    <div class="cc">
      
    </div>
</template>

<script>
import {getInStockRate, getOutStockRate, getStockData} from "@/api/wms/index/index.js";
import "../../../public/cdn/iconfont/index/iconfont.css"

export default {
    name: "index",
    data() {
        return {
            
        };
    },
    created() {
    },
   
    deactivated() {
    },
    methods: {
      
    }
};
</script>

<style>
   .cc{
             width: 100%;    
            height: 100%;
            position: absolute;
            background-color: white;    
            background-image: url('/img/bg/03.png');
            background-repeat:no-repeat;
            background-position:center 35%;
        }

</style>
