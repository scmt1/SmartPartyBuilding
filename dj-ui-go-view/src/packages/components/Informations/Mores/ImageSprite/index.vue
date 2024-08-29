<template>
  <div>
    <CssSprite class="sprite"
               ref="sprite" :images="option.dataset" :fps="fps"
               :frames="{ width:width,height:height,count:count}">
    </CssSprite>
  </div>
</template>

<script setup lang="ts">
import { PropType, shallowReactive, watch, toRefs } from 'vue'
import { requireErrorImg } from '@/utils'
import { useChartDataFetch } from '@/hooks'
import { CreateComponentType } from '@/packages/index.d'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import {CssSprite} from '@/components/CssSprite'


const props = defineProps({
  chartConfig: {
    type: Object as PropType<CreateComponentType>,
    required: true
  }
})

const { w, h } = toRefs(props.chartConfig.attr)
const { dataset, fps, width, height,count } = toRefs(props.chartConfig.option)

const option = shallowReactive({
  dataset: '',
})

// 编辑更新
watch(
    () => props.chartConfig.option.dataset,
    (newData: any) => {
      option.dataset = newData
    },
    {
      immediate: true,
      deep: false
    }
)

// 预览更新
useChartDataFetch(props.chartConfig, useChartEditStore, (newData: any) => {
  option.dataset = newData
})

</script>
<!--<style scoped vars="{width,height}">
  .sprite {
    width: var(&#45;&#45;width);
    height: var(&#45;&#45;height);
  }
</style>-->
